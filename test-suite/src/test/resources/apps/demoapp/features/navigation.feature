Feature: Demo application navigation behaviors
  In order to validate the demo application inside its own area
  As an e2e automation engineer
  I want navigation coverage to live with the demoapp-specific test assets

  @demoapp @smoke
  Scenario: Open the Playwright home page
    Given the user opens the demoapp homepage
    Then the page title should contain "Playwright"
