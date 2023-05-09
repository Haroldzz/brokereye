package life.freebao.devops.brokereye.page.baidu;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchComponent extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(SearchComponent.class);
    @FindBy(id = "kw")
    private WebElement searchBox;
    @FindBy(id = "su")
    private List<WebElement> searchButton;

    public SearchComponent(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void search(String keyword){
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.TAB);
        searchButton.stream()
                .filter(webElement -> webElement.isDisplayed() && webElement.isEnabled())
                .findFirst()
                .ifPresent(WebElement::click);
        log.info("Search \"{}\"", keyword);
    }
    @Override
    public boolean isDisplayed(){
        return wait.until(webDriver -> searchBox.isDisplayed());
    }

}
