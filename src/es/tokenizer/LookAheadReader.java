package es.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Deprecated => use ESIterator in the future
 */
public class LookAheadReader {
  private InputStreamReader inputStreamReader;
  private Integer next;
  
  public LookAheadReader(InputStream inputStream) {
    inputStreamReader = new InputStreamReader(inputStream);
  }
  
  public int read() throws IOException {
    if (inputStreamReader == null) {
      throw new IOException("reader already closed");
    }
    if (next == null) {
      next = inputStreamReader.read();
    }
    int tmp = next; 
    next = inputStreamReader.read();
    return tmp;
  }
  
  public int peek() {
    return next;
  }
  
  public void close() throws IOException {
    inputStreamReader.close();
    inputStreamReader = null;
  }
}
