package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jsoup.dto.TestDTO;
import com.nixmash.springdata.jsoup.parsers.JSoupHtmlParser;
import com.nixmash.springdata.jsoup.utils.JsoupTestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class JsoupTests extends JsoupContext {

    private Document doc;
    private TestDTO testDTO;

    @Autowired
    @Qualifier("testDTOParser")
    JSoupHtmlParser<TestDTO> testDTOParser;

    @Before
    public void setup() throws IOException {
        File in = JsoupTestUtil.getFile("/html/testdto.html");
        doc = Jsoup.parse(in, null, "http://example.com");
        testDTO = testDTOParser.parse(doc.outerHtml());
    }

    @Test
    public void extractTitleFromJsoupDemoHTML() {
        String title = doc.title();
        assertThat(title, is("This is my Title"));
    }

    //  region TestDTO Annotation Tests

    /**
     * HTML:
     *
     * <div id="myid">This is my id text</div>
     * <div class="myclass" myattr="grouchy"><span>This is my class text</span></div>
     *
     */

    @Test
    public void parseMetaTagByProperty() {
        assertEquals("http://facebook.image", testDTO.getFacebookImage());
    }

    @Test
    public void parseMetaTagByName() {
        assertEquals("http://twitter.image", testDTO.getTwitterImage());
    }

    @Test
    public void parseMyClassAttribute() {
        assertEquals("grouchy", testDTO.getMyClassAttribute());
    }

    @Test
    public void parseMyClassText() {
        assertEquals("This is my class text", testDTO.getMyClassText());
    }

    @Test
    public void parseMyClassHtml() {
        assertEquals("<span>This is my class text</span>", testDTO.getMyClassHtml());
    }

    @Test
    public void parseIdText() {
        assertEquals("This is my id text", testDTO.getMyIdText());
    }

    // endregion

}
