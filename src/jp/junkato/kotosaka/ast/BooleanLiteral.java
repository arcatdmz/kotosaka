package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * Boolean
 */
public class BooleanLiteral extends Expression {
	public BooleanLiteral(Boolean bool) {
		operands.add(bool);
	}

	public Boolean evaluate(Context context) {
		return (Boolean) operands.get(0);
	}
}