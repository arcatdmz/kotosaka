package jp.junkato.kotosaka.ast;
import java.lang.reflect.*;

import jp.junkato.kotosaka.Context;

/**
 * フィールドへのアクセス
 *	Robot.positionなど
 */
public class FieldAccess extends Expression {
	public FieldAccess(Expression expression, String field_name) {
		operands.add(expression);
		operands.add(field_name);
	}

	public Object evaluate(Context context) {
		final Expression expression = (Expression) operands.get(0);
		final String field_name = (String) operands.get(1);
		final Object object = expression.evaluate(context);

		try {
			// Get a class object.
			final Class<?> cls = object.getClass();
			// Get a field.
			final Field field = cls.getField(field_name);
			// Get a field value.
			final Object ret = field.get(object);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
