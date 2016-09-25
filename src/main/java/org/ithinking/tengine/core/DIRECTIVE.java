package org.ithinking.tengine.core;

public enum DIRECTIVE {
	IGNORE("ignore",""), 
	IF("if",""), 
	INCLUDE("include",""), 
	REPEAT("repeat",""), 
	CONTINUE("continue",""), 
	DISPLAY("display",""),  
	WIDGET("widget",""),
	FRAG("frag",""),
	FRAGID("fragid",""),
	PARAM("param",""),
	ARG("arg",""),
	VAR("var",""),
	MODEL("model",""), 
	CLASS("class","class"),
	HEADER("header",""),
	PERM("perm",""),
	TEXT("text",""), 
	HTML("html",""), 
	ATTR("attr","*"),
	ANY_ATTR("*","*");

	private long code;
	private String directiveName;
	private String attrName;

	// $({user.desc})
	private DIRECTIVE(String directiveName, String attrName) {
		if (this.ordinal() > 64) {
			throw new IllegalArgumentException("枚举项过多!");
		}
		this.code = 1 << (this.ordinal() + 1);
		this.directiveName = directiveName;
		this.attrName = attrName;
	}

	/**
	 * 返回指令编码
	 * 
	 * @return
	 */
	public long getCode() {
		return this.code;
	}

	public boolean has(int directiveCodes) {
		return (code & directiveCodes) == code;
	}
	
	public String getDirectiveName(){
		return directiveName;
	}
	
	public String getAttrName(){
		return attrName;
	}

}
