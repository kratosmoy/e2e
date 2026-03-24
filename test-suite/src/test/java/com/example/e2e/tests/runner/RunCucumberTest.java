package com.example.e2e.tests.runner;

import com.example.e2e.tests.demoapp.runner.DemoAppRunCucumberTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@Suite
@DisabledOnOs(OS.LINUX)
@SelectClasses({
        DemoAppRunCucumberTest.class
})
public class RunCucumberTest {
}
