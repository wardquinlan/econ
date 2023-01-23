package econ.gui;

public class Lock {
  private static Lock lock = new Lock();
  
  private Lock() {
  }
  
  public static Lock instance() {
    return lock;
  }
}
