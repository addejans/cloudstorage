package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

//TODO: Remove pause(). These are only in because the page sometimes get stuck. It seems to always have to do w/ the result.html page.

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;

	private String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@AfterAll
	public static void afterAll() {
		driver.quit();
		driver = null;
	}

	@Test
	public void userSignupLoginLogoutTest() {

		String firstName = "Adam";
		String lastName = "DeJans";
		String username = "addejans";
		String password = "password";

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
		driver.get(baseURL + "/logout");
		assertEquals("You have been logged out", driver.findElement(By.id("logout-msg")).getText());
		driver.get(baseURL + "/home");
		assertEquals("Login",driver.getTitle()); //asserts that a call to /home keeps you on the Login.

	}

	@Test
	public void userSignupLoginTest() {
		String firstName = "Adam";
		String lastName = "DeJans";
		String username = "addejans";
		String password = "password";

		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(firstName, lastName, username, password);
		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

	@Test
	public void resultErrorTest(){
		userSignupLoginTest();
		driver.get(baseURL + "/result");
		ResultPage resultPage = new ResultPage(driver);
		assertEquals("Error", resultPage.getErrorMessage());
	}

	public void createNote(NotePage notePage, String title, String description) {
		notePage.openNoteTab();
		notePage.openModalJS();
		notePage.createNoteJS(title, description);
		notePage.saveNoteJS();
	}

	@Test
	public void createNoteTest() {
		userSignupLoginTest();
		NotePage notePage = new NotePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		createNote(notePage, "Note Title", "Note description.");
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("success-continue")));
		assertEquals("Result", driver.getTitle());
		assertEquals("Success", resultPage.getSuccessMessage());
		pause(3); //TODO: not sure why the timeout happens
		resultPage.successContinue();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("table-note-title")));
		assertEquals("Note Title", notePage.getTableNoteTitle());
	}

	@Test
	public void editNoteTest() {
		userSignupLoginTest();
		NotePage notePage = new NotePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		createNote(notePage, "Note Title", "Note description.");
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("success-continue")));
		pause(3); // TODO: why is this pause necessary? w/o the pause there will be a timeout on "editNoteButton"
		resultPage.successContinue();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("editNoteButton")));
		notePage.editNoteJS();
		notePage.createNoteJS("New Note Title", "New note description.");
		notePage.saveNoteJS();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("success-continue")));
		pause(3); // TODO: why is this pause necessary? w/o the pause there will be a timeout on "table-note-title"
		resultPage.successContinue();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("table-note-title")));
		assertEquals("New Note Title", notePage.getTableNoteTitle());
	}

	@Test
	public void deleteNoteTest() {
		userSignupLoginTest();
		NotePage notePage = new NotePage(driver);
		ResultPage resultPage = new ResultPage(driver);
		createNote(notePage, "Note Title", "Note description.");
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.id("success-continue")));
		pause(3); // TODO: why? (see all the other TODO's)
		resultPage.successContinue();
		pause(3);
		int numNotesBeforeDeletion = notePage.numNotes();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.id("deleteNoteButton")));
		notePage.deleteNoteJS();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.elementToBeClickable(By.id("success-continue")));
		pause(3); // TODO: why is this pause necessary? w/o the pause there will be a timeout on "notes-table-body"
		resultPage.successContinue();
		(new WebDriverWait(driver, 5)).until(ExpectedConditions.presenceOfElementLocated(By.id("notes-table-body")));
		assertEquals(numNotesBeforeDeletion - 1, notePage.numNotes());
	}

	public void createCredential(CredentialPage credentialPage, String url, String username, String password) {
		credentialPage.openCredentialModal();
		credentialPage.openCredentialTab();
		credentialPage.createCredential(url, username, password);
		credentialPage.saveCredentials();
	}

	@Test
	public void createCredentialTest() {
		userSignupLoginTest();
		CredentialPage credentialPage = new CredentialPage(driver);
		ResultPage resultPage = new ResultPage(driver);
		createCredential(credentialPage, "credential url", "credential username", "credential password");
		assertEquals("Result", driver.getTitle());
		assertEquals("Success", resultPage.getSuccessMessage());
		driver.get(baseURL + "/home");
		credentialPage.openCredentialTab();
		assertEquals("credential url", credentialPage.getCredentialUrl());
	}

	@Test
	public void editCredentialVerifyEncryptionVerifyPlainTextTest() {
		userSignupLoginTest();
		CredentialPage credentialPage = new CredentialPage(driver);
		ResultPage resultPage = new ResultPage(driver);
		String credentialPassword = "credential password";
		createCredential(credentialPage, "credential url", "credential username", credentialPassword);
		assertEquals("Result", driver.getTitle());
		driver.get(baseURL + "/home");
		credentialPage.openCredentialTab();
		assertNotEquals(credentialPassword, credentialPage.getCredentialPassword());
		credentialPage.editCredential();
		assertEquals(credentialPassword, credentialPage.getPlainTextPassword());
		credentialPage.createCredential("Edited credential url", "Edited credential username", "Edited credential password");
		credentialPage.saveCredentials();
		assertEquals("Result", driver.getTitle());
		assertEquals("Success", resultPage.getSuccessMessage());
		driver.get(baseURL + "/home");
		credentialPage.openCredentialTab();
		assertEquals("Edited credential url", credentialPage.getCredentialUrl());
	}

	@Test
	public void deleteCredentialTest() {
		userSignupLoginTest();
		CredentialPage credentialPage = new CredentialPage(driver);
		ResultPage resultPage = new ResultPage(driver);
		createCredential(credentialPage, "credential url", "credential username", "credential password");
		assertEquals("Result", driver.getTitle());
		assertEquals("Success", resultPage.getSuccessMessage());
		driver.get(baseURL + "/home");
		credentialPage.openCredentialTab();
		int numCredentialsBeforeDeletion = credentialPage.numCredentials();
		credentialPage.deleteCredential();
		assertEquals("Result", driver.getTitle());
		assertEquals("Success", resultPage.getSuccessMessage());
		driver.get(baseURL + "/home");
		credentialPage.openCredentialTab();
		assertEquals(numCredentialsBeforeDeletion - 1, credentialPage.numCredentials());

	}

	public void pause(){
		pause(1);
	}

	public void pause(int seconds){
		try {
			Thread.sleep(seconds*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
