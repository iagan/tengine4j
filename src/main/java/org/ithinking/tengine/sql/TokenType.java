/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
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

/**
 * 
 * SQL Token 
 * @author wenshao 2011-5-18 下午05:16:49
 * @formatter:off
 */
public enum TokenType {
    SELECT("SELECT", TokenFeatures.KEYWORD), 
    DELETE("DELETE", TokenFeatures.KEYWORD), 
    INSERT("INSERT", TokenFeatures.KEYWORD), 
    UPDATE("UPDATE", TokenFeatures.KEYWORD), 
    
    FROM("FROM", TokenFeatures.KEYWORD), 
    HAVING("HAVING", TokenFeatures.KEYWORD), 
    WHERE("WHERE", TokenFeatures.KEYWORD), 
    ORDER("ORDER", TokenFeatures.KEYWORD), 
    BY("BY", TokenFeatures.KEYWORD),
    GROUP("GROUP", TokenFeatures.KEYWORD), 
    INTO("INTO", TokenFeatures.KEYWORD), 
    AS("AS", TokenFeatures.KEYWORD), 
    
    CREATE("CREATE", TokenFeatures.KEYWORD),
    ALTER("ALTER", TokenFeatures.KEYWORD), 
    DROP("DROP", TokenFeatures.KEYWORD), 
    SET("SET", TokenFeatures.KEYWORD), 
   
    NULL("NULL", TokenFeatures.KEYWORD), 
    NOT("NOT", TokenFeatures.KEYWORD), 
    DISTINCT("DISTINCT", TokenFeatures.KEYWORD),

    TABLE("TABLE", TokenFeatures.KEYWORD), 
    TABLESPACE("TABLESPACE", TokenFeatures.KEYWORD), 
    VIEW("VIEW", TokenFeatures.KEYWORD), 
    SEQUENCE("SEQUENCE", TokenFeatures.KEYWORD), 
    TRIGGER("TRIGGER", TokenFeatures.KEYWORD), 
    USER("USER", TokenFeatures.KEYWORD), 
    INDEX("INDEX", TokenFeatures.KEYWORD), 
    SESSION("SESSION", TokenFeatures.KEYWORD),
    PROCEDURE("PROCEDURE", TokenFeatures.KEYWORD),
    FUNCTION("FUNCTION", TokenFeatures.KEYWORD),
    
    PRIMARY("PRIMARY", TokenFeatures.KEYWORD), 
    KEY("KEY", TokenFeatures.KEYWORD), 
    DEFAULT("DEFAULT", TokenFeatures.KEYWORD), 
    CONSTRAINT("CONSTRAINT", TokenFeatures.KEYWORD), 
    CHECK("CHECK", TokenFeatures.KEYWORD), 
    UNIQUE("UNIQUE", TokenFeatures.KEYWORD), 
    FOREIGN("FOREIGN", TokenFeatures.KEYWORD), 
    REFERENCES("REFERENCES", TokenFeatures.KEYWORD), 
    
    EXPLAIN("EXPLAIN", TokenFeatures.KEYWORD), 
    FOR("FOR", TokenFeatures.KEYWORD), 
    IF("IF", TokenFeatures.KEYWORD), 
   
   
   
    ALL("ALL", TokenFeatures.KEYWORD), 
    UNION("UNION", TokenFeatures.KEYWORD), 
    EXCEPT("EXCEPT", TokenFeatures.KEYWORD), 
    INTERSECT("INTERSECT", TokenFeatures.KEYWORD), 
    MINUS("MINUS", TokenFeatures.KEYWORD),
    INNER("INNER", TokenFeatures.KEYWORD), 
    LEFT("LEFT", TokenFeatures.KEYWORD), 
    RIGHT("RIGHT", TokenFeatures.KEYWORD), 
    FULL("FULL", TokenFeatures.KEYWORD), 
    OUTER("OUTER", TokenFeatures.KEYWORD), 
    JOIN("JOIN", TokenFeatures.KEYWORD), 
    ON("ON", TokenFeatures.KEYWORD), 
    SCHEMA("SCHEMA", TokenFeatures.KEYWORD), 
    CAST("CAST", TokenFeatures.KEYWORD),
    COLUMN("COLUMN", TokenFeatures.KEYWORD),
    USE("USE", TokenFeatures.KEYWORD),
    DATABASE("DATABASE", TokenFeatures.KEYWORD),
    TO("TO", TokenFeatures.KEYWORD),

