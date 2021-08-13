package com.payconiq.stock;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.platform.engine.Cucumber;


/**
 * @author Saeid Noroozi
 * Define cucumber configuration to define path of feature files and
 * customuze execution of cucumber
 */
@Cucumber
@CucumberOptions(features = "src/test/com/payconiq/stock",
        plugin = {"pretty", "json:target/reports/json/calculator.json"},
        snippets = CucumberOptions.SnippetType.UNDERSCORE,
        monochrome = true
)
public class CucumberIntegrationTest extends SpringIntegrationTest {
}
