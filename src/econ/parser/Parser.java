package econ.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import econ.core.Utils;

public class Parser {
  private static final Log log = LogFactory.getFactory().getInstance(Parser.class);
  private static final Map<Integer, Operator> operatorMap = new HashMap<>();
  static {
    operatorMap.put(Token.PLUS, new Plus());
    operatorMap.put(Token.MINUS, new Minus());
    operatorMap.put(Token.MULT, new Mult());
    operatorMap.put(Token.DIV, new Div());
    operatorMap.put(Token.EXP, new Exp());
    operatorMap.put(Token.AND, new And());
    operatorMap.put(Token.OR, new Or());
    operatorMap.put(Token.EQ, new Eq());
    operatorMap.put(Token.NE, new Ne());
    operatorMap.put(Token.LT, new Lt());
    operatorMap.put(Token.LTE, new Lte());
    operatorMap.put(Token.GT, new Gt());
    operatorMap.put(Token.GTE, new Gte());
  }
  private static final UnaryOperator uPlus = new UPlus();
  private static final UnaryOperator uMinus = new UMinus();
  private static final UnaryOperator uNot = new UNot();
  private FunctionCaller functionCaller = new FunctionCaller();
  private Map<String, Symbol> symbolTable;

  public Parser(Map<String, Symbol> symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public Map<String, Symbol> parse(Token tk, TokenIterator itr) throws Exception {
    while (true) {
      parseStatement(tk, itr);
      if (!itr.hasNext()) {
        break;
      }
      tk = itr.next();
    }
    return symbolTable;
  }
  
  private void parseStatement(Token tk, TokenIterator itr) throws Exception {
    Statement statement = new Statement();
    while (true) {
      if (tk.getType() == Token.SEMI) {
        break;
      }
      statement.getTokens().add(tk);
      if (!itr.hasNext()) {
        log.error("missing semi colon");
        throw new Exception("syntax error");
      }
      tk = itr.next();
    }
    if (statement.getTokens().size() == 0) {
      return;
    }
    TokenIterator itr2 = new TokenIterator(statement.getTokens());
    Token tk2 = itr2.next();
    if (tk2.getType() == Token.CONST) {
      parseDeclaration(tk2, itr2);
    } else {
      expression(tk2, itr2);
    }
    if (itr2.hasNext()) {
      log.error("unexpected symbol at end of line");
      throw new Exception("syntax error");
    }
  }
  
  private void parseDeclaration(Token tk, TokenIterator itr) throws Exception {
    if (!itr.hasNext()) {
      log.error("invalid const declaration");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      log.error("invalid const declaration");
      throw new Exception("syntax error");
    }
    String symbolName = (String) tk.getValue();
    if (!itr.hasNext()) {
      log.error("invalid const declaration");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    if (tk.getType() != Token.ASSIGN) {
      log.error("invalid const declaration");
      throw new Exception("syntax error");
    }
    if (!itr.hasNext()) {
      log.error("invalid const declaration");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    Object val = expression(tk, itr);
    if (symbolTable.get(symbolName) != null) {
      throw new Exception("symbol already defined: " + symbolName);
    }
    symbolTable.put(symbolName, new Symbol(val, true));
  }
  
  private Object expression(Token tk, TokenIterator itr) throws Exception {
    Object val1 = expressionL2(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        return val1;
      }
      if (itr.peek().getType() == Token.AND || itr.peek().getType() == Token.OR) {
        Executor executor = new Executor(operatorMap.get(itr.peek().getType()));
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val2 = expressionL2(tk, itr);
        val1 = executor.exec(val1, val2);
      } else {
        break;
      }
    }
    return val1;
  }
  
  private Object expressionL2(Token tk, TokenIterator itr) throws Exception {
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
      Executor executor = new Executor(operatorMap.get(itr.peek().getType()));
      itr.next();
      if (!itr.hasNext()) {
        log.error("missing RHS");
        throw new Exception("syntax error");
      }
      tk = itr.next();
      Object val2 = simpleExpression(tk, itr);
      val1 = executor.exec(val1, val2);
    }
    return val1;
  }
  
  private Object simpleExpression(Token tk, TokenIterator itr) throws Exception {
    Object val1 = term(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.PLUS || itr.peek().getType() == Token.MINUS) {
        Executor executor = new Executor(operatorMap.get(itr.peek().getType()));
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val2 = term(tk, itr);
        val1 = executor.exec(val1, val2);
      } else {
        break;
      }
    }
    return val1;
  }
  
