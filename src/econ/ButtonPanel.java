package econ;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel {
  private static final long serialVersionUID = 4277429174272287112L;
  private JButton buttonLeft = new JButton("<<");
  private JButton buttonRight = new JButton(">>");

  public ButtonPanel() {
    buttonLeft.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("left clicked");
      }
    });
    buttonRight.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("right clicked");
      }
    });
    add(buttonLeft);
    add(buttonRight);
  }
}
