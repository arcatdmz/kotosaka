package jp.junkato.kotosaka.ast;
import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.Variable;


/**
 * 変数へのアクセス
 */
public class VariableAccess extends Expression {
	public VariableAccess(String variable_name) {
		operands.add(variable_name);
	}

	public Object evaluate(Context context) {
		final String name = (String) operands.get(0);
		final Variable variable = context.getVariable(name);

		// 変数として登録されていなければ同名の関数も探す
		if (variable == null)
		{
			return context.getFunction(name);
		}
		return variable.value;
	}
}
