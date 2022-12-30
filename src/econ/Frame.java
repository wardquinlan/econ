package econ;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class Frame extends JFrame {
  private static final long serialVersionUID = 4488715210063650680L;

  public Frame(Context ctx) throws Exception {
    super("Econ");
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    JTabbedPane tabbedPane = new JTabbedPane();
    for (Panel panel: ctx.getPanels()) {
      DefaultTableModel model = new DefaultTableModel();
      model.addColumn("Date");
      model.addColumn("TBOND2");
      model.addColumn("TBOND10");
      for (int i = 0; i < 100; i++) {
        model.addRow(new Object[] {"2021-10-31", "4.37", "4.25"});
      }
      JTable table = new JTable(model);
      ChartsPanel chartsPanel = new ChartsPanel(ctx, panel);
      JPanel containerPanel = new JPanel();
      containerPanel.setLayout(new BorderLayout());
      containerPanel.add(new ButtonPanel(), BorderLayout.NORTH);
      containerPanel.add(new JScrollPane(table), BorderLayout.WEST);
      containerPanel.add(chartsPanel, BorderLayout.CENTER);
      tabbedPane.addTab(panel.getLabel(), containerPanel);
    }
    setSize(Toolkit.getDefaultToolkit().getScreenSize());
    getContentPane().add(tabbedPane);
    setVisible(true);
  }
}
