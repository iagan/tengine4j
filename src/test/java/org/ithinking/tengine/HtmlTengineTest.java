package org.ithinking.tengine;

import java.util.ArrayList;
import java.util.List;

import org.ithinking.tengine.core.Configuration;
import org.ithinking.tengine.core.Context;
import org.ithinking.tengine.core.Loader;
import org.ithinking.tengine.core.Template;
import org.ithinking.tengine.core.TemplateManager;
import org.ithinking.tengine.html.parser.HtmlParser;
import org.ithinking.tengine.loader.ClasspathLoader;
import org.junit.Test;

public class HtmlTengineTest {
	// SQL解析类库 SQLJEP
	// StringTokenizer
	/**
	 * bug: 1. 字符区域写上 "或'字符时，会出现解析错误
	 */
	@Test
	public void test() {
		Configuration conf = Configuration.newConfiguration();
		Loader loader = new ClasspathLoader(conf.getViewPrefix(),conf.getViewCharset());
		HtmlParser parser = new HtmlParser();
		//
		User admin = new User("admin", 10, "admin@126.com", "甘肃兰州");

		List<User> userList = new ArrayList<User>();
		userList.add(new User("阿甘", 10, "vajava@126.com", "海南海口"));
		userList.add(new User("李四", 20, "lisi@qq.com", "美国"));
		userList.add(new User("王五", 15, "wangwu@163.com", "中国北京"));
		userList.add(new User("赵六", 30, "zhaoliu@xin.com", "广东广州"));
		userList.add(new User("小明", 30, "xiaoming@xin.com", "广东广州"));
		userList.add(new User("路人甲", 40, "lurenjia@xin.com", "广东广州"));

		// Context context
		TemplateManager manager = new TemplateManager(loader, conf, parser);
		Context context = new DefContext(manager);
		Template template = context.loadTemplate("index.html");
		context.add("title", "this is test").add("queryUrl", "http://localhost").add("userList", userList)
				.add("admin", admin).add("showList", true);

		template.render(context);
	}

	public static class User {
		private Long id;
		private String name;
		private Integer age;
		private String address;
		private String email;
		private boolean single;

		public User() {
			this("test", 25, "1233@qq.com", "广东省深圳市白石洲");
		}

		public User(String name, int age, String email, String address) {
			this.id = Math.round(Math.random() * 1000);
			this.name = name;
			this.age = age;
			this.email = email;
			this.address = address;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
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

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public boolean isSingle() {
			return single;
		}

		public void setSingle(boolean single) {
			this.single = single;
		}
	}
}
