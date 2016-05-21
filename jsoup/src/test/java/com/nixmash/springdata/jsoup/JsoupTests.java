package com.nixmash.springdata.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupTests extends JsoupContext {

	private Document doc;
	private String html;

	@Before
	public void setup() throws IOException {
		File in = JsoupTestUtil.getFile("/html/jsoup.html");
		doc = Jsoup.parse(in, null, "http://example.com");
	}

	@Test
	public void readResourceHtmlFile() throws IOException {
		System.out.println(doc.toString());
	}

	@Test
	public void extractTitleFromJsoupDemoHTML() {
		String title = doc.title();
		assertThat(title, is("This is my Title"));
	}
}
