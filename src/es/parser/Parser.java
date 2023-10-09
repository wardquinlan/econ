package es.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.core.ESIterator;
import es.evaluator.ESNode;
import es.evaluator.Executor;
import es.evaluator.SimpleStatement;
import es.evaluator.Statement;

public class Parser {
  private static final Log log = LogFactory.getFactory().getInstance(Parser.class);

  public ESIterator<Statement> parse(Token tk, ESIterator<Token> itr) throws Exception {
    List<Statement> list = new ArrayList<Statement>();
    while (true) {
      list.add(parseStatement(tk, itr));
    }
  }
  
  private Statement parseStatement(Token tk, ESIterator<Token> itr) throws Exception {
    List<Token> tokens = new ArrayList<Token>();
    while (true) {
      if (tk.getType() == Token.SEMI) {
        break;
      }
      tokens.add(tk);
      if (!itr.hasNext()) {
        log.error("missing semi colon");
        throw new Exception("syntax error");
      }
      tk = itr.next();
    }
    if (tokens.size() == 0) {
      return null;
    }
    ESIterator<Token> itr2 = new ESIterator<Token>(tokens);
    Token tk2 = itr2.next();
    Object expr = expression(tk2, itr2);
    if (itr2.hasNext()) {
      log.error("unexpected symbol at end of line");
      throw new Exception("syntax error");
    }
    SimpleStatement statement = new SimpleStatement();
    statement.setNode(expr);
    return statement;
  }

  private Object expression(Token tk, ESIterator<Token> itr) throws Exception {
    Object val1 = primary(tk, itr);
    while (true) {
      if (!itr.hasNext()) {
        return val1;
      }
      if (itr.peek().getType() == Token.AND || itr.peek().getType() == Token.OR) {
        itr.next();
        if (!itr.hasNext()) {
          log.error("missing RHS");
          throw new Exception("syntax error");
        }
        tk = itr.next();
        Object val2 = primary(tk, itr);
        ESNode node = new ESNode();
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
    throw new Exception("unsupported primary expression: " + tk);
  }
}
