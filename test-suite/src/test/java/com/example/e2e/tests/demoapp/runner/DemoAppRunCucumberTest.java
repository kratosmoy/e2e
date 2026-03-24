package com.example.e2e.tests.demoapp.runner;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Suite
@IncludeEngines("cucumber")
@DisabledOnOs(OS.LINUX)
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "com.example.e2e.core.hooks,com.example.e2e.tests.demoapp.steps"
)
@SelectClasspathResource("apps/demoapp/features")
public class DemoAppRunCucumberTest {
}
