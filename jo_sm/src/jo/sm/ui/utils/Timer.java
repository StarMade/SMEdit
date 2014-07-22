/**
 * Copyright 2014 
 * SMEdit https://github.com/StarMade/SMEdit
 * SMTools https://github.com/StarMade/SMTools
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 **/
package jo.sm.ui.utils;

/**
 * A Timer
 */
public class Timer {

	/**
	 * Converts milliseconds to a String in the format hh:mm:ss.
	 * 
	 * @param time The number of milliseconds.
	 * @return The formatted String.
	 */
	public static String format(final long time) {
		final StringBuilder t = new StringBuilder();
		final long total_secs = time / 1000;
		final long total_mins = total_secs / 60;
		final long total_hrs = total_mins / 60;
		final int secs = (int) total_secs % 60;
		final int mins = (int) total_mins % 60;
		final int hrs = (int) total_hrs % 60;
		if (hrs < 10) {
			t.append("0");
		}
		t.append(hrs);
		t.append(":");
		if (mins < 10) {
			t.append("0");
		}
		t.append(mins);
		t.append(":");
		if (secs < 10) {
			t.append("0");
		}
		t.append(secs);
		return t.toString();
	}
	private long end;
	private final long start;

	private final long period;

	/**
	 * Instantiates a new Timer with a given time period in milliseconds.
	 * 
	 * @param period
	 *            Time period in milliseconds.
	 */
	public Timer(final long period) {
		this.period = period;
		this.start = System.currentTimeMillis();
		this.end = start + period;
	}

	/**
	 * Returns the number of milliseconds elapsed since the start time.
	 * 
	 * @return The elapsed time in milliseconds.
	 */
	public long getElapsed() {
		return (System.currentTimeMillis() - start);
	}

	/**
	 * Returns the number of milliseconds remaining until the timer is up.
	 * 
	 * @return The remaining time in milliseconds.
	 */
	public long getRemaining() {
		if (isRunning()) {
			return (end - System.currentTimeMillis());
		}
		return 0;
	}

	/**
	 * Returns <tt>true</tt> if this timer's time period has not yet elapsed.
	 * 
	 * @return <tt>true</tt> if the time period has not yet passed.
	 */
	public boolean isRunning() {
		return (System.currentTimeMillis() < end);
	}

	/**
	 * Restarts this timer using its period.
	 */
	public void reset() {
		this.end = System.currentTimeMillis() + period;
	}

	/**
	 * Sets the end time of this timer to a given number of milliseconds from
	 * the time it is called. This does not edit the period of the timer (so
	 * will not affect operation after reset).
	 * 
	 * @param ms
	 *            The number of milliseconds before the timer should stop
	 *            running.
	 * @return The new end time.
	 */
	public long setEndIn(final long ms) {
		this.end = System.currentTimeMillis() + ms;
		return this.end;
	}

	/**
	 * Returns a formatted String of the time elapsed.
	 * 
	 * @return The elapsed time formatted hh:mm:ss.
	 */
	public String toElapsedString() {
		return format(getElapsed());
	}

	/**
	 * Returns a formatted String of the time remaining.
	 * 
	 * @return The remaining time formatted hh:mm:ss.
	 */
	public String toRemainingString() {
		return format(getRemaining());
	}
        
        /**
	 * @param ms
	 *            The time to sleep in milliseconds.
	 */
	public void sleep(final int ms) {
		try {
			final long s = System.currentTimeMillis();
			Thread.sleep(ms);
			long now; // Guarantee minimum sleep
			while (s + ms > (now = System.currentTimeMillis())) {
				Thread.sleep(s + ms - now);
			}
		} catch (final InterruptedException ignored) {
		}
	}
}