package org.ithinking.tengine;

import org.apache.commons.io.FileUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeIterator;
import org.junit.Test;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by test on 2016/7/7.
 */
public class HtmlParserTest {
    private static String ENCODE = "utf-8";

    @Test
    public void testValidatorNu() {

        try {

            File file = new File("D:\\Projects\\banana\\tengine4j\\src\\test\\resources\\templates\\index.html");
            String html = FileUtils.readFileToString(file, "UTF-8");

            Parser parser = Parser.createParser(html, "utf-8");

            //Parser parser = new Parser((HttpURLConnection) (new URL("https://www.baidu.com")).openConnection());

            for (NodeIterator i = parser.elements(); i.hasMoreNodes(); ) {
                Node node = i.nextNode();

               // node.get

                message("getText:" + node.getText());
                message("getPlainText:" + node.toPlainTextString());
                message("toHtml:" + node.toHtml());
                message("toHtml(true):" + node.toHtml(true));
                message("toHtml(false):" + node.toHtml(false));
                message("toString:" + node.toString());
                message("=================================================");
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }

    private static void message(String szMsg) {
        try {
            System.out.println(new String(szMsg.getBytes(ENCODE), System.getProperty("file.encoding")));
        } catch (Exception e) {
        }
    }
}
