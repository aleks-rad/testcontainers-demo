package ru.aleksrad.testcontainersdemo.ui;

import com.codeborne.selenide.WebDriverRunner;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import ru.aleksrad.testcontainersdemo.config.TestConfig;

import java.io.File;

@SpringBootTest
@ContextConfiguration(classes = {TestConfig.class}, initializers = {BaseUITests.Initializer.class})
public abstract class BaseUITests {

    @Autowired
    protected ObjectMapper objectMapper;

    protected static BrowserWebDriverContainer<?> browserContainer;
    protected static MockServerContainer mockServerContainer;

    static {
        Testcontainers.exposeHostPorts(8080);

        browserContainer = new BrowserWebDriverContainer<>()
                .withCapabilities(new ChromeOptions())
                .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("target"));
        browserContainer.withAccessToHost(true);
        browserContainer.start();
        WebDriverRunner.setWebDriver(browserContainer.getWebDriver());

        mockServerContainer = new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.14.0"));
        mockServerContainer.withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(49153), new ExposedPort(1080)))
        ));
        mockServerContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

        }
    }

}
