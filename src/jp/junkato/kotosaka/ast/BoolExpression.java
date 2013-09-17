package jp.junkato.kotosaka.ast;


/**
 * Boolean型を返す演算表現の親クラス
 */
public abstract class BoolExpression extends CalcExpression {
	public BoolExpression(Object op1, Object op2) {
		super(op1, op2);
	}
}
