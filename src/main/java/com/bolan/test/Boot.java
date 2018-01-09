package com.bolan.test;

import com.openhtmltopdf.extend.FSSupplier;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.svgsupport.BatikSVGDrawer;
import org.apache.commons.io.IOUtils;
import org.jsoup.parser.ParseSettings;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.*;

/**
 * Created by vipcxj on 2018/1/2.
 */
@Singleton
@Startup
public class Boot {

    @PostConstruct
    public void init() {
        PdfRendererBuilder builder = new PdfRendererBuilder();
        org.jsoup.parser.Parser parser = org.jsoup.parser.Parser.htmlParser();
        parser.settings(new ParseSettings(true, true));
        org.jsoup.nodes.Document doc = null;
        try {
            doc = parser.parseInput(IOUtils.toString(getClass().getResourceAsStream("/svg.html"), "UTF-8"), "/");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
/*        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/FangSong.ttf");
            }
        }, "FangSong");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/KaiTi.ttf");
            }
        }, "KaiTi");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/LiSu.ttf");
            }
        }, "LiSu");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/SimSun.ttf");
            }
        }, "SimSun");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/SimHei.ttf");
            }
        }, "SimHei");
        builder.useFont(new FSSupplier<InputStream>() {
            @Override
            public InputStream supply() {
                return getClass().getResourceAsStream("/fonts/XingSu.ttf");
            }
        }, "XingSu");*/
        builder.defaultTextDirection(PdfRendererBuilder.TextDirection.LTR);
        builder.useSVGDrawer(new BatikSVGDrawer());
        // builder.withHtmlContent(IOUtils.toString(getClass().getResourceAsStream("/svg.html"), "UTF-8"), "/");
        builder.withHtmlContent(doc.outerHtml(), "/");
        try (OutputStream fos = new ByteArrayOutputStream(1024 * 1024)){
            builder.toStream(fos);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