  private Object term(Token tk, TokenIterator itr) throws Exception {
    Object val1 = exp(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.MULT || itr.peek().getType() == Token.DIV) {
        Executor executor = new Executor(operatorMap.get(itr.peek().getType()));
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val2 = exp(tk, itr);
        val1 = executor.exec(val1, val2);
      } else {
        break;
      }
    }
    return val1;
  }

  private Object exp(Token tk, TokenIterator itr) throws Exception {
    Object val1 = primary(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        break;
      }
      if (itr.peek().getType() == Token.EXP) {
        Executor executor = new Executor(operatorMap.get(itr.peek().getType()));
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val2 = primary(tk, itr);
        val1 = executor.exec(val1, val2);
      } else {
        break;
      }
    }
    return val1;
  }
  
  private Object primary(Token tk, TokenIterator itr) throws Exception {
    if (tk.getType() == Token.INTEGER || tk.getType() == Token.REAL || tk.getType() == Token.STRING || tk.getType() == Token.BOOLEAN) {
      return tk.getValue();
    }
    // NOTE: could probably implement NOT here if needed
    if (tk.getType() == Token.PLUS) {
      Executor executor = new Executor(uPlus);
      tk = itr.next();
      Object val = primary(tk, itr);
      if (!(val instanceof Integer) && !(val instanceof Float)) {
        log.error("invalid unary plus");
        throw new Exception("syntax error");
      }
      return val;
    }
    if (tk.getType() == Token.MINUS) {
      Executor executor = new Executor(uMinus);
      tk = itr.next();
      Object val = primary(tk, itr);
      if (val instanceof Integer) {
        return new Integer(-(Integer) val);
      } else if (val instanceof Float) {
        return new Float(-(Float) val);
      } else {
        log.error("invalid unary minus");
        throw new Exception("syntax error");
      }
    }
    if (tk.getType() == Token.NOT) {
      Executor executor = new Executor(uNot);
      tk = itr.next();
      Object val = primary(tk, itr);
      if (val instanceof Boolean) {
        return !((Boolean) val);
      } else {
        log.error("invalid not");
        throw new Exception("syntax error");
      }
    }
    if (tk.getType() == Token.SYMBOL) {
      String symbolName = (String) tk.getValue();
      if (itr.hasNext() && itr.peek().getType() == Token.ASSIGN) {
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS on ASSIGN");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val = expression(tk, itr);
        if (val == null) {
          throw new Exception("cannot assign null value to " + symbolName);
        }
        Symbol symbol = symbolTable.get(symbolName);
        if (symbol != null && symbol.isConstant()) {
          throw new Exception("cannot write to a const: " + symbolName);
        }
        symbolTable.put(symbolName, new Symbol(val));
      }
       
      Symbol symbol = symbolTable.get(symbolName);
      if (symbol == null) {
        throw new Exception("uninitialized symbol: " + tk.getValue());
      }
      return symbol.getValue();
    }
    if (tk.getType() == Token.LPAREN) {
      tk = itr.next();
      Object val = expression(tk, itr);
      if (!itr.hasNext()) {
        log.error("missing RPAREN (end of line)");
        throw new Exception("syntax error: unmatched lparen");
      }
      tk = itr.next();
      if (tk.getType() != Token.RPAREN) {
        log.error("missing RPAREN (unexpected token)");
        throw new Exception("syntax error: unmatched lparen");
      }
      return val;
    }
    if (tk.getType() == Token.FUNC) {
      String funcName = (String) tk.getValue();
      if (!itr.hasNext()) {
        log.error("unexpected end of input: " + funcName);
        throw new Exception("syntax error: " + funcName);
      }
      File file = tk.getFile();
      tk = itr.next();
      if (tk.getType() != Token.LPAREN) {
        log.error("expecting left parenthesis: " + funcName);
        throw new Exception("syntax error: " + funcName);
      }
      tk = itr.next();
      List<Object> params = new ArrayList<Object>();
      while (tk.getType() != Token.RPAREN) {
        Object val = expression(tk, itr);
        params.add(val);
        if (!itr.hasNext()) {
          log.error("unexpected end of input: " + funcName);
          throw new Exception("syntax error: " + funcName);
        }
        tk = itr.next();
        if (tk.getType() == Token.COMMA) {
          if (!itr.hasNext()) {
            log.error("unexpected end of input: " + funcName);
            throw new Exception("syntax error: " + funcName);
          }
          tk = itr.next();
          if (tk.getType() == Token.RPAREN) {
            log.error("unexpected comma: " + funcName);
            throw new Exception("syntax error: " + funcName);
          }
        }
      }
      Utils.ASSERT(file != null, "file is null");
      return functionCaller.invokeFunction(funcName, symbolTable, file, params);
    }
    throw new Exception("unsupported primary expression: " + tk);
  }
}
