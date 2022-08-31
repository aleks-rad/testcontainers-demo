package ru.aleksrad.testcontainersdemo.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.MediaType;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import ru.aleksrad.testcontainersdemo.integration.dto.BankInfoDto;
import ru.aleksrad.testcontainersdemo.ui.pageobject.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
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
        screenshot("open-page-test/check-page");
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
                        .withQueryStringParameter("cardNumber", cardNumber))
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(
                                objectMapper.writeValueAsString(
                                        BankInfoDto.builder()
                                                .name(bankInfoName)
                                                .code(bankInfoCode)
                                                .build()
                                )
                        ));

        screenshot("bank-info-should-found-test/1-before-input");
        mainPage.cardNumberField.setValue(cardNumber);
        screenshot("bank-info-should-found-test/2-after-input");
        mainPage.sendBtn.click();
        mainPage.bankNameLabel.shouldHave(text(bankInfoName));
        mainPage.bankCodeLabel.shouldHave(text(bankInfoCode));
        screenshot("bank-info-should-found-test/3-after-click");
    }

}
