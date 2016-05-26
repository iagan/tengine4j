package org.ithinking.tengine;

import org.ithinking.tengine.core.Resource;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.ithinking.tengine.sql.SQLParser;
import org.junit.Test;

public class SqlTengineTest {

	@Test
	public void test() {
		SQLParser parser = new SQLParser();
		ClasspathLoader cl = new ClasspathLoader(null);
		Resource res = cl.load("user.sql");
		parser.parse(res.getText());
	}
}
