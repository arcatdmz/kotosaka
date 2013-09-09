package jp.junkato.kotosaka;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Queue;

import jp.junkato.kotosaka.ast.*;

public class KotosakaParser {

	public static enum RESERVED {
		SYS_FOREACH("foreach"), SYS_IN("in"), SYS_EXCEPT("except"),
		SYS_IF("if"), SYS_ELSE("else"),
		SYS_FOR("for"), SYS_WHILE("while"),
		SYS_FUNCTION("function"),
		SYS_RETURN("return"),
		SYS_PRINT("print"),
		SYS_NEW("new"),
		SYS_TRUE("true"),
		SYS_FALSE("false");
		private final String text;

		private RESERVED(String text) {
			this.text = text;
		}

		public String toString() {
			return this.text;
		}
	}

	public static final Hashtable<String, RESERVED> reservedWords;

	static {
		reservedWords = new Hashtable<String, RESERVED>();
		for (RESERVED reserved : RESERVED.values()) {
			reservedWords.put(reserved.toString(), reserved);
		}
	}

	/** Tokens list */
	private Queue<Token> tokens;

	/** Token */
	private Token token;

	/** Depth of the current parsing context. */
	private int depth;

	/** Root context. */
	private Context context;

	/** Anonymous function name identifier. */
	private static int anonymousFunction = 0;

	/**
	 * トークン列をパースして階層化されたコンテクストとして返す
	 *
	 * @param _tokens
	 *            トークン列
	 * @return 階層化されたコンテクスト
	 */
	public Context parse(Queue<Token> tokens) {
		this.context = new Context();
		this.depth = 0;
		this.tokens = tokens;

		getNextToken();
		context.code.addAll(parseInternal());
		return context;
	}

	/**
	 * トップレベルあるいは { } の中をパースして Evaluator の列として返す
	 *
	 * @return Evaluator列
	 */
	private ArrayList<Evaluator> parseInternal() {
		final ArrayList<Evaluator> code = new ArrayList<Evaluator>();
		depth ++;

		while (true) {
			if (token == null)
				break;
			else if (token.equals('}')) {
				if (depth > 1)
					getNextToken(); // 括弧が閉じたので、次を読んで返る
				else
					; // パースエラー: トップレベルで括弧を閉じている
				break;
			}
			// TODO: save a correspondent token?
			// final Token correspondentToken = token;
			final Evaluator instruction = getInstruction();
			if (instruction != null)
			{
				code.add(instruction);
			}
		}
		depth --;
		return code;
	}

	/**
	 * 次のトークンを読み込んで削除する
	 *
	 * @return 読んだトークン
	 */
	private Token getNextToken() {
		token = tokens.poll();
		return token;
	}

	private Token checkNextToken() {
		return tokens.peek();
	}

	/**
	 * トークンを読み、オブジェクトにして返す
	 *
	 * @return 評価器を継承するオブジェクト
	 */
	private Evaluator getInstruction() {
		switch (token.getTag()) {

		// 構文要素
		case Tag.tagSystem:
			RESERVED sys = (RESERVED) token.value;
			if (sys == RESERVED.SYS_NEW)
			{
				return parseName(null);
			}

			getNextToken();
			switch (sys) {
			case SYS_IF:
				return parseIf();
			case SYS_FOR:
				return parseFor();
			case SYS_WHILE:
				return parseWhile();
			case SYS_FOREACH:
				return parseForeach();
			case SYS_FUNCTION:
				return parseFunction();
			case SYS_RETURN:
				return parseReturn();
			case SYS_PRINT:
				return parsePrint();
			default:
				// パースエラー？: 処理が定義されていない予約語
				break;
			}
			break;

		// 前置演算子
		case Tag.tagSymbol:
			if (!token.isOperator()) {
				// パースエラー: 演算子でない記号
				break;
			}
			char op = token.charValue();
			getNextToken();
			return parsePreOp(op);

			// 変数や関数
		case Tag.tagName:
			return parseName(null);

		case Tag.tagEnd:
			getNextToken();
			return null;

		default:
			break;
		}
		if (token.getTag() != Tag.tagSymbol || token.charValue() != ';') {
			System.out
					.println("unused token(tag=" + token.getTag() + "): " + token);
		}
		getNextToken();
		return null;
	}

