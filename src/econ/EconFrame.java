package econ;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

public class EconFrame extends JFrame {
  private static final long serialVersionUID = 4488715210063650680L;

  public EconFrame(EconContext econContext) throws Exception {
    super();
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    JTabbedPane tabbedPane = new JTabbedPane();
    for (Panel panel: econContext.getPanels()) {
      tabbedPane.addTab(panel.getLabel(), new EconPanel(econContext));
    }
    JFrame frame = new JFrame("Econ");
    frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(tabbedPane);
    frame.setVisible(true);
  }
}
