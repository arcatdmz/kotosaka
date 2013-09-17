package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 剰余算
 */
public class ModExpression extends CalcExpression {
	public ModExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (prepareCalc()) {
			if (op1f != null && op2f != null)
				return Float.valueOf(op1f % op2f);
		}
		return null;
	}
}
