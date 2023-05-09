package life.freebao.devops.brokereye.page.baidu;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SearchPage extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(SearchPage.class);
    private final SearchComponent searchComponent;
    private final SearchResult searchResult;
    @Value("${application.webdriver.baidu.search.url}")
    private String url;

    public SearchPage(WebDriver driver, WebDriverWait wait, SearchComponent searchComponent, SearchResult searchResult) {
        super(driver, wait);
        this.searchComponent = searchComponent;
        this.searchResult = searchResult;
    }

    public SearchComponent getSearchComponent() {
        return searchComponent;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void getSearchPage(){
        log.debug("Go to {}", url);
        driver.get(url);
    }

    public void searchKey(String key){
        searchComponent.search(key);
    }

    @Override
    public boolean isDisplayed() {
        return searchComponent.isDisplayed();
    }

    public SessionId getSessionId(){
        return ((RemoteWebDriver) driver).getSessionId();
    }
    public void closeSession(){
        if (driver.getWindowHandles().size()>1) {
            driver.close();
        }
    }

}
