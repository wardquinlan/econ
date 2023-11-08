package es.gui;

import java.awt.Point;

public class Line {
  private Point src;
  private Point dest;
  
  public Line(Point src, Point dest) {
    this.src = src;
    this.dest = dest;
  }

  public Point getSrc() {
    return src;
  }

  public Point getDest() {
    return dest;
  }
}