	/**
	 * if(expr) { ... } と if(expr) { ... } else { ... } をパースする
	 */
	private Evaluator parseIf() {
		if (!token.equals('('))
			return null;

		// expr
		getNextToken();
		Expression expr = parseExpression();

		if (!token.equals(')'))
			return null;

		// ...
		List<Evaluator> instructions = new ArrayList<Evaluator>();
		if (getNextToken().equals('{')) {
			getNextToken();
			instructions = parseInternal();
		} else
			instructions.add(getInstruction());

		// else
		getNextToken();
		if ((token.getTag() == Tag.tagSystem)
				&& (RESERVED) token.value == RESERVED.SYS_ELSE) {
			List<Evaluator> instructionsElse = new ArrayList<Evaluator>();
			if (getNextToken().equals('{')) {
				getNextToken();
				instructionsElse = parseInternal();
			} else
				instructionsElse.add(getInstruction());
			return new IfStatement(expr, instructions, instructionsElse);
		}
		return new IfStatement(expr, instructions);
	}

	/**
	 * while(expr) { ... } をパースする
	 */
	private Evaluator parseWhile() {
		if (!token.equals('('))
			return null;

		// expr
		getNextToken();
		Expression expr = parseExpression();

		if (!token.equals(')'))
			return null;

		// ...
		List<Evaluator> instructions = new ArrayList<Evaluator>();
		if (getNextToken().equals('{')) {
			getNextToken();
			instructions = parseInternal();
		} else
			instructions.add(getInstruction());
		return new WhileStatement(expr, instructions);
	}

	/**
	 * for(a; b; c) { ... } をパースする
	 */
	private Evaluator parseFor() {
		if (!token.equals('('))
			return null;

		// a
		getNextToken();
		Expression a = parseExpression();
		if (!token.equals(';'))
			return null;

		// b
		getNextToken();
		Expression b = parseExpression();
		if (!token.equals(';'))
			return null;

		// c
		getNextToken();
		Expression c = parseExpression();
		if (token.equals(';'))
			getNextToken();

		if (!token.equals(')'))
			return null;

		// ...
		List<Evaluator> instructions = new ArrayList<Evaluator>();
		if (getNextToken().equals('{')) {
			getNextToken();
			instructions = parseInternal();
		} else
			instructions.add(getInstruction());
		return new ForStatement(a, b, c, instructions);
	}

	/**
	 * foreach(item in list except exceptions) { ... } をパースする
	 */
	private Evaluator parseForeach() {
		if (!token.equals('('))
			return null;

		// item
		getNextToken();
		String variableName = (String) token.value;

		// in
		getNextToken();
		if (token.getTag() != Tag.tagSystem
				|| !token.value.equals(RESERVED.SYS_IN))
			return null;

		// list
		getNextToken();
		String listName = (String) token.value;

		// except
		List<Expression> exceptions;
		getNextToken();
		if (token.getTag() == Tag.tagSystem
				&& token.value.equals(RESERVED.SYS_EXCEPT)) {
			getNextToken();
			exceptions = parseExpressions();
		} else {
			exceptions = null;
			if (!token.equals(')'))
				return null;
		}

		// ...
		ArrayList<Evaluator> instructions = new ArrayList<Evaluator>();
		if (getNextToken().equals('{')) {
			getNextToken();
			instructions = parseInternal();
		} else
			instructions.add(getInstruction());
		return new ForeachStatement(variableName, listName, instructions,
				exceptions);
	}

