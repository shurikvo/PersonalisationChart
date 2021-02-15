import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CFGReader {
    private String fileName;

    public ProductItem[] readCFG() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        Document doc = null;
        XPathExpression expr;
        Node xN;
        ProductItem[] pitArray = new ProductItem[0];

        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(fileName);

            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            expr = xpath.compile("//product");
            NodeList nodes = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);

            pitArray = new ProductItem[nodes.getLength()];

            for (int i = 0; i < nodes.getLength(); i++) {
                ProductItem pit = new ProductItem();

                expr = xpath.compile("title");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.title = xN.getTextContent();

                expr = xpath.compile("type");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.type = xN.getTextContent();

                expr = xpath.compile("DBServer");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.sDBServer = xN.getTextContent();

                expr = xpath.compile("DBBase");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.sDBBase = xN.getTextContent();

                expr = xpath.compile("DBUser");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.sDBUser = xN.getTextContent();

                expr = xpath.compile("DBPsw");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.sDBPsw = xN.getTextContent();

                expr = xpath.compile("Query");
                xN = (Node)expr.evaluate(nodes.item(i), XPathConstants.NODE);
                pit.sQuery = xN.getTextContent();

                pitArray[i] = pit;
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return pitArray;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return pitArray;
        }
        return pitArray;
    }

    public CFGReader(String sFile) {
        fileName = sFile;
    }
}
