package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * Boolean型を返す演算表現の親クラス
 */
public abstract class BoolExpression extends Expression {
	protected Object op1;
	protected Object op2;
	protected int op1i;
	protected int op2i;
	public BoolExpression(Object op1, Object op2) {
		operands.add(op1);
		operands.add(op2);
	}
	public Boolean evaluate(Context context) {
		op1 = ((Expression) operands.get(0)).evaluate(context);
		op2 = ((Expression) operands.get(1)).evaluate(context);
		return null;
	}
	protected boolean get_int() {
		if (!(op1 instanceof Integer)) return false;
		if (!(op2 instanceof Integer)) return false;
		op1i = ((Integer) op1).intValue();
		op2i = ((Integer) op2).intValue();
		return true;
	}
}
