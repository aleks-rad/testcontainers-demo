package ru.aleksrad.testcontainersdemo.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ru.aleksrad.testcontainersdemo.config.TestConfig;

import java.io.File;

import static java.util.Objects.nonNull;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class})
public abstract class BaseUITests {

    @Autowired
    protected ObjectMapper objectMapper;

    private static final int MOCK_SERVER_PORT = 49153;

    private static final String MOCK_SERVER_IMAGE_NAME = "mockserver/mockserver:5.14.0";

    protected static BrowserWebDriverContainer<?> browserContainer;
    protected static MockServerContainer mockServerContainer;

    static {
        Testcontainers.exposeHostPorts(8080);

        browserContainer = new BrowserWebDriverContainer<>()
                .withCapabilities(new ChromeOptions())
                //todo запись видео не работает
                .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));
        browserContainer.withAccessToHost(true);
        browserContainer.start();
        WebDriverRunner.setWebDriver(browserContainer.getWebDriver());
        Configuration.timeout = 7000;
        Configuration.pageLoadTimeout = 5000;

        mockServerContainer = new MockServerContainer(DockerImageName.parse(MOCK_SERVER_IMAGE_NAME));
        mockServerContainer.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(MOCK_SERVER_PORT), new ExposedPort(1080)))
        ));
        mockServerContainer.start();

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
    }

    @AfterAll
    public static void cleanUp() {
        if (nonNull(browserContainer)) {
            browserContainer.stop();
        }

        if (nonNull(mockServerContainer)) {
            mockServerContainer.stop();
        }
    }

}
