package life.freebao.devops.brokereye.service;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import life.freebao.devops.brokereye.page.baidu.SearchPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class BaiduSearchService extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(BaiduSearchService.class);
    private final SearchPage searchPage;

    public BaiduSearchService(WebDriver driver, WebDriverWait wait, SearchPage searchPage) {
        super(driver, wait);
        this.searchPage = searchPage;
    }
    @Override
    public boolean isDisplayed() {
        return searchPage.getSearchComponent().isDisplayed();
    }
    @Scheduled(cron = "${application.webdriver.baidu.search.cron:-}")
    public void baiduSearchPageTask(){
        log.debug("Search task starts in session: "+ searchPage.getSessionId());
        searchPage.getSearchPage();
        Assert.isTrue(isDisplayed(),"Search box is not displayed");
        searchPage.searchKey("time");
        log.info("Search task ends in session: "+searchPage.getSessionId());
        searchPage.closeSession();
    }

}