    AND("AND", TokenFeatures.KEYWORD), 
    OR("OR", TokenFeatures.KEYWORD), 
    XOR("XOR", TokenFeatures.KEYWORD), 
    CASE("CASE", TokenFeatures.KEYWORD), 
    WHEN("WHEN", TokenFeatures.KEYWORD), 
    THEN("THEN", TokenFeatures.KEYWORD), 
    ELSE("ELSE", TokenFeatures.KEYWORD), 
    END("END", TokenFeatures.KEYWORD), 
    EXISTS("EXISTS", TokenFeatures.KEYWORD), 
    IN("IN", TokenFeatures.KEYWORD),

    NEW("NEW", TokenFeatures.KEYWORD), 
    ASC("ASC", TokenFeatures.KEYWORD), 
    DESC("DESC", TokenFeatures.KEYWORD), 
    IS("IS", TokenFeatures.KEYWORD), 
    LIKE("LIKE", TokenFeatures.KEYWORD), 
    ESCAPE("ESCAPE", TokenFeatures.KEYWORD), 
    BETWEEN("BETWEEN", TokenFeatures.KEYWORD), 
    VALUES("VALUES", TokenFeatures.KEYWORD), 
    INTERVAL("INTERVAL", TokenFeatures.KEYWORD),

    LOCK("LOCK", TokenFeatures.KEYWORD), 
    SOME("SOME", TokenFeatures.KEYWORD), 
    ANY("ANY", TokenFeatures.KEYWORD),
    TRUNCATE("TRUNCATE", TokenFeatures.KEYWORD),

    // mysql
    TRUE("TRUE", TokenFeatures.KEYWORD), 
    FALSE("FALSE", TokenFeatures.KEYWORD),
    LIMIT("LIMIT", TokenFeatures.KEYWORD),
    KILL("KILL", TokenFeatures.KEYWORD),
    IDENTIFIED("IDENTIFIED", TokenFeatures.KEYWORD),
    PASSWORD("PASSWORD", TokenFeatures.KEYWORD),
    DUAL("DUAL", TokenFeatures.KEYWORD),
    BINARY("BINARY", TokenFeatures.KEYWORD),
    SHOW("SHOW", TokenFeatures.KEYWORD),
    REPLACE("REPLACE", TokenFeatures.KEYWORD),
    
    //postgresql
    WINDOW("WINDOW", TokenFeatures.KEYWORD),
    OFFSET("OFFSET", TokenFeatures.KEYWORD),
    ROW("ROW", TokenFeatures.KEYWORD),
    ROWS("ROWS", TokenFeatures.KEYWORD),
    ONLY("ONLY", TokenFeatures.KEYWORD),
    FIRST("FIRST", TokenFeatures.KEYWORD),
    NEXT("NEXT", TokenFeatures.KEYWORD),
    FETCH("FETCH", TokenFeatures.KEYWORD),
    OF("OF", TokenFeatures.KEYWORD),
    SHARE("SHARE", TokenFeatures.KEYWORD),
    NOWAIT("NOWAIT", TokenFeatures.KEYWORD),
    RECURSIVE("RECURSIVE", TokenFeatures.KEYWORD),
    TEMPORARY("TEMPORARY", TokenFeatures.KEYWORD),
    TEMP("TEMP", TokenFeatures.KEYWORD),
    UNLOGGED("UNLOGGED", TokenFeatures.KEYWORD),
    RESTART("RESTART", TokenFeatures.KEYWORD),
    IDENTITY("IDENTITY", TokenFeatures.KEYWORD),
    CONTINUE("CONTINUE", TokenFeatures.KEYWORD),
    CASCADE("CASCADE", TokenFeatures.KEYWORD),
    RESTRICT("RESTRICT", TokenFeatures.KEYWORD),
    USING("USING", TokenFeatures.KEYWORD),
    CURRENT("CURRENT", TokenFeatures.KEYWORD),
    RETURNING("RETURNING", TokenFeatures.KEYWORD),
    COMMENT("COMMENT", TokenFeatures.KEYWORD),
    OVER("OVER", TokenFeatures.KEYWORD),
    TYPE("TYPE", TokenFeatures.KEYWORD), 
    
