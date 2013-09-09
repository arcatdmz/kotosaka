package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 値を返す演算表現の親クラス
 */
public abstract class CalcExpression extends Expression {
	protected Object op1;
	protected Object op2;
	protected int op1i;
	protected int op2i;
	public CalcExpression(Object op1, Object op2) {
		operands.add(op1);
		operands.add(op2);
	}
	public Object evaluate(Context context) {
		op1 = ((Expression) operands.get(0)).evaluate(context);
		op2 = ((Expression) operands.get(1)).evaluate(context);
		return null;
	}
	protected boolean getInt() {
		if (!(op1 instanceof Integer)) return false;
		if (!(op2 instanceof Integer)) return false;
		op1i = ((Integer) op1).intValue();
		op2i = ((Integer) op2).intValue();
		return true;
	}
}
