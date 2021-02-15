import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChartComponent extends JFrame {
    public String title = "", type = "";
    public String sDBServer = "", sDBBase = "", sDBUser = "", sDBPsw = "", sQuery = "";

    private JLabel lblInfo = null;
    private JTextArea strResult = null;
    private XYSeriesCollection dataset;
    private Map<String, PointItem> pointItemMap = new HashMap<String, PointItem>();
    public ArrayList<String> chartKeyList = new ArrayList<String>();

    public int createDatasetAll() {
        int RC;

        for (int i = 0; i < chartKeyList.size(); ++i) {
            RC = createDataset(chartKeyList.get(i));
            if (RC < 0)
                return RC;
        }
        return 0;
    }

    public int createDataset(String sKey) {
        XYSeries series = new XYSeries(sKey);

        writeMsg(sKey + ":");
        for (String sK : pointItemMap.keySet()) {
            PointItem pit = pointItemMap.get(sK);
            if (!pit.date.substring(0,4).equals(sKey))
                continue;
            series.add(Double.parseDouble(pit.date.substring(4)), Double.parseDouble(pit.count));
            writeMsg(pit.date + " (" + pit.date.substring(4) + ") " + pit.count);
        }
        dataset.addSeries(series);

        return 0;
    }

    public void initUI() {
        JFreeChart chart = createChart();

        this.setLayout( new FlowLayout() );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        this.add(chartPanel);

        this.pack();
        this.setTitle("График персонализации");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    public JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createXYLineChart(
                title,
                "По месяцам",
                "Количество персонализированных карт",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(title, new Font("Serif", java.awt.Font.BOLD, 18)));

        return chart;
    }

    public int getData() {
        int RC;

        pointItemMap = new HashMap<String, PointItem>();
        chartKeyList = new ArrayList<String>();

        DBMatter dbM = new DBMatter(sDBServer,sDBBase,sDBUser);
        RC = dbM.connect(sDBPsw);
        if (RC < 0) {
            writeMsg(": Failed: "+dbM.Message);
            return -1;
        }
        writeMsg("Connect DB: OK");

        RC = dbM.runQuery(sQuery);
        if(RC < 0) {
            writeMsg("DB query failed: "+dbM.Message);
            return -1;
        }

        try {
            while (dbM.resultSet.next()) {
                String sKey, sMon, sCount, sYear;

                PointItem pit = new PointItem();
                sMon = dbM.resultSet.getString(2).replace(".","").substring(0, 6);
                sYear = sMon.substring(0, 4);
                sKey = sMon;
                sCount = dbM.resultSet.getString(1);
                if (pointItemMap.containsKey(sKey)) {
                    int sum = Integer.parseInt(pointItemMap.get(sKey).count) + Integer.parseInt(sCount);
                    pit.count = Integer.toString(sum);
                    pit.date = sMon;
                    pointItemMap.replace(sKey, pit);
                } else {
                    pit.count = sCount;
                    pit.date = sMon;
                    pointItemMap.put(sKey, pit);
                }
                if (!chartKeyList.contains(sYear))
                    chartKeyList.add(sYear);
            }
            writeMsg("SQL query OK: " + RC + " (" + pointItemMap.keySet().size() + ")");
            dbM.closeConnection();
        } catch (SQLException e) {
            writeMsg("DB result failed: "+e.getMessage());
            return -1;
        }

        return pointItemMap.keySet().size();
    }

    public void setLogAttributes(JLabel lblI, JTextArea strR) {
        lblInfo = lblI;
        strResult = strR;
    }

    public ChartComponent(ProductItem pit) {
        title = pit.title;
        type = pit.type;
        sDBServer = pit.sDBServer;
        sDBBase = pit.sDBBase;
        sDBUser = pit.sDBUser;
        sDBPsw = pit.sDBPsw;
        sQuery = pit.sQuery;
        dataset = new XYSeriesCollection();
    }

    private void writeMsg(String sMes){
        if (lblInfo != null) lblInfo.setText(sMes);
        if (strResult != null) strResult.setText(strResult.getText() + "\n" + sMes);
    }
}