    // oracle
    START("START", TokenFeatures.KEYWORD),
    PRIOR("PRIOR", TokenFeatures.KEYWORD),
    CONNECT("CONNECT", TokenFeatures.KEYWORD),
    WITH("WITH" , TokenFeatures.KEYWORD),
    EXTRACT("EXTRACT", TokenFeatures.KEYWORD),
    CURSOR("CURSOR", TokenFeatures.KEYWORD),
    MODEL("MODEL", TokenFeatures.KEYWORD),
    MERGE("MERGE", TokenFeatures.KEYWORD),
    MATCHED("MATCHED", TokenFeatures.KEYWORD),
    ERRORS("ERRORS", TokenFeatures.KEYWORD),
    REJECT("REJECT", TokenFeatures.KEYWORD),
    UNLIMITED("UNLIMITED", TokenFeatures.KEYWORD),
    BEGIN("BEGIN", TokenFeatures.KEYWORD),
    EXCLUSIVE("EXCLUSIVE", TokenFeatures.KEYWORD),
    MODE("MODE", TokenFeatures.KEYWORD),
    WAIT("WAIT", TokenFeatures.KEYWORD),
    ADVISE("ADVISE", TokenFeatures.KEYWORD),
    SYSDATE("SYSDATE", TokenFeatures.KEYWORD),
    DECLARE("DECLARE", TokenFeatures.KEYWORD),
    EXCEPTION("EXCEPTION", TokenFeatures.KEYWORD),
    GRANT("GRANT", TokenFeatures.KEYWORD),
    REVOKE("REVOKE", TokenFeatures.KEYWORD),
    LOOP("LOOP", TokenFeatures.KEYWORD),
    GOTO("GOTO", TokenFeatures.KEYWORD),
    COMMIT("COMMIT", TokenFeatures.KEYWORD),
    SAVEPOINT("SAVEPOINT", TokenFeatures.KEYWORD),
    CROSS("CROSS", TokenFeatures.KEYWORD),
    
    PCTFREE("PCTFREE", TokenFeatures.KEYWORD),
    INITRANS("INITRANS", TokenFeatures.KEYWORD),
    MAXTRANS("MAXTRANS", TokenFeatures.KEYWORD),
    INITIALLY("INITIALLY", TokenFeatures.KEYWORD),
    ENABLE("ENABLE", TokenFeatures.KEYWORD),
    DISABLE("DISABLE", TokenFeatures.KEYWORD),
    SEGMENT("SEGMENT", TokenFeatures.KEYWORD),
    CREATION("CREATION", TokenFeatures.KEYWORD),
    IMMEDIATE("IMMEDIATE", TokenFeatures.KEYWORD),
    DEFERRED("DEFERRED", TokenFeatures.KEYWORD),
    STORAGE("STORAGE", TokenFeatures.KEYWORD),
    MINEXTENTS("MINEXTENTS", TokenFeatures.KEYWORD),
    MAXEXTENTS("MAXEXTENTS", TokenFeatures.KEYWORD),
    MAXSIZE("MAXSIZE", TokenFeatures.KEYWORD),
    PCTINCREASE("PCTINCREASE", TokenFeatures.KEYWORD),
    FLASH_CACHE("FLASH_CACHE", TokenFeatures.KEYWORD),
    CELL_FLASH_CACHE("CELL_FLASH_CACHE", TokenFeatures.KEYWORD),
    KEEP("KEEP", TokenFeatures.KEYWORD),
    NONE("NONE", TokenFeatures.KEYWORD),
    LOB("LOB", TokenFeatures.KEYWORD),
    STORE("STORE", TokenFeatures.KEYWORD),
    CHUNK("CHUNK", TokenFeatures.KEYWORD),
    CACHE("CACHE", TokenFeatures.KEYWORD),
    NOCACHE("NOCACHE", TokenFeatures.KEYWORD),
    LOGGING("LOGGING", TokenFeatures.KEYWORD),
    NOCOMPRESS("NOCOMPRESS", TokenFeatures.KEYWORD),
    KEEP_DUPLICATES("KEEP_DUPLICATES", TokenFeatures.KEYWORD),
    EXCEPTIONS("EXCEPTIONS", TokenFeatures.KEYWORD),
    PURGE("PURGE", TokenFeatures.KEYWORD),
    
