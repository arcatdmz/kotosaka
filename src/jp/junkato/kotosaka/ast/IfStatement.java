package jp.junkato.kotosaka.ast;
import java.util.List;

import jp.junkato.kotosaka.Context;


/**
 * if文
 */
public class IfStatement extends Statement {
	public IfStatement(Expression expr, List<Evaluator> instructions) {
		operands.add(expr);
		operands.add(instructions);
	}
	public IfStatement(Expression expr, List<Evaluator> instructions, List<Evaluator> instructions_else) {
		operands.add(expr);
		operands.add(instructions);
		operands.add(instructions_else);
	}
	public Object evaluate(Context context) {
		Expression expr = (Expression) operands.get(0);
		Context localContext;

		// if true
		if((Boolean) expr.evaluate(context))
			localContext = context.createLocalContext((List<Evaluator>) operands.get(1));
		// if false else ...
		else if (operands.size() > 2)
			localContext = context.createLocalContext((List<Evaluator>) operands.get(2));
		// if false
		else return null;

		localContext.run();
		return null;
	}
}
