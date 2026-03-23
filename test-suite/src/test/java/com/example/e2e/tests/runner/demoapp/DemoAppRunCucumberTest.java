package com.example.e2e.tests.runner.demoapp;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Suite
@IncludeEngines("cucumber")
@DisabledOnOs(OS.LINUX)
@SelectClasspathResource("features/demoapp")
public class DemoAppRunCucumberTest {
}
