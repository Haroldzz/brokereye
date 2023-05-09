package life.freebao.devops.brokereye.page.circ;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrokerQueryResult extends AbstractBasePage {
    @FindBy(id = "practicecode")
    private List<WebDriver> results;

    public BrokerQueryResult(WebDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    @Override
    public boolean isDisplayed() {
        return wait.until(webDriver -> !results.isEmpty());
    }
    public Integer getResultCount(){
        return results.size();
    }
}
