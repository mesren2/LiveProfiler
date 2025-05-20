package net.mesren2.liveprofiler;

import java.util.HashMap;
import java.util.Map;

public class ProfilerManager {
    private static final Map<String, Long> totalTimes = new HashMap<>();
    private static final Map<String, Integer> counts = new HashMap<>();

    // For graphing, keep track of recent samples per section
    private static final int MAX_SAMPLES = 100;
    private static final Map<String, long[]> recentSamples = new HashMap<>();
    private static final Map<String, Integer> sampleIndices = new HashMap<>();

    public static void record(String section, long duration) {
        totalTimes.put(section, totalTimes.getOrDefault(section, 0L) + duration);
        counts.put(section, counts.getOrDefault(section, 0) + 1);

        // Add to recent samples for graphing
        long[] samples = recentSamples.computeIfAbsent(section, k -> new long[MAX_SAMPLES]);
        int idx = sampleIndices.getOrDefault(section, 0);
        samples[idx] = duration;
        sampleIndices.put(section, (idx + 1) % MAX_SAMPLES);
    }

    public static Map<String, Double> getAverages() {
        Map<String, Double> averages = new HashMap<>();
        for (String key : totalTimes.keySet()) {
            long total = totalTimes.get(key);
            int count = counts.getOrDefault(key, 1);
            averages.put(key, total / (double) count / 1_000_000.0); // Convert ns to ms
        }
        return averages;
    }

    public static Map<String, long[]> getRecentSamples() {
        return recentSamples;
    }

    public static void reset() {
        totalTimes.clear();
        counts.clear();
        recentSamples.clear();
        sampleIndices.clear();
    }
}
