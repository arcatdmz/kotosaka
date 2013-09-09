package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 引き算
 */
public class SubExpression extends CalcExpression {
	public SubExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (!getInt()) return null;
		return new Integer(op1i - op2i);
	}
}
