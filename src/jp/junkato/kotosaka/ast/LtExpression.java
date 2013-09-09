package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 不等号 Less than
 */
public class LtExpression extends BoolExpression {
	public LtExpression(Object op1, Object op2) { super(op1, op2); }

	public Boolean evaluate(Context context) {
		super.evaluate(context);
		if (!super.get_int()) return null;
		return Boolean.valueOf(op1i < op2i);
	}
}
