package life.freebao.devops.brokereye.config;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.ImmutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URL;

@Configuration
@Profile("grid")
public class RemoteWebDriverConfiguration {
    @Value("${application.webdriver.selenium.grid.url}")
    private String url;
    @Value("${application.webdriver.browser.options}")
    private String[] browserOptions;

    @Bean
    @ConditionalOnProperty(name = "application.webdriver.browser.name", havingValue = "firefox")
    public WebDriver remoteFirefoxDriver() throws Exception {
        URL gridUrl = new URL(url);
        Capabilities capabilities = new ImmutableCapabilities("browserName", "firefox");
        WebDriver driver = new RemoteWebDriver(gridUrl, capabilities);
        return new Augmenter().augment(driver);
    }
    @Bean
    @ConditionalOnProperty(name = "application.webdriver.browser.name", havingValue = "MicrosoftEdge")
    public WebDriver remoteEdgeDriver() throws Exception {
        URL gridUrl = new URL(url);
        Capabilities capabilities = new ImmutableCapabilities("browserName", "MicrosoftEdge");
        WebDriver driver = new RemoteWebDriver(gridUrl, capabilities);
        return new Augmenter().augment(driver);
    }
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "application.webdriver.browser.name", havingValue = "chrome")
    public WebDriver remoteChromeDriver() throws Exception {
        ChromeOptions chromeOptions = new ChromeOptions();
        for (String option : browserOptions) {
            option = option.contains("/") ? option.replace('/', ',') : option;
            chromeOptions.addArguments(option);
        }
        URL gridUrl = new URL(url);
        WebDriver driver = new RemoteWebDriver(gridUrl, chromeOptions);
        return new Augmenter().augment(driver);
    }
}
