package com.bolan.test;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by vipcxj on 2018/1/9.
 */
public class PdfTest {

    @Test
    public void testSvg() throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        org.jsoup.nodes.Document doc = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/pdf.html"), "UTF-8"), "/");
        doc.outputSettings().escapeMode(Entities.EscapeMode.xhtml).syntax(Document.OutputSettings.Syntax.xml);
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        builder.useSVGDrawer(new BatikSVGDrawer());
        // builder.withHtmlContent(IOUtils.toString(getClass().getResourceAsStream("/svg.html"), "UTF-8"), "/");
        builder.withW3cDocument(new W3CDom().fromJsoup(doc), "/");
        try (OutputStream fos = new FileOutputStream("pdf-test-svg.pdf")){
            builder.toStream(fos);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFonts() throws IOException {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/FangSong.ttf");
            }
        }, "FangSong");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/Wingdings2.ttf");
            }
        }, "Wingdings 2");
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        String html = IOUtils.toString(getClass().getResourceAsStream("/fonts.html"), "UTF-8");
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
        builder.withW3cDocument(new W3CDom().fromJsoup(doc), "/");
        try (FileOutputStream fos = new FileOutputStream("pdf-test-fonts.pdf")){
            builder.toStream(fos);
            builder.run();
        }
    }
}
