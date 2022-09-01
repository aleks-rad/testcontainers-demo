package ru.aleksrad.testcontainersdemo.ui.pageobject;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MainPage {

    public final SelenideElement cardNumberField = $(By.id("cardNumberField"));
    public final SelenideElement sendBtn = $(By.id("sendBtn"));
    public final SelenideElement bankNameLabel = $(By.id("bankNameLabel"));
    public final SelenideElement bankCodeLabel = $(By.id("bankCodeLabel"));
    public final SelenideElement errorLabel = $(By.id("errorLabel"));

    public static MainPage open() {
        return Selenide.open("http://host.testcontainers.internal:8080", MainPage.class);
    }

}
