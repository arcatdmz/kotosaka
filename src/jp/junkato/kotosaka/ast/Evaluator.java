package jp.junkato.kotosaka.ast;
import java.util.*;

import jp.junkato.kotosaka.Context;


/**
 * 全ての評価器、構文木の葉の親クラス
 */
public abstract class Evaluator {
	private final String DEFAULT_CLASSNAME_PREFIX =
		"class jp.junkato.kotosaka.ast.";
	protected List<Object> operands = new ArrayList<Object>();
	protected Evaluator() {};

	public abstract Object evaluate(Context context);
	public Object getOperand(int index) { return operands.get(index); }
	public String toString() { return getClassName()+operands.toString(); }
	private String getClassName() {
		String className = this.getClass().toString();
		if (className.startsWith(
				DEFAULT_CLASSNAME_PREFIX))
		{
			return className.substring(
					DEFAULT_CLASSNAME_PREFIX.length());
		}
		else
		{
			return className;
		}
	}
}
