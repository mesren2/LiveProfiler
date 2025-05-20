package net.mesren2.liveprofiler;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Map;

public class ProfilerOverlay {
    private static boolean visible = true;
    private static final int GRAPH_WIDTH = 150;
    private static final int GRAPH_HEIGHT = 60;
    private static final int GRAPH_MARGIN = 5;

    public static void toggle() {
        visible = !visible;
    }

    public static void render(MatrixStack matrices, DrawContext drawContext) {
        if (!visible) return;

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        Map<String, Double> averages = ProfilerManager.getAverages();
        Map<String, long[]> recentSamples = ProfilerManager.getRecentSamples();

        int x = 10;
        int y = 10;

        // Draw averages as text
        for (Map.Entry<String, Double> entry : averages.entrySet()) {
            String text = String.format("%s: %.3f ms", entry.getKey(), entry.getValue());
            drawContext.drawText(textRenderer, text, x, y, 0xFFFFFFFF, true);
            y += 12;
        }

        y += 10;

        // Draw XY graph of recent samples below averages
        int graphX = x;
        int graphY = y;

        int i = 0;
        for (Map.Entry<String, long[]> entry : recentSamples.entrySet()) {
            String section = entry.getKey();
            long[] samples = entry.getValue();

            drawContext.drawText(textRenderer, section, graphX, graphY + i * (GRAPH_HEIGHT + GRAPH_MARGIN + 10), 0xFFFFFF, true);

            drawGraph(drawContext, samples, graphX, graphY + 10 + i * (GRAPH_HEIGHT + GRAPH_MARGIN + 10), GRAPH_WIDTH, GRAPH_HEIGHT);

            i++;
        }
    }

    private static void drawGraph(DrawContext drawContext, long[] samples, int x, int y, int width, int height) {
        // Find max to scale graph
        long max = 1;
        for (long s : samples) {
            if (s > max) max = s;
        }

        // Background
        drawContext.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0x80000000);

        // Y axis labels
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        String maxLabel = String.format("%.1f ms", max / 1_000_000.0);
        String midLabel = String.format("%.1f ms", max / 2_000_000.0);
        String zeroLabel = "0 ms";

        int yMaxPos = y + 5;
        int yMidPos = y + height / 2;
        int yZeroPos = y + height - 10;

        drawContext.drawText(textRenderer, maxLabel, x - 40, yMaxPos, 0xFFFFFFFF, true);
        drawContext.drawText(textRenderer, midLabel, x - 40, yMidPos, 0xFFFFFFFF, true);
        drawContext.drawText(textRenderer, zeroLabel, x - 40, yZeroPos, 0xFFFFFFFF, true);

        // X axis labels (sample indices)
        int len = samples.length;
        float xScale = (float) width / (len - 1);
        int step = Math.max(1, len / 4);

        for (int i = 0; i < len; i += step) {
            int xPos = x + (int)(i * xScale);
            String label = String.valueOf(i);

            drawContext.drawVerticalLine(xPos, y + height, y + height + 5, 0xFFFFFFFF);
            drawContext.drawText(textRenderer, label, xPos - textRenderer.getWidth(label)/2, y + height + 7, 0xFFFFFFFF, true);
        }

        // Red "High (slow)" label at top right
        String highLabel = "High (slow)";
        int highLabelX = x + width - textRenderer.getWidth(highLabel);
        int highLabelY = y - 10;
        drawContext.drawText(textRenderer, highLabel, highLabelX, highLabelY, 0xFFFF5555, true);

        // Draw the line graph
        for (int i = 0; i < len - 1; i++) {
            float sx1 = x + i * xScale;
            float sx2 = x + (i + 1) * xScale;

            float sy1 = y + height - (float) samples[i] / max * height;
            float sy2 = y + height - (float) samples[i + 1] / max * height;

            int color = interpolateColor(samples[i], max);

            drawContext.drawHorizontalLine((int) sx1, (int) sx2, (int) sy1, color);
        }
    }

    private static int interpolateColor(long value, long max) {
        // Gradient from green (fast) to red (slow)
        float ratio = Math.min(1f, (float) value / max);
        int r = (int) (255 * ratio);
        int g = (int) (255 * (1 - ratio));
        int b = 0;
        return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }
}
