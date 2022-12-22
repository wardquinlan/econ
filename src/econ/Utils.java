package econ;

public class Utils {
  public static int parseHex(String string) throws Exception {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < string.length(); i++) {
      char ch = string.charAt(i);
      if (Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
        sb.append(ch);
      } else {
        throw new Exception("invalid hex value: " + string);
      }
    }
    return Integer.parseInt(sb.toString(), 16);
  }
}
