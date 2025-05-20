package net.mesren2.liveprofiler;

import java.util.HashMap;
import java.util.Map;

public class LiveProfiler {
	private static final Map<String, Long> startTimes = new HashMap<>();

	public static void begin(String section) {
		startTimes.put(section, System.nanoTime());
	}

	public static void end(String section) {
		long endTime = System.nanoTime();
		long startTime = startTimes.getOrDefault(section, endTime);
		ProfilerManager.record(section, endTime - startTime);
	}
}
