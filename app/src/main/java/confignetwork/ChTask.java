package confignetwork;

import android.content.Context;

import java.util.List;

import confignetwork.task.ChTaskParameter;
import confignetwork.task.IChTaskParameter;
import confignetwork.task.__ChTask;


public class ChTask implements IChTask {

    public __ChTask _mChTask;
    private IChTaskParameter _mParameter;

    /**
     * Constructor of ChTask
     *
     * @param apSsid       the Ap's ssid
     * @param apBssid      the Ap's bssid
     * @param apPassword   the Ap's password
     * @param isSsidHidden whether the Ap's ssid is hidden
     * @param context      the Context of the Application
     */
    public ChTask(String apSsid, String apBssid, String apPassword, boolean isSsidHidden, Context context) {
        _mParameter = new ChTaskParameter();
        _mChTask = new __ChTask(apSsid, apBssid, apPassword, context, _mParameter, isSsidHidden);
    }

    /**
     * Constructor of ChTask
     *
     * @param apSsid             the Ap's ssid
     * @param apBssid            the Ap's bssid
     * @param apPassword         the Ap's password
     * @param isSsidHidden       whether the Ap's ssid is hidden
     * @param timeoutMillisecond (it should be >= 15000+6000) millisecond of total timeout
     * @param context            the Context of the Application
     */
    public ChTask(String apSsid, String apBssid, String apPassword, boolean isSsidHidden, int timeoutMillisecond, Context context) {
        _mParameter = new ChTaskParameter();
        _mParameter.setWaitUdpTotalMillisecond(timeoutMillisecond);
        _mChTask = new __ChTask(apSsid, apBssid, apPassword, context, _mParameter, isSsidHidden);
    }

    @Override
    public void interrupt() {
        _mChTask.interrupt();
    }

    @Override
    public IChResult executeForResult() throws RuntimeException {
        return _mChTask.executeForResult();
    }

    @Override
    public boolean isCancelled() {
        return _mChTask.isCancelled();
    }

    @Override
    public List<IChResult> executeForResults(int expectTaskResultCount) throws RuntimeException {
        if (expectTaskResultCount <= 0) {
            expectTaskResultCount = Integer.MAX_VALUE;
        }
        return _mChTask.executeForResults(expectTaskResultCount);
    }

    @Override
    public void setChListener(IChListener chListener) {
        _mChTask.setChListener(chListener);
    }
}
