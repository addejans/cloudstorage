package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class NotePage {

    @FindBy(css = "#nav-notes-tab")
    private WebElement notesTabField;

    @FindBy(css = "#addNoteButton")
    private WebElement addNote;

    @FindBy(css = "#note-title")
    private WebElement noteTitleField;

    @FindBy(css = "#table-note-title")
    private WebElement tableNoteTitle;

    @FindBy(css = "#notes-table-body")
    private WebElement notesTable;

    @FindBy(css = "#note-description")
    private WebElement noteDescriptionField;

    @FindBy(css = "#noteSubmit")
    private WebElement saveNote;

    @FindBy(css = "#editNoteButton")
    private WebElement editNote;

    @FindBy(css = "#deleteNoteButton")
    private WebElement deleteNote;

    private final JavascriptExecutor javascriptExecutor;

    private final WebDriverWait wait;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        wait = new WebDriverWait(webDriver, 1000);
        javascriptExecutor = (JavascriptExecutor) webDriver;
    }

    public void openNoteTab() {
        this.notesTabField.click();
    }

    /**
     * 2° Thread.sleep() [BAD PRACTICE]
     *
     * First and foremost, when we run a set of tests using Selenium, this set of tests ends up running at a very high
     * speed and thus many times the components present in our pages end up not being loaded, which makes interaction
     * with them impossible.
     *
     * This ends up affecting mainly two methods present in Selenium that are sendKeys() and click(), because without
     * the element having been loaded, these methods have no way to be triggered.
     *
     * Here it is worth to remember that when we run Selenium, independently of the result, it keeps running the whole
     * test set, so let's say that for example, you have two tests, one testing the Login page and the other the Sign-Up
     * page, following this same order, if an element of the Login page, as for example the field text referring to the
     * username wasn't loaded, trying, for example, to send the username data with sendKeys() will not work, because this
     * element will not be able to do that action, but Selenium will continue the set of tests and will preload the
     * Sign-Up page, but the tests will not be executed due to the error in the Login page and here usually when it is
     * tested if, for example, the test went to the correct page, it ends up showing the wrong page, because due to the
     * preloading it always loads the information of the next page and this makes some developers think that the problem
     * is in their code, but in fact, it is a problem that occurred in the previous page.
     *
     * Selenium usually has two solutions for loading elements that are Implicit and Explicit Wait, but they do not work
     * well with some dependencies that have elements written in Javascript and thus end up not identifying these elements
     * correctly and so even putting the WebDriverWait the error occurs in your code, because it does not identify these
     * elements from the dependencies you are using in your project.
     *
     * Thread.sleep() ends up working, because when you request a page using driver.get() and immediately after using
     * Thread.sleep(), it ends up causing the execution of your test to be paused and thus allows the page that was
     * requested through driver.get() can be loaded completely, thus allowing the remaining test actions to be executed
     * without any problem.
     *
     * However, Thread.sleep() is a bad practice, because this approach besides taking your application to states that
     * you can't control, leading your application to stop completely in some cases, it ends up increasing resources such
     * as CPU and Memory, causing your application to lose performance.
     *
     * 3° JavascriptExecutor [GOOD PRACTICE]
     *
     * JavascriptExecutor besides being a good practice ends up being the best option, because it allows us to fill in
     * the fields or click on the elements even if the page hasn't been fully loaded, by accessing the elements using the
     * DOM, not having any kind of problem with other dependencies and ensuring that your tests will run on any machine.
     */
    public void openModalJS() {
        javascriptExecutor.executeScript("arguments[0].click();", addNote);
    }

    public void createNoteJS(String title, String description) {
        javascriptExecutor.executeScript("arguments[0].value='" + title + "';", noteTitleField);
        javascriptExecutor.executeScript("arguments[0].value='" + description + "';", noteDescriptionField);
    }

    public void saveNoteJS() {
        javascriptExecutor.executeScript("arguments[0].click();", saveNote);
    }

    public void editNoteJS() {
        javascriptExecutor.executeScript("arguments[0].click();", editNote);
    }

    public void deleteNoteJS() {
        javascriptExecutor.executeScript("arguments[0].click();", deleteNote);
    }

    public String getTableNoteTitle() {
//        return tableNoteTitle.getText(); //TODO: This returns blank on 'createNoteTest()'. Why?
        return tableNoteTitle.getAttribute("innerHTML");
    }

    public int numNotes() {
        List<WebElement> notesList = notesTable.findElements(By.id("table-note-title"));
        return notesList.size();
    }
}