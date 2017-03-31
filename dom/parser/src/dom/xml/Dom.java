package dom.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Dom {
    
    /**
     * W3C object model representation of a xml document.
     */
    private Document doc;

    /**
     * Vypise vsechna ID
     */
    public void printIds() {
        //nacteme seznam elementu s tagem ID
        NodeList idList = doc.getElementsByTagName("ID");

        for (int i = 0; i < idList.getLength(); i++) {
            
                Element idElement = (Element) idList.item(i);
                //nacteni textoveho obsahu elementu
                String textId = idElement.getTextContent();
                System.out.println(textId);
            
        }

    }

    /**
     * Vrati pocet synsetu jednoho slovniho druhu
     *
     * @param pos slovni druh
     * @return pocet synsetu
     */
    public int getPosCount(String pos) {
        int posCount = 0;
        NodeList posList = doc.getElementsByTagName("POS");

        for (int i = 0; i < posList.getLength(); i++) {
            
                Element posElement = (Element) posList.item(i);
                //nacteni textoveho obsahu elementu
                String textPos = posElement.getTextContent();
                if (textPos.equals(pos)) {
                    posCount++;
                }
            
        }
        return posCount;
    }

    /**
     * Zmeni text literalu a vrati pocet zmenenych
     *
     * @param original hledana hodnota
     * @param update nova hodnota
     * @return pocet zmenenych literalu
     */
    public int changeLiteral(String original, String update) {
        NodeList litList = doc.getElementsByTagName("LITERAL");
        int changed = 0;

        for (int i = 0; i < litList.getLength(); i++) {
            
                Element litElement = (Element) litList.item(i);
                //nacteni textoveho obsahu elementu
                String textLiteral = litElement.getTextContent();
                if (textLiteral.equals(original)) {
                    litElement.setTextContent(update);
                    changed++;
                }
            
        }
        return changed;
    }

    /**
     * Odstrani zaznam s urcenym ID
     *
     * @param id ID zaznamu, ktery se ma odstranit
     * @return pocet odstranenych
     */
    public int removeSynsetById(String removeId) {
        int removed = 0;
        NodeList synList = doc.getElementsByTagName("SYNSET");

        for (int i = 0; i < synList.getLength(); i++) {
                Element synElement = (Element) synList.item(i);
                //najdeme si ID k synsetu
                NodeList idList = synElement.getElementsByTagName("ID");
                Node idElement = idList.item(0);
                String textId = idElement.getTextContent();

                if (textId.equals(removeId)) {
                    //odstranime element Synset,
                    doc.getDocumentElement().removeChild(synElement);
                    removed++;
                }
            
        }

        return removed;
    }
    
    public void addElement() {
        NodeList synList = doc.getElementsByTagName("SYNSET");
        for (int i = 0; i < synList.getLength(); i++) {
          Element synElement = (Element) synList.item(i);
          Element newtest = doc.createElement("test");
          newtest.setTextContent(Integer.toString(i));
          synElement.appendChild(newtest);
        }
    }

    /**
     * Vrati seznam synonym pro synset
     *
     * @param id hledany synset
     * @return seznam synonym
     */
    public String getSynonyms(String id) {
        String synonyms = "";
        NodeList synList = doc.getElementsByTagName("SYNSET");

        for (int i = 0; i < synList.getLength(); i++) {
            
                Element synElement = (Element) synList.item(i);
                //najdeme si ID k synsetu
                NodeList idList = synElement.getElementsByTagName("ID");
                Node idElement = idList.item(0);
                String textId = idElement.getTextContent();

                if (textId.equals(id)) {
                    //najdeme vsechny LITERAL
                    NodeList litList = synElement.getElementsByTagName("LITERAL");

                    for (int j = 0; j < litList.getLength(); j++) {
                        if (litList.item(j) instanceof Element) {
                            //doplnime k vysledku text a hodnotu atributu sense
                            Element litElement = (Element) litList.item(j);
                            String textLiteral = litElement.getTextContent();
                            String atrSense = litElement.getAttribute("sense");

                            synonyms += textLiteral + ":" + atrSense + ", ";
                        }
                    }
                }
            
        }

        return synonyms;
    }

    /**
     * Vytvori novou instanci teto tridy a nacte obsah
     * XML dokumentu zadaneho jmena.
     */
    public static Dom newInstance(String fileName) throws SAXException,
            ParserConfigurationException, IOException {
        return new Dom(fileName);
    }

    /**
     * Vytvori novou instanci teto tridy a nacte obsah XML dokumentu.
     */
    public static Dom newInstance(File file)
            throws SAXException, ParserConfigurationException, IOException {
        return newInstance(file.toString());
    }

    /**
     * Constructor tridy, nacte soubor zadaneho jmena a pripravi DOM
     */
    private Dom(String fileName) throws SAXException, ParserConfigurationException,
            IOException {
        // Vytvorime instanci tovarni tridy
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // Pomoci tovarni tridy ziskame instanci DocumentBuilderu
        DocumentBuilder builder = factory.newDocumentBuilder();
        // DocumentBuilder pouzijeme pro zpracovani XML dokumentu
        // a ziskame model dokumentu ve formatu W3C DOM
        doc = builder.parse(fileName);
    }

    /*
     * Z dokumentu ve formatu DOM vytvori XML dokument a zapise ho do souboru
     * s urcenym URI
     */
    public void serializetoXML(String fileName) throws IOException,
            TransformerConfigurationException, TransformerException {
        // Vytvorime instanci tovarni tridy
        TransformerFactory factory = TransformerFactory.newInstance();
        // Pomoci tovarni tridy ziskame instanci tzv. kopirovaciho transformeru
        Transformer transformer = factory.newTransformer();
        // Vstupem transformace bude dokument v pameti
        DOMSource source = new DOMSource(doc);
        // Vystupem transformace bude vystupni soubor
        StreamResult result = new StreamResult(new FileOutputStream(fileName));
        // Provedeme transformaci
        transformer.transform(source, result);
    }

    /*
     * Z dokumentu ve formatu DOM vytvori XML dokument a zapise ho do souboru
     */
    public void serializetoXML(File output) throws IOException,
            TransformerException {
        serializetoXML(output.toString());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SAXException,
            ParserConfigurationException, TransformerException {

        Dom dom = newInstance("wn.xml");

        dom.printIds();

        int verbs = dom.getPosCount("v");
        System.out.println("Verbs: " + verbs);

        int changedLiterals = dom.changeLiteral("meerkat", "surikata");
        System.out.println("Updated: " + changedLiterals);

        int removedSynsets = dom.removeSynsetById("eng-30-01171183-v");
        System.out.println("Removed: " + removedSynsets);

        String synonyms = dom.getSynonyms("eng-30-07881800-n");
        System.out.println(synonyms);
        dom.addElement();

        dom.serializetoXML("output.xml");
    }
    
}
