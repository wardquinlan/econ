package es.gui;

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import es.core.Settings;

public class Frame extends JFrame {
  private static final long serialVersionUID = 4488715210063650680L;

  public Frame(Context ctx) throws Exception {
    super("ES - " + Settings.getInstance().getVersion());
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    JTabbedPane tabbedPane = new JTabbedPane();
    for (Panel panel: ctx.getPanels()) {
      ChartsPanel chartsPanel = new ChartsPanel(ctx, panel, tabbedPane);
      chartsPanel.setToolTipText("");
      tabbedPane.addTab(panel.getLabel(), chartsPanel);
    }
    setSize(Toolkit.getDefaultToolkit().getScreenSize());
    getContentPane().add(tabbedPane);
    ToolTipManager.sharedInstance().setInitialDelay((int) ctx.get("settings.tooltips.initialdelay"));
    ToolTipManager.sharedInstance().setDismissDelay((int) ctx.get("settings.tooltips.dismissdelay"));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
    tabbedPane.getComponentAt(0).requestFocus();
  }
}
