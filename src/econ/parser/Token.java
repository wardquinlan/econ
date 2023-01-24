package econ.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Token {
  public static final int SYMBOL   = 0;  // some_symbol
  public static final int INTEGER  = 1;  // 10
  public static final int REAL     = 2;  // 3.14159
  public static final int STRING   = 3;  // "hello"
  public static final int LPAREN   = 4;  // (
  public static final int RPAREN   = 5;  // )
  public static final int LBRACE   = 6;  // {
  public static final int RBRACE   = 7;  // }
  public static final int ASSIGN   = 8;  // =
  public static final int EQ       = 9;  // ==
  public static final int NE       = 10;  // !=
  public static final int LT       = 11; // <
  public static final int LTE      = 12; // <=
  public static final int GT       = 13; // >
  public static final int GTE      = 14; // >=
  public static final int SEMI     = 15; // ;
  public static final int COMMA    = 16; // ,
  public static final int COMMENT  = 17; // --
  public static final int PLUS     = 18; // +
  public static final int MINUS    = 19; // - 
  public static final int MULT     = 20; // *
  public static final int DIV      = 21; // /
  public static final int EXP      = 22; // ^
  public static final int INC      = 23; // include
  public static final int FUNC     = 24; // function()
  public static final int CONST    = 25; // const
  public static final int AND      = 26; // &&
  public static final int OR       = 27; // ||
  
  public static Map<Integer, String> map = new HashMap<Integer, String>();
  static {
    map.put(SYMBOL,  "SYMBOL");
    map.put(INTEGER, "INTEGER");
    map.put(REAL,    "REAL");
    map.put(STRING,  "STRING");
    map.put(LPAREN,  "LPAREN");
    map.put(RPAREN,  "RPAREN");
    map.put(LBRACE,  "LBRACE");
    map.put(RBRACE,  "RBRACE");
    map.put(ASSIGN,  "ASSIGN");
    map.put(EQ,      "EQUAL");
    map.put(NE,      "NE");
    map.put(LT,      "LT");
    map.put(LTE,     "LTE");
    map.put(GT,      "GT");
    map.put(GTE,     "GTE");
    map.put(SEMI,    "SEMI");
    map.put(COMMA,   "COMMA");
    map.put(COMMENT, "COMMENT");
    map.put(PLUS,    "PLUS");
    map.put(MINUS,   "MINUS");
    map.put(MULT,    "MULT");
    map.put(DIV,     "DIV");
    map.put(EXP,     "EXP");
    map.put(INC,     "INC");
    map.put(FUNC,    "FUNC");
    map.put(CONST,   "CONST");
  }
  
  private int type;
  private Object value;
  private File file; // file which created this token
	
  public Token(int type) {
    this.type = type;
  }
	
  public int getType() {
    return type;
  }
  
  public Object getValue() {
    return value;
  }
  
  public void setValue(Object object) {
    value = object;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public String toString() {
    return map.get(type) + (value == null ? "" : "=" + value);
  }
}
