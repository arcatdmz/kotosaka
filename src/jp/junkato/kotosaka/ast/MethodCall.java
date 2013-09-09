package jp.junkato.kotosaka.ast;
import java.util.List;
import java.lang.reflect.*;

import jp.junkato.kotosaka.Context;


/**
 * Method call
 */
public class MethodCall extends Expression {
	public MethodCall(Expression expression, String method_name, List<Expression> argument_list){

		Object[] arguments = new Object[argument_list.size()];
		for(int i = 0; i < argument_list.size(); i ++) arguments[i] = argument_list.get(i);

		operands.add(expression);
		operands.add(method_name);
		operands.add(arguments);
	}

	public Object evaluate(Context context) {
		Expression expression = (Expression) operands.get(0);
		String method_name = (String) operands.get(1);
		Object[] expressions = (Object[]) operands.get(2);

		// expressionはVariableAccess型かFieldAccess型で、
		// evaluateすると何らかの(world/Robotなどのクラスを持った)オブジェクトを返す
		Object object = expression.evaluate(context);

		if (object == null)
		{
			return null;
		}

		// 取得したオブジェクトのメソッドに渡す引数配列と、
		// その型を表す配列を用意する
		Object[] arguments = new Object[expressions.length];
		Class<?>[] params = new Class[arguments.length];
		for (int i = 0; i < arguments.length; i ++) {
			if (expressions[i] == null) {
				arguments[i] = null;
				params[i] = null;
			} else {
				arguments[i] = ((Expression) expressions[i]).evaluate((context));
				params[i] = arguments[i] == null ? Object.class : arguments[i].getClass();
			}

			// TODO temporary walkaround, 後で直す必要あり
			//	int型を使うメソッドでInteger型の値を持つオブジェクトを渡すとエラーが出るため、
			//	アドホックに修正した模様
			if (arguments[i] instanceof Integer) {
				params[i] = int.class;
			}
		}

		try {
			// クラスを取得
			Class<?> cls = object.getClass();
			// メソッドを取得
			//	元コメント「スーパークラスも探す必要あり」
			Method method = cls.getMethod(method_name, params);

			// cls.getSuperclass();
			// TODO search for methods which accept arguments consisting of super classes of the original arguments.

			// メソッドを実行
			Object ret = method.invoke(object, arguments);
			return ret;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.err.println("Method \"" + method_name + "\" not found.");
	    }
	    return null;
	}
}
