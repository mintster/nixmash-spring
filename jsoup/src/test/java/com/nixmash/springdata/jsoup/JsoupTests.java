package com.nixmash.springdata.jsoup;

import com.nixmash.springdata.jsoup.base.JsoupImage;
import com.nixmash.springdata.jsoup.base.JsoupLink;
import com.nixmash.springdata.jsoup.dto.TestDTO;
import com.nixmash.springdata.jsoup.base.JSoupHtmlParser;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        testDTO = testDTOParser.parse(doc);
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
        assertEquals("one, two, three", testDTO.getMetaKeywords());
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

    @Test
    public void parseDescriptionWhichShouldBeNull() {
        assertNull(testDTO.getDescription());
    }

    @Test
    public void parseImages() {

        // # of images in content area one less than full testdto.html page

        assertEquals(testDTO.getTestImagesInContentArea().size(), 2);
        assertEquals(testDTO.getTestImagesInPage().size(), 3);

        // attributes of first image in List<JsoupImage>

        JsoupImage img = testDTO.getTestImagesInContentArea().get(0);
        assertEquals("http://example.com/one.png", img.getSrc());
        assertEquals("one", img.getAlt());
        assertTrue(img.getHeight().equals(40));

        // empty image height-width attributes are null

        assertTrue(testDTO.getTestImagesInContentArea().get(1).getHeight() == null);

        // retrieving image with class=myimage

        assertEquals(testDTO.getTestImage().getSrc(),"http://example.com/two.png");

    }

    @Test
    public void parseLinks() {

        // # of links in content area one less than full testdto.html page

        assertEquals(testDTO.getTestLinksInContentArea().size(), 2);
        assertEquals(testDTO.getTestLinksInPage().size(), 3);

        // attributes of first link in List<JsoupLink>

        JsoupLink link = testDTO.getTestLinksInContentArea().get(0);
        assertEquals("http://example.com/one.html", link.getHref());
        assertEquals("First Link", link.getText());

        // retrieving link with class=mylink

        assertEquals(testDTO.getTestLink().getHref(),"http://example.com/two.html");
        
    }
        // endregion

}
