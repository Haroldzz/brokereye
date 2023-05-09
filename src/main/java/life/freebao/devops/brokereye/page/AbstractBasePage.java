package life.freebao.devops.brokereye.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.PostConstruct;

public abstract class AbstractBasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public AbstractBasePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    @PostConstruct
    public void init(){
        PageFactory.initElements(this.driver,this);
    }
    public abstract boolean isDisplayed();
}
