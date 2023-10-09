package es.parser;

import java.util.ArrayList;
import java.util.List;

import es.tokenizer.Token;

public class Statement {
  private List<Token> list = new ArrayList<Token>();
  
  public List<Token> getTokens() {
    return list;
  }
  
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("STATEMENT=[");
    for (int i = 0; i < list.size(); i++) {
      Token tk = list.get(i);
      sb.append(tk);
      if (i != list.size() - 1) {
        sb.append(",");
      }
    }
    sb.append("]");
    return sb.toString();
  }
}
