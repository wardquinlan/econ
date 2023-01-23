package econ.gui;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

public class Frame extends JFrame {
  private static final long serialVersionUID = 4488715210063650680L;

  public Frame(Context ctx) throws Exception {
    super("Econ");
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    JTabbedPane tabbedPane = new JTabbedPane();
    for (Panel panel: ctx.getPanels()) {
      ChartsPanel chartsPanel = new ChartsPanel(ctx, panel);
      chartsPanel.setToolTipText("");
      tabbedPane.addTab(panel.getLabel(), chartsPanel);
    }
    setSize(Toolkit.getDefaultToolkit().getScreenSize());
    getContentPane().add(tabbedPane);
    ToolTipManager.sharedInstance().setInitialDelay((int) ctx.get("settings.tooltips.initialdelay"));
    ToolTipManager.sharedInstance().setDismissDelay((int) ctx.get("settings.tooltips.dismissdelay"));
    setVisible(true);
    if (ctx.getPanels().size() == 1) {
      tabbedPane.getComponentAt(0).requestFocus();
    }
  }
}
