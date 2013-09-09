package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.Variable;


/**
 * 変数への値の代入
 */
public class VariableAssign extends Expression {
	public VariableAssign(String variableName, Expression assignment) {
		operands.add(variableName);
		operands.add(assignment);
	}

	public Object evaluate(Context context) {
		String variableName = (String) operands.get(0);
		Expression assignment = (Expression) operands.get(1);

		Variable variable = context.getVariable(variableName);

		// 変数の新規作成
		if (variable == null)
			variable = context.createVariable(variableName);
		variable.value = assignment.evaluate(context);

		return variable.value;
	}
}
