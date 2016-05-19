package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.mail.service.JsoupService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupTests extends JsoupContext {

	@Autowired
	private JsoupService jsoupService;

	private Document doc;
	private String html;

	@Before
	public void setup() {
		html = jsoupService.getDemoJsoupHTML();
		doc = Jsoup.parse(html);
	}

	@Test
	public void canRetrieveJsoupDemoHTML() throws MessagingException, IOException {
		assertThat(html, containsString("<html>"));
	}

	@Test
	public void extractTitleFromJsoupDemoHTML() {
		String title = doc.title();
		assertThat(title, is("This is my Title"));
	}
}
