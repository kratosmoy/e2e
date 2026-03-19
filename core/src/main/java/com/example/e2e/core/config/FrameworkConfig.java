package com.example.e2e.core.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public record FrameworkConfig(
        String baseUrl,
        boolean headless,
        int slowMo,
        String browser,
        Path tracesDir,
        Path screenshotsDir,
        Path videosDir
) {
    public static FrameworkConfig fromSystemProperties() {
        Path artifactsRoot = Paths.get(System.getProperty("artifacts.dir", "build/artifacts"));
        return new FrameworkConfig(
                System.getProperty("base.url", "https://playwright.dev"),
                Boolean.parseBoolean(System.getProperty("headless", "true")),
                Integer.getInteger("slowmo", 0),
                System.getProperty("browser", "chromium"),
                artifactsRoot.resolve("traces"),
                artifactsRoot.resolve("screenshots"),
                artifactsRoot.resolve("videos")
        );
    }
}
