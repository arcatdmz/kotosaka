package jp.junkato.kotosaka.ast;
import java.lang.reflect.*;

import jp.junkato.kotosaka.Context;

/**
 * フィールドへの値の代入
 */
public class FieldAssign extends Expression {
	public FieldAssign(Expression expression, String field_name, Expression assignment) {
		operands.add(expression);
		operands.add(field_name);
		operands.add(assignment);
	}

	public Object evaluate(Context context) {
		final Expression expression = (Expression) operands.get(0);
		final String field_name = (String) operands.get(1);
		final Expression assignment = (Expression) operands.get(2);

		Object object = expression.evaluate(context);

		try {
			// クラスを取得
			final Class<?> cls = object.getClass();
			// クラス変数を取得
			final Field field = cls.getField(field_name);
			// クラス変数の値を設定
			final Object ret = assignment.evaluate(context);
			field.set(object, ret);
			return ret;
		} catch (Exception e) {
			System.err.println("Failed to set member variable.");
		}
		return null;
	}
}
