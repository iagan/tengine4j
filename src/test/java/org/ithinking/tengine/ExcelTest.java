package org.ithinking.tengine;

import org.ithinking.tengine.core.Resource;
import org.ithinking.tengine.excel.ExcelContext;
import org.ithinking.tengine.excel.ExcelParser;
import org.ithinking.tengine.excel.WorkbookDef;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
            //
            ClasspathLoader cl = new ClasspathLoader(null, null);
            Resource res = cl.load("/templates/excel.xml");
            String tplText = res.getText();





            ExcelParser reader = new ExcelParser();
            WorkbookDef workbookDef = reader.parse(tplText);
            ExcelContext context = new ExcelContext(null);


            List<User> userList = new ArrayList<>();
            userList.add(new User("张三", 12));
            userList.add(new User("李四", 20));
            userList.add(new User("王五", 90));
            //
            context.add("owner", "阿甘");
            context.add("date", "2016-01-02");

            context.add("userList", userList);

            String wName = workbookDef.getName(context, "test.xls");


            File file = new File("D:\\tmp\\" + wName);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);

            //
            context.setOs(fos);
            workbookDef.create(context);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class User {
        private String name;
        private Integer age;

        public User(){

        }

        public User(String name, Integer age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }
}