	/**
	 * funcname(param, param, ...) { ... } をパースする
	 */
	private Evaluator parseFunction() {
		List<String> arguments = new ArrayList<String>();
		List<Evaluator> instructions = new ArrayList<Evaluator>();

		// funcname
		String functionName = token.value.toString();

		// (
		if (!getNextToken().equals('('))
			return null;

		// param, param, ... )
		getNextToken();
		if (!token.equals(')'))
			while (true) {
				String name = token.value.toString();
				arguments.add(name);
				getNextToken();
				if (token.equals(')'))
					break;
				if (!token.equals(','))
					return null; // パースエラー: 区切り文字以外が検出された
				getNextToken();
			}

		// {
		if (!getNextToken().equals('{'))
			return null;

		// ... }
		getNextToken();
		instructions = parseInternal();

		// 現在のコンテクストに関数定義を追加する
		context.addFunction(new Function(functionName, arguments,
				instructions));
		return null;
	}

	/**
	 * 返値をパースする
	 */
	private Evaluator parseReturn() {
		Expression expression = parseExpression();
		return new ReturnStatement(expression);
	}

	/**
	 * print文をパースする
	 */
	private Evaluator parsePrint() {
		Expression expression = parseExpression();
		return new PrintStatement(expression);
	}

	/**
	 * リスト化された式表現をパースする
	 */
	private List<Expression> parseExpressions() {
		boolean expectsDelim = false; // 次の while ループで期待されるのがデリミタなら true
		boolean expectsElem = false; // 少なくとも一つ以上要素があれば以降 true
		// (a, b, c, ) において最後の null を渡すために必要
		List<Expression> expressions = new ArrayList<Expression>();
		while (true) {
			if (token.equals(',')) {
				if (expectsDelim)
					expectsDelim = false;
				else
					expressions.add(null);
				expectsElem = true;
				getNextToken();
				continue;
			} else if (token.equals(')')) {
				if (expectsElem && !expectsDelim)
					expressions.add(null);
				getNextToken();
				return expressions;
			}
			// パースエラー: 区切りがあるべきところに区切りがない
			if (expectsDelim) {
				return expressions;
			}

			expressions.add(parseExpression());
			expectsDelim = true;
		}
	}

	/**
	 * Parse "new" expression, variable or field access, function or method call.
	 *
	 * @param leftExpression Already parsed expression of variable or field access.
	 */
	private Expression parseName(Expression leftExpression) {

		// Object instantiation.
		if (token.getTag() == Tag.tagSystem &&
				(RESERVED) token.value == RESERVED.SYS_NEW) {

			// Get Class name
			getNextToken();
			if (token.getTag() == Tag.tagName)
			{
				final String className = token.value.toString();
				getNextToken();
				if (token.equals('('))
				{
					getNextToken();
					final Expression expr =
							new NewExpression(
									className, parseExpressions());
					/*
					if (token.equals(';'))
					{
						getNextToken();
					}
					*/
					return expr;
				}
			}
		}
		else
		{
			final String name = token.value.toString();
			getNextToken();

			// Recursive call of fields or methods.
			if (token.equals('.')) {
				getNextToken();
				Expression expr;
				if (leftExpression == null)
				{
					expr = parseName(new VariableAccess(name));
				}
				else
				{
					expr = parseName(new FieldAccess(leftExpression, name));
				}
				return expr;
			}

			// Call of a function or a method.
			else if (token.equals('(')) {
				getNextToken();
				Expression expr;
				if (leftExpression == null)
				{
					expr = new FunctionCall(name, parseExpressions());
				}
				else
				{
					expr = new MethodCall(leftExpression, name, parseExpressions());
				}
				/*
				if (token.equals(';'))
				{
					getNextToken();
				}
				*/
				return expr;
			}

			// Assignment of value into a field or a variable.
			// (Only if '=' does not appear twice continuously.)
			else if (token.equals('=') && !checkNextToken().equals('=')) {
				getNextToken();
				Expression expr;
				Expression assignExpr;

				// Anonymous function
				if (token.getTag() == Tag.tagSystem &&
						((RESERVED) token.value) == RESERVED.SYS_FUNCTION)
				{
					final String functionName = "_anon" + (anonymousFunction ++);
					token.value = functionName;
					parseFunction();
					assignExpr = new VariableAccess(functionName);
				}
				else
				{
					assignExpr = parseExpression();
				}
				if (leftExpression == null)
				{
					expr = new VariableAssign(name, assignExpr);
				}
				else
				{
					expr = new FieldAssign(leftExpression, name, assignExpr);
				}
				/*
				if (token.equals(';'))
				{
					getNextToken();
				}
				*/
				return expr;
			}

			// Access to a field or a variable
			else if (token.getTag() == Tag.tagSymbol) {
				/*
				if (token.equals(';'))
				{
					getNextToken();
				}
				*/
				if (leftExpression == null)
					return new VariableAccess(name);
				return new FieldAccess(leftExpression, name);
			}
		}

		// Parse error: unknown token
		return null;
	}

