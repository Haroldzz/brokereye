package life.freebao.devops.brokereye.page.baidu;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchResult extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(SearchResult.class);

    @FindBy(id = "content_left")
    private List<WebElement> results;

    public Integer getResultCount(){
        return results.size();
    }

    public SearchResult(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public boolean isDisplayed() {
        return wait.until(webDriver -> !results.isEmpty());
    }
}

