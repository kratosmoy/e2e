package com.example.e2e.tests.demoapp.steps;

import com.example.e2e.core.steps.PlaywrightStepsSupport;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoHomeSteps extends PlaywrightStepsSupport {
    @Then("the get started link should be visible")
    public void theGetStartedLinkShouldBeVisible() {
        boolean visible = page()
                .locator("text=Get started")
                .first()
                .isVisible();
        assertTrue(visible, "Expected Playwright documentation landing page to show the Get started link");
    }
}
