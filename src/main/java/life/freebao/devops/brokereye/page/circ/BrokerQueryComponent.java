package life.freebao.devops.brokereye.page.circ;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import life.freebao.devops.brokereye.page.AbstractBasePage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class BrokerQueryComponent extends AbstractBasePage {
    private static final Logger log = LoggerFactory.getLogger(BrokerQueryComponent.class);
    @FindBy(id = "name")
    private WebElement nameBox;
    @FindBy(id = "cardno")
    private WebElement idNumberBox;
    @FindBy(id = "yzm")
    private WebElement captchaBox;
    @FindBy(id = "captcha")
    private WebElement captchaImage;
    @FindBy(xpath = "/html/body/div[2]/div/form/div[6]/button[1]")
    private WebElement submitButton;
    @Value("${application.webdriver.screenshot.path:/tmp/}")
    private String captchaImagePath;
    private final HttpClient httpClient;

    public BrokerQueryComponent(WebDriver driver, WebDriverWait wait, HttpClient httpClient) {
        super(driver, wait);
        this.httpClient = httpClient;
    }

    public String getCaptchaImageEncode(){
        return captchaImage.getScreenshotAs(OutputType.BASE64);
    }

    public String saveCaptchaImageFile(String captchaImageEncode){
        byte[] captchaImageDecode = Base64.getDecoder().decode(captchaImageEncode);
        String captchaImageDigest = DigestUtils.md5DigestAsHex(captchaImageDecode);
        String path = captchaImagePath.endsWith("/") ? captchaImagePath : (captchaImagePath + '/');
        String filePath = path + captchaImageDigest + ".png";
        try {
            new FileSystemResource(filePath).getOutputStream().write(captchaImageDecode);
        } catch (IOException e) {
            log.info("{} saved failed with message: {}",filePath,e.getMessage());
        }
        return captchaImageDigest;
    }

    public String getCaptchaImageOcr(String captchaImageEncode, String ocrUrl) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(ocrUrl))
                .POST(HttpRequest.BodyPublishers.ofString(captchaImageEncode))
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        Map<String, String> responseBodyMap = new ObjectMapper().readValue(httpResponse.body(), new TypeReference<Map<String, String>>(){});
        String captchaImageOcr = responseBodyMap.get("result");
        log.info("Captcha is: {}",captchaImageOcr);
        return captchaImageOcr;
    }

    public void getBrokerInfo(String name, String idNumber, String captcha) throws InterruptedException {
        nameBox.sendKeys(name);
        idNumberBox.sendKeys(idNumber);
        captchaBox.sendKeys(captcha);
        log.info("Summit broker request: {}, {}, {}",name,idNumber,captcha);
        TimeUnit.MILLISECONDS.sleep(1000L);
        submitButton.click();
    }
    @Override
    public boolean isDisplayed(){
        return wait.until(webDriver -> submitButton.isDisplayed() && captchaImage.isDisplayed());
    }

}
