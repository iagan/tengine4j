package org.ithinking.tengine.sql;

import org.ithinking.tengine.core.Resource;
import org.ithinking.tengine.loader.ClasspathLoader;

public class SQLParser {
	
	public static void main(String[] args){
		ClasspathLoader cl = new ClasspathLoader("","");
		Resource res = cl.load("user.sql");
		SQLParser p = new SQLParser();
		p.parse(res.getText());
	}

	public void parse(String text){
		SQLLexer lexer = new SQLLexer(text);
		while(lexer.hasNext()){
			System.out.println(lexer.next());
		}
	}
}
