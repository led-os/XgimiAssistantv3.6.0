package confignetwork.task;


import java.util.List;

import confignetwork.IChListener;
import confignetwork.IChResult;


/**
 * IChTask defined the task of ch should offer. INTERVAL here means
 * the milliseconds of interval of the step. REPEAT here means the repeat times
 * of the step.
 * 
 * @author afunx
 * 
 */
public interface __IChTask {

	/**
	 * set the ch listener, when one device is connected to the Ap, it will be called back
	 * @param chListener when one device is connected to the Ap, it will be called back
	 */
	void setChListener(IChListener chListener);
	
	/**
	 * Interrupt the Ch Task when User tap back or close the Application.
	 */
	void interrupt();

	/**
	 * Note: !!!Don't call the task at UI Main Thread or RuntimeException will
	 * be thrown Execute the Ch Task and return the result
	 * 
	 * @return the IChResult
	 * @throws RuntimeException
	 */
	IChResult executeForResult() throws RuntimeException;

	/**
	 * Note: !!!Don't call the task at UI Main Thread or RuntimeException will
	 * be thrown Execute the Ch Task and return the result
	 * 
	 * @param expectTaskResultCount
	 *            the expect result count(if expectTaskResultCount <= 0,
	 *            expectTaskResultCount = Integer.MAX_VALUE)
	 * @return the list of IChResult
	 * @throws RuntimeException
	 */
	List<IChResult> executeForResults(int expectTaskResultCount) throws RuntimeException;
	
	/**
	 * Turn on or off the log.
	 */
	static final boolean DEBUG = true;

	boolean isCancelled();
}
