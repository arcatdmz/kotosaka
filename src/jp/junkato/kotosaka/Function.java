package jp.junkato.kotosaka;
import java.util.List;

import jp.junkato.kotosaka.ast.Evaluator;


/**
 * 関数を表すクラス
 */
public class Function {
	public String name;
	public List<String> arguments;
	public List<Evaluator> instructions;

	public Function(String functionName, List<String> arguments, List<Evaluator> instructions) {
		this.name = functionName;
		this.arguments = arguments;
		this.instructions = instructions;
	}
	public int size() {
		return arguments.size();
	}
}
