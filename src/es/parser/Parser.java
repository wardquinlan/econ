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
    tokenNodeMap.put(Token.LT, ESNode.LT);
    tokenNodeMap.put(Token.GT, ESNode.GT);
    tokenNodeMap.put(Token.LTE, ESNode.LTE);
    tokenNodeMap.put(Token.GTE, ESNode.GTE);
    tokenNodeMap.put(Token.EQ, ESNode.EQ);
    tokenNodeMap.put(Token.NE, ESNode.NE);
    tokenNodeMap.put(Token.MULT, ESNode.MULT);
    tokenNodeMap.put(Token.DIV, ESNode.DIV);
    tokenNodeMap.put(Token.EXP, ESNode.EXP);
  }
  // just for checks of existing functions, does not actually invoke the function from here...
  private FunctionCaller functionCaller = new FunctionCaller();

  public ESIterator<Statement> parse(Token tk, ESIterator<Token> itr) throws Exception {
    List<Statement> list = new ArrayList<Statement>();
    while (true) {
      parseStatement(list, tk, itr);
      if (!itr.hasNext()) {
        break;
      }
      tk = itr.next();
    }
    ESIterator<Statement> statementItr = new ESIterator<Statement>(list);
    return statementItr;
  }
  
  private void parseStatement(List<Statement> list, Token tk, ESIterator<Token> itr) throws Exception {
    if (tk.getType() == Token.FUNCDECL) {
      list.add(parseFunction(tk, itr));
    } else if (tk.getType() == Token.RETURN) {
      list.add(parseReturn(tk, itr));
    } else if (tk.getType() == Token.THROW) {
      list.add(parseThrow(tk, itr));
    } else if (tk.getType() == Token.IF) {
      list.add(parseIf(tk, itr));
    } else if (tk.getType() == Token.TRY) {
      Statement st = parseTry(tk, itr);
      System.out.println(st);
      list.add(st);
      //list.add(parseTry(tk, itr));
    } else {
      list.add(parseSimpleStatement(tk, itr));
    }
  }
  
  private void parseBlock(List<Statement> list, Token tk, ESIterator<Token> itr) throws Exception {
    if (tk.getType() == Token.LBRACE) {
      while (true) {
        if (!itr.hasNext()) {
          throw new Exception("syntax error: invalid block");
        }
        tk = itr.next();
        if (tk.getType() == Token.RBRACE) {
          break;
        }
        parseStatement(list, tk, itr);
      }
    } else {
      parseStatement(list, tk, itr);
    }
  }
  
  private Statement parseTry(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing try opening brace");
    }
    tk = itr.next();
    if (tk.getType() != Token.LBRACE) {
      throw new Exception("syntax error: missing try opening brace");
    }
    List<Statement> listTry = new ArrayList<>();
    parseBlock(listTry, tk, itr);
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing catch clause");
    }
    tk = itr.next();
    if (tk.getType() != Token.CATCH) {
      throw new Exception("syntax error: missing catch clause");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing exception");
    }
    tk = itr.next();
    if (tk.getType() != Token.LPAREN) {
      throw new Exception("syntax error: missing exception");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing exception");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      throw new Exception("syntax error: invalid exception");
    }
    String ex = (String) tk.getValue();
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid exception");
    }
    tk = itr.next();
    if (tk.getType() != Token.RPAREN) {
      throw new Exception("syntax error: invalid exception");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing catch opening brace");
    }
    tk = itr.next();
    if (tk.getType() != Token.LBRACE) {
      throw new Exception("syntax error: missing catch opening brace");
    }
    List<Statement> listCatch = new ArrayList<>();
    parseBlock(listCatch, tk, itr);
    TryCatchStatement statement = new TryCatchStatement(ex);
    statement.getTryBody().addAll(listTry);
    statement.getCatchBody().addAll(listCatch);
    return statement;
  }
  
  private Statement parseIf(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing predicate");
    }
    tk = itr.next();
    if (tk.getType() != Token.LPAREN) {
      throw new Exception("syntax error: invalid predicate");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid predicate");
    }
    tk = itr.next();
    Object predicate = expression(tk, itr);
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid predicate");
    }
    tk = itr.next();
    if (tk.getType() != Token.RPAREN) {
      throw new Exception("syntax error: invalid predicate");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: invalid if body");
    }
    tk = itr.next();
    IfStatement statement = new IfStatement(predicate);
    parseBlock(statement.getIfBody(), tk, itr);
    
    if (itr.hasNext() && itr.peek().getType() == Token.ELSE) {
      itr.next();
      if (!itr.hasNext()) {
        throw new Exception("syntax error: invalid else body");
      }
      tk = itr.next();
      parseBlock(statement.getElseBody(), tk, itr);
    }
    return statement;
  }
  
  private Statement parseFunction(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing function name");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      throw new Exception("syntax error: invalid function name");
    }
    String name = (String) tk.getValue();
    if (functionCaller.isFunction(name)) {
      throw new Exception("syntax error: invalid function name (use of a built-in function name)");
    }
    FunctionDeclaration functionDeclaration = new FunctionDeclaration(name);
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
      functionDeclaration.getParams().add((String) tk.getValue());
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
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing right brace");
    }
    tk = itr.next();
    while (true) {
      if (tk.getType() == Token.RBRACE) {
        break;
      }
      parseStatement(functionDeclaration.getStatements(), tk, itr);
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing right brace");
      }
      tk = itr.next();
    }
    return functionDeclaration;
  }
  
  private Statement parseReturn(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing semi colon");
    }
    tk = itr.next();
    List<Token> tokens = new ArrayList<Token>();
    while (tk.getType() != Token.SEMI) {
      tokens.add(tk);
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing semi colon");
      }
      tk = itr.next();
    }
    if (tokens.size() == 0) {
      return new ReturnStatement();
    }
    ESIterator<Token> itr2 = new ESIterator<Token>(tokens);
    Token tk2 = itr2.next();
    Object expr = expression(tk2, itr2);
    if (itr2.hasNext()) {
      throw new Exception("syntax error: unexpected symbol at end of line");
    }
    return new ReturnStatement(expr);
  }

  private Statement parseThrow(Token tk, ESIterator<Token> itr) throws Exception {
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing semi colon");
    }
    tk = itr.next();
    List<Token> tokens = new ArrayList<Token>();
    while (tk.getType() != Token.SEMI) {
      tokens.add(tk);
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing semi colon");
      }
      tk = itr.next();
    }
    if (tokens.size() == 0) {
      throw new Exception("syntax error: missing throw expression");
    }
    ESIterator<Token> itr2 = new ESIterator<Token>(tokens);
    Token tk2 = itr2.next();
    Object expr = expression(tk2, itr2);
    if (itr2.hasNext()) {
      throw new Exception("syntax error: unexpected symbol at end of line");
    }
    return new ThrowStatement(expr);
  }
  
  private Statement parseSimpleStatement(Token tk, ESIterator<Token> itr) throws Exception {
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
    return new SimpleStatement(expr);
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
    Object val1 = expressionL2(tk, itr);
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
        Object val2 = expressionL2(tk, itr);
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
  
  private Object expressionL2(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = simpleExpression(tk, itr);
    if (!itr.hasNext()) {
      return val1;
    }
    
    // NOTE: could implement LT, LTE, GT, GTE for Strings, too (using String.compareTo())
    // NOTE: all of these are the same level of precedence.  This differs from Java -
    //       see https://introcs.cs.princeton.edu/java/11precedence/
    //
    //       We could probably put LT, LTE, GT and GTE into a 'expressionL3()' which I think would fix this.
    // NOTE: My implementation of AND and OR is also slightly different from Java; see above web site.
    if (itr.peek().getType() == Token.EQ  || 
        itr.peek().getType() == Token.NE  ||
        itr.peek().getType() == Token.LT  ||
        itr.peek().getType() == Token.LTE ||
        itr.peek().getType() == Token.GT  ||
        itr.peek().getType() == Token.GTE) {
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
    }
    return val1;
  }

  private Object simpleExpression(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = term(tk, itr);
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
        Object val2 = term(tk, itr);
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
  
  private Object term(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = exp(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.MULT || itr.peek().getType() == Token.DIV) {
        Token tkOp = itr.next();
        if (!itr.hasNext()) {
          throw new Exception("syntax error: missing RHS");
        }
        tk = itr.next();
        Object val2 = exp(tk, itr);
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

  private Object exp(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = primary(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.EXP) {
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
    if (tk.getType() == Token.INTEGER || tk.getType() == Token.REAL || tk.getType() == Token.STRING || tk.getType() == Token.BOOLEAN || tk.getType() == Token.NULL) {
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
    if (tk.getType() == Token.LPAREN) {
      tk = itr.next();
      Object val = expression(tk, itr);
      if (!itr.hasNext()) {
        throw new Exception("syntax error: unmatched lparen");
      }
      tk = itr.next();
      if (tk.getType() != Token.RPAREN) {
        throw new Exception("syntax error: unmatched lparen");
      }
      return val;
    }
    
    throw new Exception("unsupported primary expression: " + tk);
  }
}
