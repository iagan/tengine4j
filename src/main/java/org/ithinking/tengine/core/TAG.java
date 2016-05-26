package org.ithinking.tengine.core;

public class TAG {

	// 表达式属性 <img src="${root}/${user.id}.png">
	public static final int F_ATTR_EXPR = 1;
	// 指令标签 <div tg-repeat-user="userList">
	public static final int F_DIRECTIVE = 2;
	// meta 标签
	public static final int F_META = 4;
	// 字关闭标签 <br/>
	public static final int F_SELF_CLOSE = 8;
	// 强制关闭
	public static final int F_FIX_CLOSE = 16;
	// 正常关闭
	public static final int F_TAG_CLOSE = 32;
	// 文本标签 <script> ... </script>
	public static final int F_TEXT_TAG = 64;
	// 空标签 <input>
	public static final int F_EMPTY_TAG = 128;
	// 正常标签
	public static final int F_NORMAL_TAG = 256;

	/**
	 * 
	 */
	public static final String DIRECTIVE_PREFIX = "data-tg-";

	/**
	 * SYMBOL
	 */
	public static final String SYMBOL_DOCTYPE = "<!DOCTYPE";
	public static final String SYMBOL_COMMENT = "<!--";
	public static final String SYMBOL_CDATA = "<![CDATA[";
	public static final String SYMBOL_PI = "<?";
	public static final String SYMBOL_TAG_END = "</";

	/**
	 * tag name
	 */
	public static final String SCRIPT = "script";
	public static final String STYLE = "style";
	public static final String META = "meta";

}