	/**
	 * Parse an expression.
	 */
	private Expression parseExpression() {
		final Expression expr = parseExprAddSub();

		if (token.isOperator())
			switch (token.charValue())
			{

			// !, !=, !==
			case '!':
				getNextToken();
				if (token.equals('=')) {
					getNextToken();
					if (token.equals('='))
						getNextToken();
				}
				// if (!...) { ... }
				if (expr == null)
					return makeOperation('!', new BooleanLiteral(true),
							parseExpression());
				// if (... != ...) { ... }
				return makeOperation('!', expr, parseExpression());

			// =, ==, ===
			case '=':
				getNextToken();
				if (token.equals('=')) {
					getNextToken();
					if (token.equals('='))
						getNextToken();
				} else if (token.equals('>')) {
					// =>
				} else if (token.equals('<')) {
					// =<
				}
				return makeOperation('=', expr, parseExpression());

			case '>':
				getNextToken();
				if (token.equals('=')) {
					// >=
				}
				return makeOperation('>', expr, parseExpression());

			case '<':
				getNextToken();
				if (token.equals('=')) {
					// <=
				}
				return makeOperation('<', expr, parseExpression());
			}

		return expr;
	}

	/**
	 * Parse expression: add or subtract
	 */
	private Expression parseExprAddSub() {
		Expression expr = parseExprMulDiv();

		while (token.equals('+') || token.equals('-')) {
			char op = token.charValue();
			getNextToken();
			expr = makeOperation(op, expr, parseExprMulDiv());
		}
		return expr;
	}

	/**
	 * Parse expression: multiply or divide
	 */
	private Expression parseExprMulDiv() {
		Expression expr = parseExprPower();

		while (token.equals('*') || token.equals('/') || token.equals('%')) {
			char op = token.charValue();
			getNextToken();
			expr = makeOperation(op, expr, parseExprPower());
		}
		return expr;
	}

	/**
	 * Parse expression: power
	 */
	private Expression parseExprPower() {
		Expression expr = parseExprUnary();

		if (token.equals('^')) {
			char op = token.charValue();
			getNextToken();
			expr = makeOperation(op, expr, parseExprPower());
		}
		return expr;
	}

	/**
	 * Parse expression: unary plus or minus
	 */
	private Expression parseExprUnary() {
		char op;
		if (token.equals('+') || token.equals('-'))
		{
			op = token.charValue();

			// 前置演算子
			if (getNextToken().equals(op))
				return parsePreOp(op);
		}

		// それ以外はopに演算子を保存して単項をパース
		else op = 0;

		Expression expr = parseExprBracket();
		if (op == '-')
			return makeOperation(op, new NumberLiteral(0), expr);
		return expr;
	}

