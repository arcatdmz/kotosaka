package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 不等号
 */
public class NeExpression extends BoolExpression {
	public NeExpression(Object op1, Object op2) { super(op1, op2); }

	public Boolean evaluate(Context context) {
		super.evaluate(context);
		return Boolean.valueOf(!op1.equals(op2));
	}
}
