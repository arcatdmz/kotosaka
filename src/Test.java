import java.util.ArrayList;
import java.util.Queue;

import jp.junkato.kotosaka.Char;
import jp.junkato.kotosaka.Context;
import jp.junkato.kotosaka.KotosakaParser;
import jp.junkato.kotosaka.KotosakaReader;
import jp.junkato.kotosaka.Token;


public class Test {

	public static void main(String[] args) {
		String script = "print (1+2*3-4)%5*6/7; test = 8; print test + 3;";

		ArrayList<Char> characters = new ArrayList<Char>();
		for (int i = 0; i < script.length(); i ++) {
			characters.add(new Char(script.charAt(i)));
		}

		KotosakaReader reader = new KotosakaReader();
		Queue<Token> tokens = reader.read(characters);

		KotosakaParser parser = new KotosakaParser();
		Context context = parser.parse(tokens);

		context.run();
	}

}
