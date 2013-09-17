package jp.junkato.kotosaka.ast;
import java.util.ArrayList;
import java.util.List;

import jp.junkato.kotosaka.Context;
import jp.junkato.math.Matrix2x2f;
import jp.junkato.math.Vector2f;


/**
 * 数値
 */
public class MatrixLiteral extends Expression {
	public MatrixLiteral(List<Expression> elementList) {
		operands.add(elementList);
	}

	@SuppressWarnings("unchecked")
	public Object evaluate(Context context) {
		List<Expression> elementList = (List<Expression>) operands.get(0);
		List<Float> valueList = new ArrayList<Float>(4);
		for (Expression element : elementList) {
			Object ret = element.evaluate(context);
			if (ret == null) {
				valueList.add(Float.NaN);
			} else if (ret instanceof Float) {
				valueList.add((Float) ret);
			} else try {
				valueList.add(Float.valueOf(ret.toString()));
			} catch (NumberFormatException nfe) {
				valueList.add(Float.NaN);
			} 
		}
		if (valueList.size() == 4) {
			Matrix2x2f m = new Matrix2x2f();
			for (int i = 0; i < 4; i ++) {
				m.set(i % 2, i / 2, valueList.get(i));
			}
			return m;
		}
		if (valueList.size() == 2) {
			return new Vector2f(
					valueList.get(0), valueList.get(1));
		}
		return null;
	}
}