package com.example.e2e.core.hooks;

import com.example.e2e.core.playwright.PlaywrightManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import java.nio.file.Path;

public class CucumberHooks {
    @Before(order = 0)
    public void beforeScenario(Scenario scenario) {
        PlaywrightManager.start(scenario.getName());
    }

    @After(order = 100)
    public void afterScenario(Scenario scenario) {
        try {
            if (scenario.isFailed()) {
                byte[] screenshot = PlaywrightManager.page().screenshot();
                scenario.attach(screenshot, "image/png", scenario.getName());
            }

            Path tracePath = PlaywrightManager.config().tracesDir()
                    .resolve(scenario.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-") + ".zip");
            PlaywrightManager.page().context().tracing().stop(
                    new com.microsoft.playwright.Tracing.StopOptions().setPath(tracePath)
            );
        } finally {
            PlaywrightManager.stop();
        }
    }
}
