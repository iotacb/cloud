package de.kostari.cloud.utilities.time;

public class Timer {

	private long pastMS;

	/**
	 * Will return true if the time provided in milliseconds
	 * has passed.
	 * 
	 * @param milliseconds
	 * @return
	 */
	public boolean hasTimePassed(long milliseconds) {
		if (System.currentTimeMillis() - pastMS > milliseconds) {
			reset();
			return true;
		}
		return false;
	}

	/**
	 * Used to manually reset the timer.
	 */
	public void reset() {
		pastMS = System.currentTimeMillis();
	}

}
