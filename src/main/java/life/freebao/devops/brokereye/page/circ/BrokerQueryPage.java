package life.freebao.devops.brokereye.page.circ;

import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v110.network.Network;
import org.openqa.selenium.devtools.v110.network.model.RequestId;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@Component
public class BrokerQueryPage extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(BrokerQueryPage.class);
    private final BrokerQueryComponent brokerQueryComponent;
    private final BrokerQueryResult brokerQueryResult;
    @Value("${application.webdriver.circ.query.url:http://127.0.0.1}")
    private String queryUrl;
    @Value("${application.webdriver.circ.ocr.url:http://127.0.0.1}")
    private String ocrUrl;
    public BrokerQueryPage(WebDriver driver, WebDriverWait wait, BrokerQueryComponent brokerQueryComponent, BrokerQueryResult brokerQueryResult) {
        super(driver, wait);
        this.brokerQueryComponent = brokerQueryComponent;
        this.brokerQueryResult = brokerQueryResult;
    }

    public BrokerQueryComponent getBrokerQueryComment() {
        return brokerQueryComponent;
    }

    public BrokerQueryResult getBrokerQueryResult() {
        return brokerQueryResult;
    }
    @Override
    public boolean isDisplayed() {
        return brokerQueryComponent.isDisplayed();
    }
    public HashMap<String, String> getBrokerQueryPage(String name, String idNumber) throws IOException, InterruptedException {
        // initial devtools
        DevTools devTools = ((HasDevTools) driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(),Optional.empty(),Optional.empty()));
        // go to query url
        log.info("Go to url: {}",queryUrl);
        driver.get(queryUrl);
        // get captcha and send query request
        HashMap<String, String> responseMap = new HashMap<>();
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            try {
                String responseUrl = responseReceived.getResponse().getUrl();
                RequestId requestId = responseReceived.getRequestId();
                if (responseUrl.contains("insurance.do")) {
                    String responseBody = devTools.send(Network.getResponseBody(requestId)).getBody();
                    responseMap.put(responseUrl,responseBody);
                    log.info("BrokerQueryPage responseReceived: {}", responseMap);
                }
            } catch (Exception e) {
                log.info("Incorrect response: " + responseReceived.getResponse().getUrl());
            }
        });

        if (isDisplayed()) {
            String captchaImageEncode = brokerQueryComponent.getCaptchaImageEncode();
            String captchaImageDigest = brokerQueryComponent.saveCaptchaImageFile(captchaImageEncode);
            String captchaImageOcr = brokerQueryComponent.getCaptchaImageOcr(captchaImageEncode, ocrUrl);
            brokerQueryComponent.getBrokerInfo(name, idNumber, captchaImageOcr);
            responseMap.put("captchaImageDigest",captchaImageDigest);
            responseMap.put("captchaImageOcr",captchaImageOcr);
            log.info("Query response is: {}",responseMap);
        }

        return responseMap;
    }
}