    COMPUTE("COMPUTE", TokenFeatures.KEYWORD),
    ANALYZE("ANALYZE", TokenFeatures.KEYWORD),
    OPTIMIZE("OPTIMIZE", TokenFeatures.KEYWORD),
    
    // transact-sql
    TOP("TOP", TokenFeatures.KEYWORD),
    
    ARRAY("ARRAY", TokenFeatures.KEYWORD),
    
    // hive

    EOF, 
    ERROR,
    IDENTIFIER(TokenFeatures.VAL),
    HINT(TokenFeatures.VAL),
    VARIANT(TokenFeatures.VAL),
    LITERAL_INT(TokenFeatures.VAL),
    LITERAL_FLOAT(TokenFeatures.VAL),
    LITERAL_HEX(TokenFeatures.VAL),
    LITERAL_CHARS(TokenFeatures.VAL),
    LITERAL_NCHARS(TokenFeatures.VAL),
    
    LITERAL_ALIAS(TokenFeatures.VAL),
    LINE_COMMENT(TokenFeatures.VAL),
    MULTI_LINE_COMMENT(TokenFeatures.VAL),
    
    // Oracle
    BINARY_FLOAT(TokenFeatures.VAL),
    BINARY_DOUBLE(TokenFeatures.VAL),
    
    // odps,hive
    PARTITION,
    PARTITIONED,
    OVERWRITE,

    PAREN(),
    LPAREN("(", TokenFeatures.OPER), 
    RPAREN(")", TokenFeatures.OPER), 
    LBRACE("{", TokenFeatures.OPER), 
    RBRACE("}", TokenFeatures.OPER), 
    LBRACKET("[", TokenFeatures.OPER), 
    RBRACKET("]", TokenFeatures.OPER), 
    SEMI(";", TokenFeatures.OPER), 
    COMMA(",", TokenFeatures.OPER), 
    DOT(".", TokenFeatures.OPER), 
    DOTDOT("..", TokenFeatures.OPER), 
    DOTDOTDOT("..,", TokenFeatures.OPER), 
    EQ("=", TokenFeatures.OPER), 
    GT(">", TokenFeatures.OPER), 
    LT("<", TokenFeatures.OPER), 
    BANG("!", TokenFeatures.OPER),
    BANGBANG("!!", TokenFeatures.OPER),
    TILDE("~", TokenFeatures.OPER), 
    QUES("?", TokenFeatures.OPER), 
    COLON(":", TokenFeatures.OPER), 
    COLONCOLON(":", TokenFeatures.OPER), 
    COLONEQ(":=", TokenFeatures.OPER), 
    EQEQ("==", TokenFeatures.OPER), 
    LTEQ("<=", TokenFeatures.OPER), 
    LTEQGT("<=>", TokenFeatures.OPER), 
    LTGT("<>", TokenFeatures.OPER), 
    GTEQ(">=", TokenFeatures.OPER), 
    BANGEQ("!=", TokenFeatures.OPER), 
    BANGGT("!>", TokenFeatures.OPER), 
    BANGLT("!<", TokenFeatures.OPER),
    AMPAMP("&&", TokenFeatures.OPER), 
    BARBAR("||", TokenFeatures.OPER), 
    BARBARSLASH("||/", TokenFeatures.OPER), 
    BARSLASH("|/", TokenFeatures.OPER), 
    PLUS("+", TokenFeatures.OPER), 
    SUB("-", TokenFeatures.OPER), 
    STAR("*", TokenFeatures.OPER), 
    SLASH("/", TokenFeatures.OPER), 
    AMP("&", TokenFeatures.OPER), 
    BAR("|", TokenFeatures.OPER), 
    CARET("^", TokenFeatures.OPER), 
    PERCENT("%", TokenFeatures.OPER), 
    LTLT("<<", TokenFeatures.OPER), 
    GTGT(">>", TokenFeatures.OPER),
    MONKEYS_AT("@", TokenFeatures.OPER),
    POUND("#", TokenFeatures.OPER),
    BLANK
    ;

    public final String name;
    public final int features;
   

    TokenType(){
        this(null);
    }
    
    TokenType(int features){
       this(null, features);
    }

    TokenType(String name){
    	 this(name, 0);
    }
    
    TokenType(String name, int features){
        this.name = name;
        this.features = features;
    }
    
    public boolean isFeatures(int features){
    	return (this.features & features) == features;
    }
    
}
