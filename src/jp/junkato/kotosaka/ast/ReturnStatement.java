package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;


/**
 * return文
 *	context.return_valueの実装を確認する必要あり
 */
public class ReturnStatement extends Statement {
	public ReturnStatement(Expression expression) {
		operands.add(expression);
	}

	public Object evaluate(Context context) {
		Expression expression = (Expression) operands.get(0);
		// context.return_value = expression.evaluate(context);
		return expression.evaluate(context);
	}

}
