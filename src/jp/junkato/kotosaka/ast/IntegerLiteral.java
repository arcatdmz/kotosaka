package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 数値
 */
public class IntegerLiteral extends Expression {
	public IntegerLiteral(Object integer) {
		operands.add(integer);
	}

	public Object evaluate(Context context) {
		return (Integer) operands.get(0);
	}
}