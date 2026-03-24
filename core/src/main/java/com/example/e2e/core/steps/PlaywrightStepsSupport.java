package com.example.e2e.core.steps;

import com.example.e2e.core.context.ScenarioContext;
import com.example.e2e.core.playwright.PlaywrightManager;
import com.microsoft.playwright.Page;

public abstract class PlaywrightStepsSupport {
    protected final Page page() {
        return PlaywrightManager.page();
    }

    protected final ScenarioContext scenarioContext() {
        return PlaywrightManager.scenarioContext();
    }

    protected final void openHomePage(String appName) {
        page().navigate(PlaywrightManager.config().homePageUrl(appName));
    }
}
