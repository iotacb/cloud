package de.kostari.cloud.utilities.time;

public class Timer {

	private long pastMS;

	public boolean havePassed(long milliseconds) {
		if (System.currentTimeMillis() - pastMS > milliseconds) {
			reset();
			return true;
		}
		return false;
	}

	public void reset() {
		pastMS = System.currentTimeMillis();
	}

}
