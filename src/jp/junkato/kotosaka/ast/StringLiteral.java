package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * 文字列
 */
public class StringLiteral extends Expression {
	public StringLiteral(Object string) {
		operands.add(string);
	}

	public Object evaluate(Context context) {
		return operands.get(0);
	}
}