	/**
	 * Parse expression: bracket, literal, variable or function
	 */
	private Expression parseExprBracket() {

		// bracket
		if (token.equals('(')) {
			getNextToken();
			Expression expr = parseExpression();
			if (!token.equals(')')) {
				// パースエラー: 閉じていない括弧
				return null;
			}
			getNextToken();
			return expr;
		}

		// literal
		Token tokLiteral = token;
		if (token.getTag() == Tag.tagNumber) {
			getNextToken();
			return new NumberLiteral(tokLiteral.value);
		} else if (token.getTag() == Tag.tagString) {
			getNextToken();
			return new StringLiteral(tokLiteral.value);
		} else if (token.getTag() == Tag.tagSystem) {
			if ((RESERVED) tokLiteral.value == RESERVED.SYS_NEW)
			{
				return parseName(null);
			}
			getNextToken();
			switch ((RESERVED) tokLiteral.value) {
			case SYS_TRUE:
				return new BooleanLiteral(true);
			case SYS_FALSE:
				return new BooleanLiteral(false);
			default:
				// パースエラー: 謎の予約語
				return null;
			}
		}

		// variable or function
		if (token.getTag() == Tag.tagName) {
			Expression expr = parseName(null);
			if (token.equals('+') || token.equals('-')) {
				final char op = token.charValue();

				// 後置演算子
				if (checkNextToken().equals(op)) {
					getNextToken(); // '+' を読み飛ばす
					getNextToken();
					return makeShortOp(op, expr, false);
				}
			}
			return expr;
		}

		// デリミタ、等号や不等号など
		if (token.getTag() == Tag.tagSymbol)
			return null;

		System.out.println("parse expression error: " + token);
		return null;
	}

	/**
	 * 前置演算子をパースする
	 * (一つ目の演算子は読み終わっており、 tokenには二つ目の演算子が入った状態で関数が呼ばれる)
	 */
	private Expression parsePreOp(char op) {
		if (!token.equals(op)) {
			// パースエラー: 繰り返されない前置演算子
			return null;
		}
		getNextToken();
		if (token.getTag() != Tag.tagName) {
			// パースエラー: 前置演算子のあとに変数以外
			return null;
		}
		Expression accessor = parseName(null);
		return makeShortOp(op, accessor, true);
	}

	/**
	 * 前置、後置演算子による演算を作る
	 *
	 * @param op
	 *            演算子
	 * @param accessor
	 *            演算対象の変数やフィールドへのアクセスオブジェクト
	 * @param pre
	 *            前置フラグ(前置ならtrue、後置ならfalse)
	 */
	private Expression makeShortOp(char op, Expression accessor, boolean pre) {
		// パースエラー: 演算対象がない
		if (accessor == null)
			return null;

		// Accessの中身を得て演算を作る
		if (accessor instanceof VariableAccess) {
			String name = (String) accessor.getOperand(0);
			Expression expr = new VariableAssign(name, makeOperation(op,
					accessor, new NumberLiteral(1)));
			if (pre)
				return expr;
			return makeOperation(op, expr, new NumberLiteral(-1));
		} else if (accessor instanceof FieldAccess) {
			Expression var = (Expression) accessor.getOperand(0);
			String name = (String) accessor.getOperand(1);
			// 得た中身(VariableAccessかFieldAccess)が持つフィールドに、
			// 元のFieldAccessの値と1の演算結果を代入するFieldAssign
			Expression expr = new FieldAssign(accessor, name, makeOperation(
					op, var, new NumberLiteral(1)));
			if (pre)
				return expr;
			return makeOperation(op, expr, new NumberLiteral(-1));
		}
		// パースエラー: 前置演算子のあとに関数やメソッドの呼び出しなど
		return null;
	}

	/**
	 * 演算を作る
	 */
	private Expression makeOperation(char op, Expression var1, Expression var2) {
		switch (op) {
		case '=':
			return new EqExpression(var1, var2);
		case '!':
			return new NeExpression(var1, var2);
		case '>':
			return new GtExpression(var1, var2);
		case '<':
			return new LtExpression(var1, var2);
		case '*':
			return new MulExpression(var1, var2);
		case '/':
			return new DivExpression(var1, var2);
		case '^':
			return new PowExpression(var1, var2);
		case '%':
			return new ModExpression(var1, var2);
		case '+':
			return new AddExpression(var1, var2);
		case '-':
			return new SubExpression(var1, var2);
		}
		return null;
	}
}
