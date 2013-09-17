package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 掛け算
 */
public class MulExpression extends CalcExpression {
	public MulExpression(Object op1, Object op2) { super(op1, op2); }

	public Object evaluate(Context context) {
		super.evaluate(context);
		if (prepareCalc()) {
			if (op1m != null && op2m != null)
				return op1m.mul(op2m);
			if (op1m != null && op2v != null)
				return op1m.mul(op2v);
			if (op1m != null && op2f != null)
				return op1m.mul(op2f);
			if (op1v != null && op2v != null)
				return op1v.mul(op2v);
			if (op1v != null && op2f != null)
				return op1v.mul(op2f);
			if (op1f != null && op2f != null)
				return Float.valueOf(op1f * op2f);
		}
		return null;
	}
}
