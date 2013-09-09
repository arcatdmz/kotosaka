package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 値を返す演算表現の親クラス
 */
public abstract class CalcExpression extends Expression {
	protected Object op1;
	protected Object op2;
	protected float op1f;
	protected float op2f;
	public CalcExpression(Object op1, Object op2) {
		operands.add(op1);
		operands.add(op2);
	}
	public Object evaluate(Context context) {
		op1 = ((Expression) operands.get(0)).evaluate(context);
		op2 = ((Expression) operands.get(1)).evaluate(context);
		return null;
	}
	protected boolean getNumber() {
		if (!(op1 instanceof Float)) return false;
		if (!(op2 instanceof Float)) return false;
		op1f = ((Float) op1).floatValue();
		op2f = ((Float) op2).floatValue();
		return true;
	}
}
