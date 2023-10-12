package es.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.core.ESIterator;

public class Parser {
  private static final Map<Integer, Integer> tokenNodeMap = new HashMap<Integer, Integer>();
  static {
    tokenNodeMap.put(Token.AND, ESNode.AND);
    tokenNodeMap.put(Token.OR, ESNode.OR);
    tokenNodeMap.put(Token.PLUS, ESNode.PLUS);
    tokenNodeMap.put(Token.MINUS, ESNode.MINUS);
  }

  public ESIterator<Statement> parse(Token tk, ESIterator<Token> itr) throws Exception {
    List<Statement> list = new ArrayList<Statement>();
    while (true) {
      if (tk.getType() == Token.UFUNC) {
        list.add(parseFunction(tk, itr));
      } else {
        list.add(parseStatement(tk, itr));
      }
      if (!itr.hasNext()) {
        break;
      }
      tk = itr.next();
    }
    ESIterator<Statement> statementItr = new ESIterator<Statement>(list);
    return statementItr;
  }
  
  private Statement parseFunction(Token tk, ESIterator<Token> itr) throws Exception {
    Function function = new Function();
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing function name");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      throw new Exception("syntax error: invalid function name");
    }
    String name = (String) tk.getValue();
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing left param");
    }
    tk = itr.next();
    if (tk.getType() != Token.LPAREN) {
      throw new Exception("syntax error: missing left paren");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing right paren");
    }
    tk = itr.next();
    while (tk.getType() == Token.SYMBOL) {
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing right paren");
      }
      function.getParams().add((String) tk.getValue());
      tk = itr.next();
      if (tk.getType() == Token.RPAREN) {
        break;
      }
      if (tk.getType() != Token.COMMA) {
        throw new Exception("syntax error: unexpected token: " + tk);
      }
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing right paren");
      }
      tk = itr.next();
      if (tk.getType() != Token.SYMBOL) {
        throw new Exception("syntax error: unexpected token: " + tk);
      }
    }
    if (tk.getType() != Token.RPAREN) {
      throw new Exception("syntax error: missing right paren");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing function body");
    }
    tk = itr.next();
    if (tk.getType() != Token.LBRACE) {
      throw new Exception("syntax error: invalid function body");
    }
    return null;
  }
  
  private Statement parseStatement(Token tk, ESIterator<Token> itr) throws Exception {
    List<Token> tokens = new ArrayList<Token>();
    while (tk.getType() != Token.SEMI) {
      tokens.add(tk);
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing semi colon");
      }
      tk = itr.next();
    }
    if (tokens.size() == 0) {
      return new SimpleStatement();
    }
    Object expr;
    ESIterator<Token> itr2 = new ESIterator<Token>(tokens);
    Token tk2 = itr2.next();
    if (tk2.getType() == Token.CONST) {
      expr = parseConst(tk2, itr2);
    } else {
      expr = expression(tk2, itr2);
    }
    if (itr2.hasNext()) {
      throw new Exception("syntax error: unexpected symbol at end of line");
    }
    SimpleStatement statement = new SimpleStatement();
    statement.setExpr(expr);
    return statement;
  }

  private Object parseConst(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid const declaration");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      throw new Exception("syntax error: invalid const declaration");
    }
    String name = (String) tk.getValue();
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid const declaration");
    }
    tk = itr.next();
    if (tk.getType() != Token.ASSIGN) {
      throw new Exception("syntax error: invalid const declaration");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid const declaration");
    }
    tk = itr.next();
    Object val = expression(tk, itr);
    ESNode node = new ESNode(ESNode.ASSIGN);
    node.setLhs(new Symbol(name, null, true));
    node.setRhs(val);
    return node;
  }
  
  private Object expression(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = simpleExpression(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        return val1;
      }
      if (itr.peek().getType() == Token.AND || itr.peek().getType() == Token.OR) {
        Token tkOp = itr.next();
        if (!itr.hasNext()) {
          throw new Exception("syntax error: missing RHS");
        }
        tk = itr.next();
        Object val2 = simpleExpression(tk, itr);
        ESNode node = new ESNode(tokenNodeMap.get(tkOp.getType()));
        node.setLhs(val1);
        node.setRhs(val2);
        val1 = node;
      } else {
        break;
      }
    }
    return val1;
  }

  private Object simpleExpression(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = primary(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.PLUS || itr.peek().getType() == Token.MINUS) {
        Token tkOp = itr.next();
        if (!itr.hasNext()) {
          throw new Exception("syntax error: missing RHS");
        }
        tk = itr.next();
        Object val2 = primary(tk, itr);
        ESNode node = new ESNode(tokenNodeMap.get(tkOp.getType()));
        node.setLhs(val1);
        node.setRhs(val2);
        val1 = node;
      } else {
        break;
      }
    }
    return val1;
  }
  
  private Object primary(Token tk, ESIterator<Token> itr) throws Exception {
    if (tk.getType() == Token.INTEGER || tk.getType() == Token.REAL || tk.getType() == Token.STRING || tk.getType() == Token.BOOLEAN) {
      return tk.getValue();
    }
    if (tk.getType() == Token.PLUS) {
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing RHS");
      }
      tk = itr.next();
      Object val2 = primary(tk, itr);
      ESNode node = new ESNode(ESNode.UPLUS);
      node.setRhs(val2);
      return node;
    }
    if (tk.getType() == Token.MINUS) {
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing RHS");
      }
      tk = itr.next();
      Object val2 = primary(tk, itr);
      ESNode node = new ESNode(ESNode.UMINUS);
      node.setRhs(val2);
      return node;
    }
    if (tk.getType() == Token.NOT) {
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing RHS");
      }
      tk = itr.next();
      Object val2 = primary(tk, itr);
      ESNode node = new ESNode(ESNode.UNOT);
      node.setRhs(val2);
      return node;
    }
    if (tk.getType() == Token.SYMBOL) {
      String name = (String) tk.getValue();
      if (itr.hasNext() && itr.peek().getType() == Token.ASSIGN) {
        itr.next();
        if (!itr.hasNext()) {
          throw new Exception("syntax error: missing RHS on ASSIGN");
        }
        tk = itr.next();
        Object val = expression(tk, itr);
        ESNode node = new ESNode(ESNode.ASSIGN);
        node.setLhs(new Symbol(name));
        node.setRhs(val);
        return node;
      } else if (itr.hasNext() && itr.peek().getType() == Token.LPAREN) {
        File file = (File) tk.getFile();
        itr.next();
        if (!itr.hasNext()) {
          throw new Exception("syntax error: missing right paren");
        }
        tk = itr.next();
        FunctionCall functionCall = new FunctionCall(name);
        functionCall.setFile(file);
        while (true) {
          if (tk.getType() == Token.RPAREN) {
            break;
          }
          Object param = expression(tk, itr);
          functionCall.getParams().add(param);
          if (!itr.hasNext()) {
            throw new Exception("syntax error: missing right paren");
          }
          tk = itr.next();
          if (tk.getType() == Token.COMMA) {
            if (!itr.hasNext()) {
              throw new Exception("syntax error: missing next param");
            }
            tk = itr.next();
            if (tk.getType() == Token.RPAREN) {
              throw new Exception("syntax error: missing next param");
            }
          } else if (tk.getType() != Token.RPAREN) {
            throw new Exception("syntax error: missing right param");
          }
        }
        return functionCall;
      } else {
        return new Symbol(name);
      }
    }
    
    throw new Exception("unsupported primary expression: " + tk);
  }
}
