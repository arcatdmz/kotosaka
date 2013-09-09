package jp.junkato.kotosaka.ast;
import java.util.List;

import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.Function;
import jp.junkato.kotosaka.Variable;


/**
 * Function call
 */
public class FunctionCall extends Expression {
	public FunctionCall(String functionName, List<Expression> argumentList){
		operands.add(functionName);
		operands.add(argumentList);
	}

	@SuppressWarnings("unchecked")
	public Object evaluate(Context context) {
		String functionName = (String) operands.get(0);
		List<Expression> argumentList = (List<Expression>) operands.get(1);

		Function function = context.getFunction(functionName);

		// If the specified function was not found,
		if (function == null)
		{
			// return null.
			return null;
		}

		Context localContext = context.createLocalContext(function.instructions);

		Object[] arguments = new Object[argumentList.size()];
		for(int i = 0; i < argumentList.size(); i ++) {
			Expression expression = (Expression) argumentList.get(i);
			if (expression == null) arguments[i] = null;
			else arguments[i] = expression.evaluate(context);
		}

		for(int i = 0; i < function.arguments.size(); i++) {
			String name = function.arguments.get(i).toString();
			if (arguments[i] != null) {
				// 関数渡し
				if (arguments[i] instanceof Function) {
					Function func = (Function) arguments[i];
					localContext.addFunction(new Function(name, func.arguments, func.instructions));
					continue;
				}
			}
			localContext.addVariable(new Variable(name, arguments[i]));
		}

		// デバッグ用
		/*
		System.out.println("== local context (calling "+function_name+") ==");
		local_context.print();
		*/

		return localContext.run();
	}
}
