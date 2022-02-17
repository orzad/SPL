package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * 
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {

	private T result;
	private boolean done;
	
	/**
	 * This should be the the only public constructor in this class.
	 */
	public Future() {
		result = null;
		done = false;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     * @return return the result of type T if it is available, if not wait until it is available.
     * @pre: isDone == true.
	 * @post: none.
     */

	public T get() {
		try {
			if (result == null) {
				wait();
			}
		}
		catch (Exception e) {
			System.out.println("probleme in get of future: wait()");
		}
		return result;
	}
	
	/**
     * Resolves the result of this Future object.
	 * @pre: (isDone == false) and (result == null).
	 * @post: (isDone == true) and (result != null).
     */
	public void resolve (T result) {
		this.result = result;
		this.done = true;

	}
	
	/**
     * @return true if this object has been resolved, false otherwise
     */
	public boolean isDone() {
		return done;
	}
	
	/**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     * @param timeout 	the maximal amount of time units to wait for the result.
     * @param unit		the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not, 
     * 	       wait for {@code timeout} TimeUnits {@code unit}. If time has
     *         elapsed, return null.
	 * @pre: none
	 * @post: if (time>timeout): return null, else: if(result!=null): return result, else: wait.
     */
	public T get(long timeout, TimeUnit unit) {

		//Time convertion still to be done.

		try {
			if (result == null) {
				wait(timeout);
			}
		}
		catch (Exception e) {
			System.out.println("probleme in get of future: wait()");
		}
		return result;
	}

}
