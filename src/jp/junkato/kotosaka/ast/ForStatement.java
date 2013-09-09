package jp.junkato.kotosaka.ast;
import java.util.List;

import jp.junkato.kotosaka.Context;


/**
 * for文
 */
public class ForStatement extends Statement {
	public ForStatement(Expression a, Expression b, Expression c, List<Evaluator> instructions) {
		operands.add(a);
		operands.add(b);
		operands.add(c);
		operands.add(instructions);
	}
	public Object evaluate(Context context) {
		Expression a = (Expression) operands.get(0);
		Expression b = (Expression) operands.get(1);
		Expression c = (Expression) operands.get(2);
		List<Evaluator> instructions = (List<Evaluator>) operands.get(3);

		if (a != null) a.evaluate(context);
		if (c != null) instructions.add(c);
		Context localContext = context.createLocalContext(instructions);

		//localContext.print();

		while((Boolean) b.evaluate(localContext)) localContext.run();
		return null;
	}
}
