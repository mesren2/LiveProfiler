package net.mesren2.liveprofiler.example;

import net.mesren2.liveprofiler.LiveProfiler;

public class TestFeatures {

    public static void register() {
        LiveProfiler.begin("MyFeature");
        LiveProfiler.end("MyFeature");
    }
}
