package life.freebao.devops.brokereye.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!grid")
public class WebDriverConfiguration {
    @Value("${application.webDriver.browser.options}")
    private String[] browserOptions;
    @Bean
    public WebDriver edgeDriver(){
        WebDriverManager.edgedriver().setup();
        WebDriver driver = new EdgeDriver();
        return new Augmenter().augment(driver);
    }
    @Bean
    public WebDriver firefoxDriver(){
        WebDriverManager.firefoxdriver().setup();
        WebDriver driver = new FirefoxDriver();
        return new Augmenter().augment(driver);
    }

    @Bean
    public WebDriver chromeDriver(){
        ChromeOptions chromeOptions = new ChromeOptions();
        for (String option : browserOptions) {
            option = option.contains("/") ? option.replace('/', ',') : option;
            chromeOptions.addArguments(option);
        }
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(chromeOptions);
        return new Augmenter().augment(driver);
        }
}
