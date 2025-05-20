package net.mesren2.liveprofiler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class ProfilerExporter {
    public static void exportToCSV() {
        Map<String, Double> averages = net.mesren2.liveprofiler.ProfilerManager.getAverages();
        try (FileWriter writer = new FileWriter(Paths.get("liveprofiler.csv").toFile())) {
            writer.write("Section,Average(ms)\n");
            for (Map.Entry<String, Double> entry : averages.entrySet()) {
                writer.write(String.format("%s,%.3f\n", entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exportToJSON() {
        Map<String, Double> averages = net.mesren2.liveprofiler.ProfilerManager.getAverages();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(Paths.get("liveprofiler.json").toFile())) {
            gson.toJson(averages, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
