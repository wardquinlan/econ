package econ;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Tokenizer {
  private static Log log = LogFactory.getFactory().getInstance(Tokenizer.class);
  private File file;
  private int level;
  private String basename;
  private LookAheadReader rdr;
  private FunctionCaller functionCaller = new FunctionCaller();
  private static final int MAX_LEVEL = 8;
  
  public Tokenizer(InputStream inputStream) throws Exception {
    this.basename = System.getProperty("user.dir");
    this.file = new File(basename + File.separator + "null");
    this.level = 0;
    rdr = new LookAheadReader(inputStream);
  }
  
  public Tokenizer(File file, int level) throws Exception {
    if (level > MAX_LEVEL) {
      throw new Exception("exceeds maximum include level");
    }
    
    this.file = file;
    this.level = level;
    this.basename = Paths.get(file.getAbsolutePath()).getParent().toString();
    rdr = new LookAheadReader(new FileInputStream(file));
  }
  
  public TokenIterator tokenize() throws Exception {
    List<Token> list = new ArrayList<Token>();
    try {
      while (true) {
        int val = rdr.read();
        if (val == -1) {
          break;
        }
        if (val == '-' && rdr.peek() == '-') {
          while (true) {
            val = rdr.read();
            if (val == -1 || val == 0x0a) {
              break;
            }
          }
        } else if (Character.isLetter(val)) {
          Token tk = null;
          StringBuffer sb = new StringBuffer();
          sb.append((char) val);
          while (Character.isLetter(rdr.peek()) || Character.isDigit(rdr.peek()) || rdr.peek() == '_' || rdr.peek() == '.') {
            sb.append((char) rdr.read());
          }
          if (sb.toString().equals("include")) {
            tk = new Token(Token.INC);
          } else if (sb.toString().equals("const")) {
            tk = new Token(Token.CONST);
          } else if (functionCaller.isFunction(sb.toString())) {
            tk = new Token(Token.FUNC);
            tk.setFile(file); // set the file so that the XML parser is able to calculate the relative path
          } else {
            tk = new Token(Token.SYMBOL);
          }
          tk.setValue(sb.toString());
          list.add(tk);
        } else if (val == '.' || Character.isDigit(val)) {
          StringBuffer sb = new StringBuffer();
          sb.append((char) val);
          while (rdr.peek() == '.' || Character.isDigit(rdr.peek())) {
            if (rdr.peek() == '.' && sb.toString().contains(".")) {
              throw new Exception("misformatted value: " + sb);
            }
            sb.append((char) rdr.read());
          }
          if (sb.toString().equals(".")) {
            throw new Exception("misformatted value: " + sb);
          }
          if (sb.toString().contains(".")) {
            Token tk = new Token(Token.REAL);
            tk.setValue(Float.parseFloat(sb.toString()));
            list.add(tk);
          } else {
            Token tk = new Token(Token.INTEGER);
            tk.setValue(Integer.parseInt(sb.toString()));
            list.add(tk);
          }
        } else if (val == '#') {
          Token tk = new Token(Token.INTEGER);
          StringBuffer sb = new StringBuffer();
          while (Character.isDigit(rdr.peek()) || (rdr.peek() >= 'a' && rdr.peek() <= 'f') || (rdr.peek() >= 'A' && rdr.peek() <= 'F')) {
            sb.append((char) rdr.read());
          }
          if (sb.toString().equals("")) {
            throw new Exception("invalid hex value");
          }
          tk.setValue(Integer.parseInt(sb.toString(), 16));
          list.add(tk);
        } else if (val == '"') {
          Token tk = new Token(Token.STRING);
          StringBuffer sb = new StringBuffer();
          while (true) {
            if (rdr.peek() == '"') {
              rdr.read();
              break;
            }
            if (rdr.peek() == -1) {
              throw new Exception("unterminated string: " + sb);
            }
            if (rdr.peek() == '\\') {
              rdr.read();
              if (rdr.peek() == -1) {
                throw new Exception("unterminated string: " + sb);
              }
            }
            sb.append((char) rdr.read());
          }
          tk.setValue(sb.toString());
          list.add(tk);
        } else if (val == '=' && rdr.peek() == '=') {
          Token tk = new Token(Token.EQ);
          list.add(tk);
        } else if (val == '!' && rdr.peek() == '=') {
          Token tk = new Token(Token.NE);
          list.add(tk);
        } else if (val == '<' && rdr.peek() == '=') {
          Token tk = new Token(Token.LTE);
          list.add(tk);
        } else if (val == '>' && rdr.peek() == '=') {
          Token tk = new Token(Token.GTE);
          list.add(tk);
        } else if (val == '^') {
          Token tk = new Token(Token.EXP);
          list.add(tk);
        } else if (val == '(') {
          Token tk = new Token(Token.LPAREN);
          list.add(tk);
        } else if (val == ')') {
          Token tk = new Token(Token.RPAREN);
          list.add(tk);
        } else if (val == '{') {
          Token tk = new Token(Token.LBRACE);
          list.add(tk);
        } else if (val == '}') {
          Token tk = new Token(Token.RBRACE);
          list.add(tk);
        } else if (val == '=') {
          Token tk = new Token(Token.ASSIGN);
          list.add(tk);
        } else if (val == '<') {
          Token tk = new Token(Token.LT);
          list.add(tk);
        } else if (val == '>') {
          Token tk = new Token(Token.GT);
          list.add(tk);
        } else if (val == ';') {
          Token tk = new Token(Token.SEMI);
          list.add(tk);
        } else if (val == ',') {
          Token tk = new Token(Token.COMMA);
          list.add(tk);
        } else if (val == '+') {
          Token tk = new Token(Token.PLUS);
          list.add(tk);
        } else if (val == '-') {
          Token tk = new Token(Token.MINUS);
          list.add(tk);
        } else if (val == '*') {
          Token tk = new Token(Token.MULT);
          list.add(tk);
        } else if (val == '/') {
          Token tk = new Token(Token.DIV);
          list.add(tk);
        }
      }
    } finally {
      if (rdr != null) {
        try {
          rdr.close();
        } catch(IOException e) {
          log.warn("cannot close reader", e);
        }
      }
    }
    return postTokenize(list);
  }
  
  private TokenIterator postTokenize(List<Token> list) throws Exception {
    List<Token> listNew = new ArrayList<Token>();
    for (int i = 0; i < list.size(); i++) {
      Token tk = list.get(i);
      if (tk.getType() == Token.INC) {
        i++;
        if (i == list.size()) {
          throw new Exception("misformatted include statement");
        }
        tk = list.get(i);
        if (tk.getType() != Token.STRING) {
          throw new Exception("misformatted include statement");
        }
        String filename = (String) tk.getValue();
        i++;
        if (i == list.size()) {
          throw new Exception("misformatted include statement");
        }
        tk = list.get(i);
        if (tk.getType() != Token.SEMI) {
          throw new Exception("misformatted include statement");
        }
        Tokenizer tokenizer;
        if (Paths.get(filename).isAbsolute()) {
          tokenizer = new Tokenizer(new File(filename), level + 1);
        } else {
          tokenizer = new Tokenizer(new File(basename + File.separator + filename), level + 1);
        }
        TokenIterator itr = tokenizer.tokenize();
        while (itr.hasNext()) {
          listNew.add(itr.next());
        }
      } else {
        listNew.add(tk);
      }
    }
    return new TokenIterator(listNew);
  }
}
