package org.ithinking.tengine;

import org.ithinking.tengine.core.Resource;
import org.ithinking.tengine.excel.ExcelBuilder;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * ${TITLE}
 *
 * @author agan
 * @date 2016-10-02
 */
public class ExcelTest {
    @Test
    public void test() {
        try {
            ExcelBuilder excelBuilder = new ExcelBuilder();
            File file = new File("D:\\tmp\\test.xls");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            //
            ClasspathLoader cl = new ClasspathLoader(null, null);
            Resource res = cl.load("/templates/excel.xml");
            String tplText = res.getText();
            excelBuilder.builder(fos, tplText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
