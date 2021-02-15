import javax.swing.*;

public class PersonalisationChart {
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("Call me: java PersonalisationChart <CFG-file>");
            return;
        }

        CFGReader rdr = new CFGReader(args[0]);
        ProductItem[] pitArray = rdr.readCFG();
        JComboBox<ProductItem> cbxProducts = new JComboBox<>();
        for (int i = 0; i < pitArray.length; ++i) {
            cbxProducts.addItem(pitArray[i]);
        }

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUIComponent formComponents = new GUIComponent("Графики персонализации");
                formComponents.cbxProducts = cbxProducts;
                formComponents.createAndShowGUI();
            }
        });
    }
}