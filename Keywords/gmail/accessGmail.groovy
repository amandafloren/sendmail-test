package gmail

import javax.mail.Flags
import javax.mail.Folder
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.NoSuchProviderException
import javax.mail.Session
import javax.mail.Store
import javax.mail.Message.RecipientType
import javax.mail.search.AndTerm
import javax.mail.search.RecipientStringTerm
import javax.mail.search.SearchTerm
import javax.mail.search.SubjectTerm

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import groovy.util.slurpersupport.Attributes
import internal.GlobalVariable

public class accessGmail {

	//******************************************
	//**    Mail Custom Keyword Definitions   **
	//******************************************


	@Keyword
	def findEmailAndClickURL(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject = "") {
		String url = findConfirmEmailURL(QAEmail, QAPassword, FolderPath, EmailRecipient, LinkKey, Subject)
		println "URL :" + url
		WebUI.openBrowser('')
		WebUI.navigateToUrl(url)
	}

	@Keyword
	def findEmailAndGetSubject(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject = "") {
		String rSubject = findConfirmEmailSubject(QAEmail, QAPassword, FolderPath, EmailRecipient, LinkKey, Subject)
		println "Subject :" + rSubject
		return rSubject
	}

	@Keyword
	def findEmailAndGetSender(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject = "") {
		String Sender = findConfirmEmailSender(QAEmail, QAPassword, FolderPath, EmailRecipient, LinkKey, Subject)
		println "Sender :" + Sender
		return Sender
	}

	@Keyword
	def ClearEmailFolder(String QAEmail, String QAPassword, String FolderPath) {
		String url = clearEmail(QAEmail, QAPassword, FolderPath)
		println "URL :" + url
	}


	//******************************************
	//**         Mail related methods         **
	//******************************************
	private String findConfirmEmailURL(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject) throws MessagingException
	{
		try {
			println(EmailRecipient)
			SearchTerm filter = createFilter(EmailRecipient, Subject);		// 1. Create mail search filter for our mailbox
			println(QAEmail + ", " + QAPassword)
			Store store = connect(QAEmail, QAPassword);				// 2. Connect to our mailbox
			String content = getMessageContent(store, FolderPath, filter);		// 3. Retrieve the message body from the received email
			println(content)
			String url = getURL(content, LinkKey);							// 4. Get the confirmation URL from the message body
			return url;
		} catch(Exception e) {
			println("Exception: ${e}")
		}
	}

	private String findConfirmEmailSubject(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject) throws MessagingException
	{
		try {
			println(EmailRecipient)
			SearchTerm filter = createFilter(EmailRecipient, Subject);		// 1. Create mail search filter for our mailbox
			println(QAEmail + ", " + QAPassword)
			Store store = connect(QAEmail, QAPassword);				// 2. Connect to our mailbox
			String content = getMessageSubject(store, FolderPath, filter);		// 3. Retrieve the message body from the received email
			println(content)
			return content;
		} catch(Exception e) {
			println("Exception: ${e}")
		}
	}

	private String findConfirmEmailSender(String QAEmail, String QAPassword, String FolderPath, String EmailRecipient, String LinkKey, String Subject) throws MessagingException
	{
		try {
			println(EmailRecipient)
			SearchTerm filter = createFilter(EmailRecipient, Subject);		// 1. Create mail search filter for our mailbox
			println(QAEmail + ", " + QAPassword)
			Store store = connect(QAEmail, QAPassword);				// 2. Connect to our mailbox
			String content = getMessageSender(store, FolderPath, filter);		// 3. Retrieve the message body from the received email
			println(content)
			return content;
		} catch(Exception e) {
			println("Exception: ${e}")
		}
	}

	private String clearEmail(String QAEmail, String QAPassword, String FolderPath) throws MessagingException
	{
		Store store
		try {
			println(QAEmail + ", " + QAPassword)
			store = connect(QAEmail, QAPassword);				// 2. Connect to our mailbox
			Folder emailFolder = store.getFolder(FolderPath);
			emailFolder.open(Folder.READ_WRITE);
			println("FolderPath: " + FolderPath)

			for (Message message in emailFolder.messages) {
				message.setFlag(Flags.Flag.DELETED, true);
			}
			emailFolder.close(true)
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			store.close();
		}
	}

	private SearchTerm createFilter(String Recipient, String Subject){
		SearchTerm t1 = new RecipientStringTerm(RecipientType.TO, Recipient);
		SearchTerm st
		if(Subject != "")
		{
			SearchTerm t2 = new SubjectTerm(Subject);
			st = new AndTerm(t1, t2);
		}
		else
		{
			st = new AndTerm(t1);
		}
		return st;
	}

	private Store connect(String QAEmail, String QAPassword) throws MessagingException
	{
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		Session session = Session.getDefaultInstance(props, null);
		Store store;

		try{
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", QAEmail, QAPassword);
		}catch (NoSuchProviderException e) {
			e.printStackTrace();
			throw e;
		}catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		}
		return store;
	}

	public String getMessageContent(Store store, String FolderPath, SearchTerm filter) throws MessagingException {
		String mailMessageContent = "";
		try{
			Folder emailFolder = store.getFolder(FolderPath);
			emailFolder.open(Folder.READ_ONLY);
			println("FolderPath: " + FolderPath)
			Message[] messages = emailFolder.search(filter);
			System.out.println("Matching Emails Found: " + messages.length);

			if(messages.length != 0){
				Message message = messages[0];
				String content = message.getContent().toString();
				mailMessageContent = content;
			}
			emailFolder.close(false);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			store.close();
		}
		return mailMessageContent;
	}

	public String getMessageSubject(Store store, String FolderPath, SearchTerm filter) throws MessagingException {
		String mailMessageSubject = "";
		try{
			Folder emailFolder = store.getFolder(FolderPath);
			emailFolder.open(Folder.READ_ONLY);
			println("FolderPath: " + FolderPath)
			Message[] messages = emailFolder.search(filter);
			System.out.println("Matching Emails Found: " + messages.length);

			if(messages.length != 0){
				Message message = messages[0];
				String subject = message.getSubject();
				String from = message.getFrom()[0].toString();
				String content = message.getContent().toString();
				mailMessageSubject = content;
			}
			emailFolder.close(false);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			store.close();
		}
		return mailMessageSubject;
	}

	public String getMessageSender(Store store, String FolderPath, SearchTerm filter) throws MessagingException {
		String mailMessageSender = "";
		try{
			Folder emailFolder = store.getFolder(FolderPath);
			emailFolder.open(Folder.READ_ONLY);
			println("FolderPath: " + FolderPath)
			Message[] messages = emailFolder.search(filter);
			System.out.println("Matching Emails Found: " + messages.length);

			if(messages.length != 0){
				Message message = messages[0];
				String Sender = message.getFrom()[0].toString();
				mailMessageSender = Sender;
			}
			emailFolder.close(false);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			store.close();
		}
		return mailMessageSender;
	}

	private String getURL(String content, String LinkKey){
		Document doc = Jsoup.parse(content);
		//org.jsoup.nodes.Attributes attributes = doc.getElementsByAttributeValueContaining("href", "ls/click?upn")[0].attributes()
		org.jsoup.nodes.Attributes attributes = doc.getElementsByAttributeValueContaining("href", LinkKey)[0].attributes()
		char[] href = attributes.get("href").value
		String URL = href.toString()
		return URL
	}
}