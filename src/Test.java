import java.util.ArrayList;
import java.util.Queue;

import jp.junkato.kotosaka.Char;
import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.KotosakaParser;
import jp.junkato.kotosaka.KotosakaReader;
import jp.junkato.kotosaka.Token;


public class Test {

	public static void main(String[] args) {
		StringBuilder script = new StringBuilder();
		script.append("a = (1 + 2 * 3 - 4) % 5 * 6 / 7;");
		script.append("print \"a = \" + a;");
		script.append("A = [1, 2, 3, a];");
		script.append("print \"A =\n\" + A;");
		script.append("print \"A.transpose() =\n\" + A.transpose();");
		script.append("B = [1, 2, 3, 4];");
		script.append("print \"B =\n\" + B;");
		script.append("print \"A.mul(B) =\n\" + A.mul(B);");
		script.append("print \"A * B =\n\" + A * B;");
		script.append("print \"A * B == A.mul(B) ? \" + (A * B == A.mul(B));");

		ArrayList<Char> characters = new ArrayList<Char>();
		for (int i = 0; i < script.length(); i ++) {
			characters.add(new Char(script.charAt(i)));
		}
		script.setLength(0);

		KotosakaReader reader = new KotosakaReader();
		Queue<Token> tokens = reader.read(characters);

		KotosakaParser parser = new KotosakaParser();
		Context context = parser.parse(tokens);

		context.run();
	}

}
