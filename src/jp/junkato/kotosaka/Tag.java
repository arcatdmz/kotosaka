package jp.junkato.kotosaka;

import java.awt.Color;


/**
 * タグの値
 */
public class Tag {
  public final static int tagInteger = 0;	// 整数
  public final static int tagString = 1;	// 文字列
  public final static int tagCharacter = 2;	// 文字
  public final static int tagName = 3;		// 名前
  public final static int tagSymbol = 4;	// 記号
  public final static int tagSystem = 5;	// システム記号
  public final static int tagEnd = 10;		// 終了マーク
  private final static String[] TAG_NAMES = {
	"Integer",
	"String",
	"Character",
	"Name",
	"Symbol",
	"System",
	"",
	"",
	"",
	"",
	"End"
  };
  private final static Color[] TAG_COLORS = {
	new Color(1.0f, 0.6f, 0.6f),
	new Color(0.6f, 0.6f, 1.0f),
	new Color(0.6f, 0.6f, 1.0f),
	new Color(0.4f, 1.0f, 0.8f),
	new Color(1.0f, 0.8f, 0.4f),
	new Color(0.4f, 0.8f, 1.0f)
  };

  /**
   * タグの値を文字列に変換して返す
   * @param tag タグの値
   * @return　タグの名前
   */
  public final static String toName(int tag) {
	if (tag >= 0 && tag < TAG_NAMES.length)
	{
		return TAG_NAMES[tag];
	}
	return String.valueOf(tag);
  }

  /**
   * Return the color correspondent to the tag type.
   * @param tag Tag type
   * @return Color object
   */
  public final static Color toColor(int tag) {
	if (tag >= 0 && tag < TAG_COLORS.length)
	{
		return TAG_COLORS[tag];
	}
	return Color.black;
  }
}