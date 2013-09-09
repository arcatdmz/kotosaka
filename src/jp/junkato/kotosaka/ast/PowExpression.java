package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 累乗
 */
public class PowExpression extends CalcExpression {
	public PowExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (!getNumber()) return null;
		return new Float(Math.pow(op1f, op2f));
	}
}
