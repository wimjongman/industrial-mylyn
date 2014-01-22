package com.industrialtsi.mylyn.tests.util;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

public final class WaitUtils {

	public static void delay(long ms) {
		Display display = Display.getCurrent();

		if (null != display) {
			long end = System.currentTimeMillis() + ms;
			while (System.currentTimeMillis() < end) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
				display.update();
			}
		} else {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	public static void waitForJobs(long delay) {
		while (!Job.getJobManager().isIdle()) {
			delay(delay);
		}
	}

}
