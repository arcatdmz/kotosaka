package jp.junkato.kotosaka.ast;
import java.util.List;
import java.util.ArrayList;

import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.Variable;


/**
 * foreach文
 */
public class ForeachStatement  extends Statement {
	public ForeachStatement(String variableName, String listName, List<Evaluator> instructions) {
		this(variableName, listName, instructions, null);
	}
	public ForeachStatement(String variableName, String listName, List<Evaluator> instructions, List<Expression> exceptions) {
		operands.add(variableName);
		operands.add(listName);
		operands.add(instructions);
		operands.add(exceptions);
	}

	public Object evaluate(Context context){
		String variableName = (String) operands.get(0);
		List<Variable> list = (List<Variable>) (context.getVariable((String) operands.get(1))).value;
		List<Evaluator> instructions = (List<Evaluator>) operands.get(2);
		List<Expression> exceptions = (List<Expression>) operands.get(3);
		List<Object> excValues = new ArrayList<Object>();

		if (exceptions != null)
			for(int i = 0; i < exceptions.size(); i ++)
				excValues.add(exceptions.get(i).evaluate(context));

		for(int j = 0; j < list.size(); j ++) {
			Context localContext = context.createLocalContext(instructions);

			Variable variable = new Variable(variableName, null);
			variable.value = list.get(j);

			// 例外チェック
			if (excValues != null) {
				System.out.println("exception");
				if (excValues.contains(variable.value)) {
					excValues.remove(variable.value);
					continue;
				}
				System.out.println(variable.value);
			}

			localContext.addVariable(variable);
			localContext.run();
		}
		
		
		return null;
	}

}
