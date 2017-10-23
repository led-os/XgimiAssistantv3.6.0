package confignetwork.task;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import confignetwork.ChResult;
import confignetwork.IChListener;
import confignetwork.IChResult;
import confignetwork.protocol.ChGenerator;
import confignetwork.udp.UDPSocketClient;
import confignetwork.udp.UDPSocketServer;
import confignetwork.util.ByteUtil;
import confignetwork.util.NetUtil;

public class __ChTask implements __IChTask {

	/**
	 * one indivisible data contain 3 9bits info
	 */
	private static final int ONE_DATA_LEN = 3;

	private static final String TAG = "ChTask";

	private volatile List<IChResult> mChResultList;
	private volatile boolean mIsSuc = false;
	private volatile boolean mIsInterrupt = false;
	private volatile boolean mIsExecuted = false;
	private final UDPSocketClient mSocketClient;
	private final UDPSocketServer mSocketServer;
	private final String mApSsid;
	private final String mApBssid;
	private final boolean mIsSsidHidden;
	private final String mApPassword;
	private final Context mContext;
	private AtomicBoolean mIsCancelled;
	private IChTaskParameter mParameter;
	private volatile Map<String, Integer> mBssidTaskSucCountMap;
	private IChListener mChListener;

	public __ChTask(String apSsid, String apBssid, String apPassword,
					Context context, IChTaskParameter parameter,
					boolean isSsidHidden) {
		if (TextUtils.isEmpty(apSsid)) {
			throw new IllegalArgumentException("the apSsid should be null or empty");
		}
		if (apPassword == null) {
			apPassword = "";
		}
		mContext = context;
		mApSsid = apSsid;
		mApBssid = apBssid;
		mApPassword = apPassword;
		mIsCancelled = new AtomicBoolean(false);
		mSocketClient = new UDPSocketClient();
		mParameter = parameter;
		mSocketServer = new UDPSocketServer(mParameter.getPortListening(),
				mParameter.getWaitUdpTotalMillisecond(), context);
		mIsSsidHidden = isSsidHidden;
		mChResultList = new ArrayList<IChResult>();
		mBssidTaskSucCountMap = new HashMap<String, Integer>();
	}

	private void __putChResult(boolean isSuc, String bssid, InetAddress inetAddress) {
		synchronized (mChResultList) {
			// check whether the result receive enough UDP response
			boolean isTaskSucCountEnough = false;
			Integer count = mBssidTaskSucCountMap.get(bssid);
			if (count == null) {
				count = 0;
			}
			++count;
			if (__IChTask.DEBUG) {
				Log.d(TAG, "__putChResult(): count = " + count);
			}
			mBssidTaskSucCountMap.put(bssid, count);
			isTaskSucCountEnough = count >= mParameter
					.getThresholdSucBroadcastCount();
			if (!isTaskSucCountEnough) {
				if (__IChTask.DEBUG) {
					Log.d(TAG, "__putChResult(): count = " + count + ", isn't enough");
				}
				return;
			}
			// check whether the result is in the mChResultList already
			boolean isExist = false;
			for (IChResult chResultInList : mChResultList) {
				if (chResultInList.getBssid().equals(bssid)) {
					isExist = true;
					break;
				}
			}
			// only add the result who isn't in the mChResultList
			if (!isExist) {
				if (__IChTask.DEBUG) {
					Log.d(TAG, "__putChResult(): put one more result");
				}
				final IChResult chResult = new ChResult(isSuc, bssid, inetAddress);
				mChResultList.add(chResult);
				if (mChListener != null) {
					mChListener.onChResultAdded(chResult);
				}
			}
		}
	}

	private List<IChResult> __getChResultList() {
		synchronized (mChResultList) {
			if (mChResultList.isEmpty()) {
				ChResult chResultFail = new ChResult(false, null, null);
				chResultFail.setIsCancelled(mIsCancelled.get());
				mChResultList.add(chResultFail);
			}
			
			return mChResultList;
		}
	}

