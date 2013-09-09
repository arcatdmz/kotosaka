package jp.junkato.kotosaka.ast;
import java.util.*;

import jp.junkato.kotosaka.Context;


/**
 * 全ての評価器、構文木の葉の親クラス
 */
public abstract class Evaluator {
	private final String DEFAULT_CLASSNAME_PREFIX =
		"class jp.designinterface.kotosaka.evaluator.";
	protected List<Object> operands = new ArrayList<Object>();
	protected Evaluator() {};

	public abstract Object evaluate(Context context);
	public Object getOperand(int index) { return operands.get(index); }
	public String toString() { return get_classname()+operands.toString(); }
	private String get_classname() {
		String classname = this.getClass().toString();
		if (classname.startsWith(
				DEFAULT_CLASSNAME_PREFIX))
		{
			return classname.substring(
					DEFAULT_CLASSNAME_PREFIX.length());
		}
		else
		{
			return classname;
		}
	}
}
