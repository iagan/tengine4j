package org.ithinking.tengine.core;

public interface Lexer {

	public boolean hasNext();
	
	public Token next();
	
	public String getText();
}