	private synchronized void __interrupt() {
		if (!mIsInterrupt) {
			mIsInterrupt = true;
			mSocketClient.interrupt();
			mSocketServer.interrupt();
			// interrupt the current Thread which is used to wait for udp response
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void interrupt() {
		if (__IChTask.DEBUG) {
			Log.d(TAG, "interrupt()");
		}
		mIsCancelled.set(true);
		__interrupt();
	}

	private void __listenAsyn(final int expectDataLen) {
		new Thread() {
			public void run() {
				if (__IChTask.DEBUG) {
					Log.d(TAG, "__listenAsyn() start");
				}
				long startTimestamp = System.currentTimeMillis();
				byte[] apSsidAndPassword = ByteUtil.getBytesByString(mApSsid + mApPassword);
				byte expectOneByte = (byte) (apSsidAndPassword.length + 9);
				if (__IChTask.DEBUG) {
					Log.i(TAG, "expectOneByte: " + (0 + expectOneByte));
				}
				byte receiveOneByte = -1;
				byte[] receiveBytes = null;
				while (mChResultList.size() < mParameter.getExpectTaskResultCount() && !mIsInterrupt) {
					receiveBytes = mSocketServer.receiveSpecLenBytes(expectDataLen);
					if (receiveBytes != null) {
						receiveOneByte = receiveBytes[0];
					} else {
						receiveOneByte = -1;
					}
					if (receiveOneByte == expectOneByte) {
						if (__IChTask.DEBUG) {
							Log.i(TAG, "receive correct broadcast");
						}
						// change the socket's timeout
						long consume = System.currentTimeMillis() - startTimestamp;
						int timeout = (int) (mParameter.getWaitUdpTotalMillisecond() - consume);
						if (timeout < 0) {
							if (__IChTask.DEBUG) {
								Log.i(TAG, "ch timeout");
							}
							break;
						} else {
							if (__IChTask.DEBUG) {
								Log.i(TAG, "mSocketServer's new timeout is " + timeout + " milliseconds");
							}
							mSocketServer.setSoTimeout(timeout);
							if (__IChTask.DEBUG) {
								Log.i(TAG, "receive correct broadcast");
							}
							if (receiveBytes != null) {
								String bssid = ByteUtil.parseBssid(
										receiveBytes,
										mParameter.getChResultOneLen(),
										mParameter.getChResultMacLen());
								InetAddress inetAddress = NetUtil.parseInetAddr(
												receiveBytes,
												mParameter.getChResultOneLen() + mParameter.getChResultMacLen(),
												mParameter.getChResultIpLen());
								__putChResult(true, bssid, inetAddress);
							}
						}
					} else {
						if (__IChTask.DEBUG) {
							Log.i(TAG, "receive rubbish message, just ignore");
						}
					}
				}
				mIsSuc = mChResultList.size() >= mParameter.getExpectTaskResultCount();
				__ChTask.this.__interrupt();
				if (__IChTask.DEBUG) {
					Log.d(TAG, "__listenAsyn() finish");
				}
			}
		}.start();
	}

	private boolean __execute(IChGenerator generator) {

		long startTime = System.currentTimeMillis();
		long currentTime = startTime;
		long lastTime = currentTime - mParameter.getTimeoutTotalCodeMillisecond();

		byte[][] gcBytes2 = generator.getGCBytes2();
		byte[][] dcBytes2 = generator.getDCBytes2();

		int index = 0;
		while (!mIsInterrupt) {
			if (currentTime - lastTime >= mParameter.getTimeoutTotalCodeMillisecond()) {
				if (__IChTask.DEBUG) {
					Log.d(TAG, "send gc code ");
				}
				// send guide code
				while (!mIsInterrupt && System.currentTimeMillis() - currentTime < mParameter.getTimeoutGuideCodeMillisecond()) {
					mSocketClient.sendData(gcBytes2,
							mParameter.getTargetHostname(),
							mParameter.getTargetPort(),
							mParameter.getIntervalGuideCodeMillisecond());
					// check whether the udp is send enough time
					if (System.currentTimeMillis() - startTime > mParameter.getWaitUdpSendingMillisecond()) {
						break;
					}
				}
				lastTime = currentTime;
			} else {
				mSocketClient.sendData(dcBytes2, index, ONE_DATA_LEN,
						mParameter.getTargetHostname(),
						mParameter.getTargetPort(),
						mParameter.getIntervalDataCodeMillisecond());
				index = (index + ONE_DATA_LEN) % dcBytes2.length;
			}
			currentTime = System.currentTimeMillis();
			// check whether the udp is send enough time
			if (currentTime - startTime > mParameter.getWaitUdpSendingMillisecond()) {
				break;
			}
		}

		return mIsSuc;
	}

	private void __checkTaskValid() {
		// !!!NOTE: the ch task could be executed only once
		if (this.mIsExecuted) {
			throw new IllegalStateException("the Ch task could be executed only once");
		}
		this.mIsExecuted = true;
	}

	@Override
	public IChResult executeForResult() throws RuntimeException {
		return executeForResults(1).get(0);
	}

	@Override
	public boolean isCancelled() {
		return this.mIsCancelled.get();
	}

	@Override
	public List<IChResult> executeForResults(int expectTaskResultCount)
			throws RuntimeException {
		__checkTaskValid();

		mParameter.setExpectTaskResultCount(expectTaskResultCount);

		if (__IChTask.DEBUG) {
			Log.d(TAG, "execute()");
		}
		if (Looper.myLooper() == Looper.getMainLooper()) {
			throw new RuntimeException(
					"Don't call the ch Task at Main(UI) thread directly.");
		}
		InetAddress localInetAddress = NetUtil.getLocalInetAddress(mContext);
		if (__IChTask.DEBUG) {
			Log.i(TAG, "localInetAddress: " + localInetAddress);
		}
		// generator the ch byte[][] to be transformed, which will cost
		// some time(maybe a bit much)
		IChGenerator generator = new ChGenerator(mApSsid, mApBssid,
				mApPassword, localInetAddress, mIsSsidHidden);
		// listen the ch result asyn
		__listenAsyn(mParameter.getChResultTotalLen());
		boolean isSuc = false;
		for (int i = 0; i < mParameter.getTotalRepeatTime(); i++) {
			isSuc = __execute(generator);
			if (isSuc) {
				return __getChResultList();
			}
		}

		if (!mIsInterrupt) {
			// wait the udp response without sending udp broadcast
			try {
				Thread.sleep(mParameter.getWaitUdpReceivingMillisecond());
			} catch (InterruptedException e) {
				// receive the udp broadcast or the user interrupt the task
				if (this.mIsSuc) {
					return __getChResultList();
				} else {
					this.__interrupt();
					return __getChResultList();
				}
			}
			this.__interrupt();
		}
		
		return __getChResultList();
	}

	@Override
	public void setChListener(IChListener chListener) {
		mChListener = chListener;
	}

}
