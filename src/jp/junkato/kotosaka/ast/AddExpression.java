package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 足し算
 */
public class AddExpression extends CalcExpression {
	public AddExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (op1 instanceof Integer) return Integer.valueOf(((Integer) op1).intValue() + ((Integer) op2).intValue());
		return (op1==null?String.valueOf(op1i):op1.toString()) +
			(op2==null?String.valueOf(op2i):op2.toString());
	}
}
