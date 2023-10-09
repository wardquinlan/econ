package es.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.core.TimeSeries;
import es.core.TimeSeriesData;
import es.core.Utils;
import es.evaluator.And;
import es.evaluator.Div;
import es.evaluator.Eq;
import es.evaluator.Executor;
import es.evaluator.Exp;
import es.evaluator.Gt;
import es.evaluator.Gte;
import es.evaluator.Lt;
import es.evaluator.Lte;
import es.evaluator.Minus;
import es.evaluator.Mult;
import es.evaluator.Ne;
import es.evaluator.Operator;
import es.evaluator.Or;
import es.evaluator.Plus;
import es.evaluator.UMinus;
import es.evaluator.UNot;
import es.evaluator.UPlus;
import es.evaluator.UnaryOperator;

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
  private SymbolTable symbolTable;

  public Parser(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }
  
  public ReturnResult parse(Token tk, TokenIterator itr) throws Exception {
    while (true) {
      if (tk.getType() == Token.UFUNC) {
        parseFunction(tk, itr);
      } else if (tk.getType() == Token.IF) {
        ReturnResult returnResult = parseIf(tk, itr);
        if (returnResult != null) {
          // need to terminate if the if statement returned
          return returnResult;
        }
        // otherwise, keep going
      } else if (tk.getType() == Token.THROW) {
        if (!itr.hasNext()) {
          throw new Exception("missing string in throw clause");
        }
        tk = itr.next();
        Object message = parseStatement(tk, itr);
        if (!(message instanceof String)) {
          throw new Exception("throw clause must throw a string: " + message);
        }
        throw new Exception((String) message);
      } else if (tk.getType() == Token.RETURN) {
        tk = itr.next();
        return new ReturnResult(parseStatement(tk, itr));
      } else {
        parseStatement(tk, itr);
      }
      if (!itr.hasNext()) {
        break;
      }
      tk = itr.next();
    }
    return null;
  }
  
  public boolean isGlobalScope() {
    return symbolTable.getParent() == null;
  }

  @SuppressWarnings("unchecked")
  private ReturnResult parseIf(Token tk, TokenIterator itr) throws Exception {
    ReturnResult returnResult = null;
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing expression");
    }
    tk = itr.next();
    if (tk.getType() != Token.LPAREN) {
      throw new Exception("syntax error: missing expression");
    }
    tk = itr.next();
    Object expr = expression(tk, itr);
    if (!(expr instanceof Boolean)) {
      throw new Exception("syntax error: expression evaluated to non-Boolean result inside if statement");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing right paren");
    }
    tk = itr.next();
    if (tk.getType() != Token.RPAREN) {
      throw new Exception("syntax error: missing right paren");
    }
    if (!itr.hasNext()) {
      throw new Exception("syntax error: missing if body");
    }
    tk = itr.next();
    if (tk.getType() != Token.BLOCK) {
      throw new Exception("syntax error: missing if body");
    }
    if ((Boolean) expr && ((List<Token>) tk.getValue()).size() > 0) {
      SymbolTable childSymbolTable = new SymbolTable(symbolTable);
      Parser parser = new Parser(childSymbolTable);
      TokenIterator itr2 = new TokenIterator((List<Token>) tk.getValue());
      Token tk2 = itr2.next();
      returnResult = parser.parse(tk2, itr2);
    }
    if (!itr.hasNext()) {
      return returnResult;
    }
    if (itr.peek().getType() == Token.ELSE) {
      itr.next();
      if (!itr.hasNext()) {
        throw new Exception("syntax error: missing else body");
      }
      tk = itr.next();
      if (tk.getType() != Token.BLOCK) {
        throw new Exception("syntax error: missing else body");
      }
      if (! (Boolean) expr  && ((List<Token>) tk.getValue()).size() > 0) {
        SymbolTable childSymbolTable = new SymbolTable(symbolTable);
        Parser parser = new Parser(childSymbolTable);
        TokenIterator itr2 = new TokenIterator((List<Token>) tk.getValue());
        Token tk2 = itr2.next();
        returnResult = parser.parse(tk2, itr2);
      }
    }
    return returnResult;
  }
  
  @SuppressWarnings("unchecked")
  private void parseFunction(Token tk, TokenIterator itr) throws Exception {
    Function function = new Function();
    /* do this later
    if (!isGlobalScope()) {
      log.error("can only define a function in the global scope");
      throw new Exception("syntax error");
    }
    */
    if (!itr.hasNext()) {
      log.error("missing function name");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    if (tk.getType() != Token.SYMBOL) {
      log.error("invalid function name");
      throw new Exception("syntax error");
    }
    String functionName = (String) tk.getValue();
    if (!itr.hasNext()) {
      log.error("missing left param");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    if (tk.getType() != Token.LPAREN) {
      log.error("missing left paren");
      throw new Exception("syntax error");
    }
    if (!itr.hasNext()) {
      log.error("missing right paren");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    while (tk.getType() == Token.SYMBOL) {
      if (!itr.hasNext()) {
        log.error("missing right paren");
        throw new Exception("syntax error");
      }
      function.getParams().add((String) tk.getValue());
      tk = itr.next();
      if (tk.getType() == Token.RPAREN) {
        break;
      }
      if (tk.getType() != Token.COMMA) {
        log.error("unexpected token: " + tk);
        throw new Exception("syntax error");
      }
      if (!itr.hasNext()) {
        log.error("missing right paren");
        throw new Exception("syntax error");
      }
      tk = itr.next();
      if (tk.getType() != Token.SYMBOL) {
        log.error("unexpected token: " + tk);
        throw new Exception("syntax error");
      }
    }
    if (tk.getType() != Token.RPAREN) {
      log.error("missing right paren");
      throw new Exception("syntax error");
    }
    if (!itr.hasNext()) {
      log.error("missing function body");
      throw new Exception("syntax error");
    }
    tk = itr.next();
    if (tk.getType() != Token.BLOCK) {
      log.error("invalid function body");
      throw new Exception("syntax error");
    }
    List<Token> list = (List<Token>) tk.getValue();
    for (Token tk2: list) {
      function.getTokenList().add(tk2);
    }
    if (symbolTable.get(functionName) != null) {
      log.error("symbol already defined for function " + functionName);
      throw new Exception("symbol already defined");
    }
    Symbol symbol = new Symbol(function, true);
    symbolTable.put(functionName, symbol);
  }
  
  private Object parseStatement(Token tk, TokenIterator itr) throws Exception {
    Statement statement = new Statement();
    Object ret = null;
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
      return ret;
    }
    TokenIterator itr2 = new TokenIterator(statement.getTokens());
    Token tk2 = itr2.next();
    if (tk2.getType() == Token.CONST) {
      ret = parseDeclaration(tk2, itr2);
    } else {
      ret = expression(tk2, itr2);
    }
    if (itr2.hasNext()) {
      log.error("unexpected symbol at end of line");
      throw new Exception("syntax error");
    }
    return ret;
  }
  
  private Object parseDeclaration(Token tk, TokenIterator itr) throws Exception {
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
    return val;
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
      return executor.exec(val);
    }
    if (tk.getType() == Token.MINUS) {
      Executor executor = new Executor(uMinus);
      tk = itr.next();
      Object val = primary(tk, itr);
      return executor.exec(val);
    }
    if (tk.getType() == Token.NOT) {
      Executor executor = new Executor(uNot);
      tk = itr.next();
      Object val = primary(tk, itr);
      return executor.exec(val);
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
        if (val instanceof TimeSeries) {
          // need to make a copy of TimeSeries
          TimeSeries timeSeries1 = (TimeSeries) val;
          TimeSeries timeSeries = new TimeSeries(timeSeries1.getType());
          for (TimeSeriesData timeSeriesData1: timeSeries1.getTimeSeriesDataList()) {
            TimeSeriesData timeSeriesData = new TimeSeriesData();
            timeSeriesData.setId(timeSeriesData1.getId());
            timeSeriesData.setDate(timeSeriesData1.getDate());
            timeSeriesData.setValue(timeSeriesData1.getValue());
            timeSeries.add(timeSeriesData);
          }
          if (timeSeries1.isBase()) {
            // copy Id / Name metadata for 'base' series
            timeSeries.setId(timeSeries1.getId());
            timeSeries.setName(timeSeries1.getName());
          }
          timeSeries.setSource(timeSeries1.getSource());
          timeSeries.setSourceId(timeSeries1.getSourceId());
          timeSeries.setNotes(timeSeries1.getNotes());
          timeSeries.setTitle(timeSeries1.getTitle());
          symbolTable.put(symbolName, new Symbol(timeSeries));
        } else {
          symbolTable.put(symbolName, new Symbol(val));
        }
      }
      Symbol symbol = symbolTable.get(symbolName);
      if (symbol == null) {
        throw new Exception("uninitialized symbol: " + tk.getValue());
      }
      if (itr.hasNext() && itr.peek().getType() == Token.LPAREN) {
        if (!(symbol.getValue() instanceof Function)) {
          throw new Exception("not a function: " + symbolName);
        }
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing right paren");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        List<Object> paramList = new ArrayList<Object>();
        while (true) {
          if (tk.getType() == Token.RPAREN) {
            break;
          }
          Object param = expression(tk, itr);
          paramList.add(param);
          if (!itr.hasNext()) {
            log.error("missing right paren");
            throw new Exception("syntax error");
          }
          tk = itr.next();
          if (tk.getType() == Token.COMMA) {
            if (!itr.hasNext()) {
              log.error("missing next param");
              throw new Exception("syntax error");
            }
            tk = itr.next();
            if (tk.getType() == Token.RPAREN) {
              log.error("missing next param");
              throw new Exception("syntax error");
            }
          } else if (tk.getType() != Token.RPAREN) {
            log.error("missing right paren");
            throw new Exception("syntax error");
          }
        }
        Function function = (Function) symbol.getValue();
        if (function.getTokenList().size() > 0) {
          if (function.getParams().size() != paramList.size()) {
            throw new Exception("param list size mismatch during function call: " + symbolName + "()");
          }
          SymbolTable childSymbolTable = new SymbolTable(symbolTable);
          for (int i = 0; i < paramList.size(); i++) {
            childSymbolTable.localPut(function.getParams().get(i), new Symbol(paramList.get(i)));
          }
          Parser parser = new Parser(childSymbolTable);
          TokenIterator itr2 = new TokenIterator(function.getTokenList());
          Token tk2 = itr2.next();
          ReturnResult returnResult = parser.parse(tk2, itr2);
          if (returnResult != null) {
            return returnResult.getValue();
          }
        }
        return null;
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
      if (!itr.hasNext()) {
        log.error("unexpected end of input: " + funcName);
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
        } else if (tk.getType() != Token.RPAREN) {
          log.error("unexpected token: " + tk);
          throw new Exception("syntax error: " + funcName);
        }
      }
      Utils.ASSERT(file != null, "file is null");
      return functionCaller.invokeFunction(funcName, symbolTable, file, params);
    }
    throw new Exception("unsupported primary expression: " + tk);
  }
}
