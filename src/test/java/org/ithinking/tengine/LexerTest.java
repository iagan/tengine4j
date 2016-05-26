package org.ithinking.tengine;

import org.ithinking.tengine.core.Lexer;
import org.ithinking.tengine.core.Token;
import org.ithinking.tengine.sql.SQLLexer;
import org.junit.Test;

public class LexerTest {

	@Test
	public void test() {
		String sql = " '1' ";
		Lexer lexer = new SQLLexer(sql);
		Token token;
		while(lexer.hasNext()){
			token = lexer.next();
			System.out.println(token);
		}
		
	}
	
	
}
