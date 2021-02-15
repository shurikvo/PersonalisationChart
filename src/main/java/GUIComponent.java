import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIComponent extends JFrame implements ItemListener {
    private String title;

    private JLabel lblInfo = null;
    private JTextArea strResult = null;

    public JComboBox<ProductItem> cbxProducts;

    private void addComponentsToPane(Container pane) {
        pane.setLayout(new BorderLayout());

        // Prepare: --------------------------------------------------------------------
        lblInfo = new JLabel("          ");
        strResult = new JTextArea("-------------------------------------------------");
        cbxProducts.setSelectedIndex(-1);
        ActionListener cbxActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == cbxProducts) {
                    if(cbxProducts.getSelectedIndex() >= 0) {
                        writeMsg("Выбран продукт: " + ((ProductItem) cbxProducts.getSelectedItem()).toString());
                        ChartComponent cco = new ChartComponent((ProductItem) cbxProducts.getSelectedItem());
                        cco.setLogAttributes(lblInfo, strResult);
                        if (cco.getData() < 0)
                            return;
                        if ( cco.createDatasetAll() < 0)
                            return;
                        cco.initUI();
                    }
                }
            }
        };
        // Top area: -------------------------------------------------------------------
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BorderLayout());
        pane.add(pnlTop, BorderLayout.PAGE_START);
        cbxProducts.addActionListener(cbxActionListener);
        pnlTop.add(cbxProducts, BorderLayout.CENTER);
        // Data area: ------------------------------------------------------------------
        JTabbedPane pnlTabs = new JTabbedPane();

        JPanel pnlLog = new JPanel(null);
        pnlLog.setPreferredSize(new Dimension(700,400));
        strResult.setEditable(false);
        strResult.setBackground(Color.black);
        strResult.setForeground(Color.white);
        strResult.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(strResult,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        pnlLog.setLayout(new GridLayout(1, 1));
        pnlLog.add(scroll);
        ImageIcon icon = null;
        pnlTabs.addTab("Журнал", icon,  pnlLog,"Лог программы");
        pnlTabs.setMnemonicAt(0, KeyEvent.VK_1);

        pane.add(pnlTabs, BorderLayout.CENTER);
        // Bottom area: ----------------------------------------------------------------
        JPanel pnlBottom = new JPanel();
        pnlBottom.setLayout(new BorderLayout());
        pane.add(pnlBottom, BorderLayout.PAGE_END);
        lblInfo.setBorder(BorderFactory.createLineBorder(Color.black));
        pnlBottom.add(lblInfo, BorderLayout.CENTER);
    }

    public void createAndShowGUI(){
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addComponentsToPane(this.getContentPane());
        this.pack();
        centerFrame();
        this.setVisible(true);
    }

    private void centerFrame() {
        Dimension windowSize = this.getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();

        int dx = centerPoint.x - windowSize.width / 2;
        int dy = centerPoint.y - windowSize.height / 2;
        this.setLocation(dx, dy);
    }

    private void writeMsg(String sMes){
        if (lblInfo != null) lblInfo.setText(sMes);
        if (strResult != null) strResult.setText(strResult.getText() + "\n" + sMes);
    }

    public GUIComponent(String sTitle) {
        title = sTitle;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }
}
