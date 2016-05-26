/*
 * Copyright 1999-2011 Ithinking Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ithinking.tengine.sql;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author fuchujian<vajava@126.com>
 */
public class Keywords {

    private final Map<String, TokenType> keywords;

    public final static Keywords     DEFAULT_KEYWORDS;
    public static String[] KEYS  ;

    static {
        Map<String, TokenType> map = new HashMap<String, TokenType>();

        map.put("ALL", TokenType.ALL);
        map.put("ALTER", TokenType.ALTER);
        map.put("AND", TokenType.AND);
        map.put("ANY", TokenType.ANY);
        map.put("AS", TokenType.AS);

        map.put("ENABLE", TokenType.ENABLE);
        map.put("DISABLE", TokenType.DISABLE);

        map.put("ASC", TokenType.ASC);
        map.put("BETWEEN", TokenType.BETWEEN);
        map.put("BY", TokenType.BY);
        map.put("CASE", TokenType.CASE);
        map.put("CAST", TokenType.CAST);

        map.put("CHECK", TokenType.CHECK);
        map.put("CONSTRAINT", TokenType.CONSTRAINT);
        map.put("CREATE", TokenType.CREATE);
        map.put("DATABASE", TokenType.DATABASE);
        map.put("DEFAULT", TokenType.DEFAULT);
        map.put("COLUMN", TokenType.COLUMN);
        map.put("TABLESPACE", TokenType.TABLESPACE);
        map.put("PROCEDURE", TokenType.PROCEDURE);
        map.put("FUNCTION", TokenType.FUNCTION);

        map.put("DELETE", TokenType.DELETE);
        map.put("DESC", TokenType.DESC);
        map.put("DISTINCT", TokenType.DISTINCT);
        map.put("DROP", TokenType.DROP);
        map.put("ELSE", TokenType.ELSE);
        map.put("EXPLAIN", TokenType.EXPLAIN);
        map.put("EXCEPT", TokenType.EXCEPT);

        map.put("END", TokenType.END);
        map.put("ESCAPE", TokenType.ESCAPE);
        map.put("EXISTS", TokenType.EXISTS);
        map.put("FOR", TokenType.FOR);
        map.put("FOREIGN", TokenType.FOREIGN);

        map.put("FROM", TokenType.FROM);
        map.put("FULL", TokenType.FULL);
        map.put("GROUP", TokenType.GROUP);
        map.put("HAVING", TokenType.HAVING);
        map.put("IN", TokenType.IN);

        map.put("INDEX", TokenType.INDEX);
        map.put("INNER", TokenType.INNER);
        map.put("INSERT", TokenType.INSERT);
        map.put("INTERSECT", TokenType.INTERSECT);
        map.put("INTERVAL", TokenType.INTERVAL);

        map.put("INTO", TokenType.INTO);
        map.put("IS", TokenType.IS);
        map.put("JOIN", TokenType.JOIN);
        map.put("KEY", TokenType.KEY);
        map.put("LEFT", TokenType.LEFT);

        map.put("LIKE", TokenType.LIKE);
        map.put("LOCK", TokenType.LOCK);
        map.put("MINUS", TokenType.MINUS);
        map.put("NOT", TokenType.NOT);

        map.put("NULL", TokenType.NULL);
        map.put("ON", TokenType.ON);
        map.put("OR", TokenType.OR);
        map.put("ORDER", TokenType.ORDER);
        map.put("OUTER", TokenType.OUTER);

        map.put("PRIMARY", TokenType.PRIMARY);
        map.put("REFERENCES", TokenType.REFERENCES);
        map.put("RIGHT", TokenType.RIGHT);
        map.put("SCHEMA", TokenType.SCHEMA);
        map.put("SELECT", TokenType.SELECT);

        map.put("SET", TokenType.SET);
        map.put("SOME", TokenType.SOME);
        map.put("TABLE", TokenType.TABLE);
        map.put("THEN", TokenType.THEN);
        map.put("TRUNCATE", TokenType.TRUNCATE);

        map.put("UNION", TokenType.UNION);
        map.put("UNIQUE", TokenType.UNIQUE);
        map.put("UPDATE", TokenType.UPDATE);
        map.put("VALUES", TokenType.VALUES);
        map.put("VIEW", TokenType.VIEW);
        map.put("SEQUENCE", TokenType.SEQUENCE);
        map.put("TRIGGER", TokenType.TRIGGER);
        map.put("USER", TokenType.USER);

        map.put("WHEN", TokenType.WHEN);
        map.put("WHERE", TokenType.WHERE);
        map.put("XOR", TokenType.XOR);

        map.put("OVER", TokenType.OVER);
        map.put("TO", TokenType.TO);
        map.put("USE", TokenType.USE);

        map.put("REPLACE", TokenType.REPLACE);

        map.put("COMMENT", TokenType.COMMENT);
        map.put("COMPUTE", TokenType.COMPUTE);
        map.put("WITH", TokenType.WITH);
        map.put("GRANT", TokenType.GRANT);
        map.put("REVOKE", TokenType.REVOKE);

        DEFAULT_KEYWORDS = new Keywords(map);
    }

    public boolean containsValue(TokenType token) {
        return this.keywords.containsValue(token);
    }

    public Keywords(Map<String, TokenType> keywords){
        this.keywords = keywords;
        //
        KEYS = new String[keywords.size()];
        Iterator<String> iter = keywords.keySet().iterator();
        int i = 0;
        while(iter.hasNext()){
        	KEYS[i] = iter.next();
        	i++;
        }
    }

    public TokenType getKeyword(String key) {
        return getKeyword(key, 0, key.length());
    }
    
	public TokenType getKeyword(String text, int start, int count) {
		String key;
		for (int i = 0; i < KEYS.length; i++) {
			key = KEYS[i];
			if (key.regionMatches(true, 0, text, start, count)) {
				return keywords.get(key);
			}
		}
		return null;
	}

    public Map<String, TokenType> getKeywords() {
        return keywords;
    }

}
