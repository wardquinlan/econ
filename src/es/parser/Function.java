package es.parser;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class Function {
  private List<String> params = new ArrayList<String>(); 
  private List<Token> tokenList = new ArrayList<Token>();

  public List<String> getParams() {
    return params;
  }
  public List<Token> getTokenList() {
    return tokenList;
  }
}
