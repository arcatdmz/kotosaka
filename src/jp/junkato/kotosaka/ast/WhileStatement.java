package jp.junkato.kotosaka.ast;
import java.util.List;

import jp.junkato.kotosaka.Context;


/**
 * while文
 */
public class WhileStatement extends Statement {
	public WhileStatement(Expression expr, List<Evaluator> instructions) {
		operands.add(expr);
		operands.add(instructions);
	}			
	public Object evaluate(Context context) {
		Expression expr = (Expression) operands.get(0);
		List<Evaluator> instructions = (List<Evaluator>) operands.get(1);

		Context localContext = context.createLocalContext(instructions);

		while((Boolean) expr.evaluate(localContext)) localContext.run();
		return null;
	}
}
