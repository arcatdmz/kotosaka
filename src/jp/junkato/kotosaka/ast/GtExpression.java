package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 不等号 Greater than
 */
public class GtExpression extends BoolExpression {
	public GtExpression(Object op1, Object op2) { super(op1, op2); }

	public Boolean evaluate(Context context) {
		super.evaluate(context);
		if (!prepareCalc()) return null;
		if (op1f == null && op2f == null) return null;
		return Boolean.valueOf(op1f > op2f);
	}
}
