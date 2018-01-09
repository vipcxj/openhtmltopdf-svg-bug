package com.bolan.test;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by vipcxj on 2018/1/9.
 */
public class PdfTest {

    @Test
    public void test() throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        org.jsoup.nodes.Document doc = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/pdf.html"), "UTF-8"), "/");
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml).syntax(Document.OutputSettings.Syntax.xml);
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        builder.useSVGDrawer(new BatikSVGDrawer());
        // builder.withHtmlContent(IOUtils.toString(getClass().getResourceAsStream("/svg.html"), "UTF-8"), "/");
        builder.withHtmlContent(doc.outerHtml(), "/");
        try (OutputStream fos = new FileOutputStream("/pdf-test.pdf")){
            builder.toStream(fos);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
