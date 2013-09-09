package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 数値
 */
public class NumberLiteral extends Expression {
	public NumberLiteral(Object integer) {
		operands.add(integer);
	}

	public Object evaluate(Context context) {
		return (Float) operands.get(0);
	}
}