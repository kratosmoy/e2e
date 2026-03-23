package com.example.e2e.tests.demoapp.steps;

import com.example.e2e.core.steps.PlaywrightStepsSupport;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DemoNavigationSteps extends PlaywrightStepsSupport {
    @Given("the user opens the relative path {string}")
    public void theUserOpensTheRelativePath(String path) {
        page().navigate(path);
    }

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String expectedTitle) {
        String title = page().title();
        assertTrue(title.contains(expectedTitle),
                () -> "Expected title to contain '%s' but was '%s'".formatted(expectedTitle, title));
    }
}
