package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 足し算
 */
public class AddExpression extends CalcExpression {
	public AddExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (getNumber()) {
			return Float.valueOf(op1f + op2f);
		}
		return (op1 == null ? "" : op1.toString()) +
			(op2 == null ? "" : op2.toString());
	}
}
