package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(css = "#success-message")
    private WebElement successMessage;

    @FindBy(css = "#error-message")
    private WebElement errorMessage;

    @FindBy(css = "#success-continue")
    private WebElement successContinue;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public String getSuccessMessage() {
        return successMessage.getText();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public void successContinue(){
        this.successContinue.click();
    }

}