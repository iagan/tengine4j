package org.ithinking.tengine.core;

import org.ithinking.tengine.sql.TokenType;

public interface Token {
	
	public TokenType getType();

	public boolean isType(TokenType type);

	public String getValue();
}
