package ru.aleksrad.testcontainersdemo.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.matchers.Times;
import org.mockserver.model.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import ru.aleksrad.testcontainersdemo.integration.dto.BankInfoDto;
import ru.aleksrad.testcontainersdemo.ui.pageobject.MainPage;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.title;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class WebUITests extends BaseUITests {

    private MainPage mainPage;

    @BeforeEach
    public void beforeEach() {
        mainPage = MainPage.open();
    }

    @Test
    public void openPageTest() {
        assertThat(title())
                .as("Проверяем заголовок страницы")
                .isEqualTo("testcontainers-demo");
    }

    @Test
    public void bankInfoShouldFoundTest() throws JsonProcessingException {
        final String cardNumber = "4276000000000001";
        final String bankInfoName = "Сбербанк";
        final String bankInfoCode = "Sber";

        new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort())
                .when(request()
                                .withMethod("GET")
                                .withPath("/bank-info")
                                .withQueryStringParameter("cardNumber", cardNumber),
                        Times.once())
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(
                                objectMapper.writeValueAsString(
                                        BankInfoDto.builder()
                                                .name(bankInfoName)
                                                .code(bankInfoCode)
                                                .build()
                                ))
                        .withDelay(TimeUnit.SECONDS, 5)
                );

        mainPage.cardNumberField.setValue(cardNumber);
        mainPage.sendBtn.click();
        mainPage.bankNameLabel.shouldHave(text(bankInfoName));
        mainPage.bankCodeLabel.shouldHave(text(bankInfoCode));
    }

    @Test
    public void bankInfoNotFoundTest() throws JsonProcessingException {
        final String cardNumber = "4276000000000002";

        new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort())
                .when(request()
                                .withMethod("GET")
                                .withPath("/bank-info")
                                .withQueryStringParameter("cardNumber", cardNumber),
                        Times.once())
                .respond(response()
                        .withStatusCode(404));

        mainPage.cardNumberField.setValue(cardNumber);
        mainPage.sendBtn.click();
        mainPage.errorLabel.shouldHave(text("404 Not Found"));
    }

}
