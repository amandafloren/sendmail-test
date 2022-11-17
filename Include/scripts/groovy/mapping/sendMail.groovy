package mapping
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When


class sendMail {
	/**
	 * The step definitions below match with Katalon sample Gherkin steps
	 */
	@Given("I want to send email: email(.*), password(.*), recipient(.*), subject(.*), body(.*)")
	def access_to_gmail(String email, password, recipient, subject, body){
		WebUI.callTestCase(findTestCase("Test Cases/Open Gmail/Send mail"),[('email') : email, ('password') : password, ('recipient') : recipient, ('subject') : subject, ('body') : body], FailureHandling.STOP_ON_FAILURE)
	}

	@Then("I receive the auto reply mail: email(.*), password(.*), path(.*), recipient(.*), key(.*)")
	def receive_auto_mail(String email, password, path, recipient, key) {
		WebUI.callTestCase(findTestCase("Test Cases/Open Gmail/Validate mail reply"),[('email') : email, ('password') : password, ('path') : path, ('recipient') : recipient, ('key') : key], FailureHandling.STOP_ON_FAILURE)
	}
}