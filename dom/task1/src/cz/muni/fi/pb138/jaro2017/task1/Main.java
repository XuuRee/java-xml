package cz.muni.fi.pb138.jaro2017.task1;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * ***********************************************************************
 * Take this skeleton code and implement the missing bodies of
 * <code>averageSalaryAtPosition</code> and <code>addNote</code> methods.
 * Everything else should be left untouched.<br/>
 *
 * After completion, the <code>main</code> method should work correctly.<br/>
 * It reads (parses) the given file (company.xml if you run it directly from the
 * Netbeans project) and processes it using your methods.
 *
 */
public class Main {

    /**
     * W3C object model representation of a XML document. 
     */
    private Document doc;

    /**
     * Task 1, part A: Complete this method. You are likely to consult the Java
     * Core API documentation for org.w3c.dom package when working on this task.
     * You can also use the XPath sub-API (javax.xml.xpath) to evaluate XPath
     * expressions from within Java methods.
     *
     * You can suppose that the input document is always valid, i.e. its
     * structure fulfills the criteria specified in the comments inside the
     * document company.xml.
     *
     * Returns the average salary at given position for each division in the
     * company.
     * 
     * You can create and use additional (private) methods you would need.
     *
     * @param position the position its average salary has to be calculated.
     * @return Element "avgsalaries" containing for each division 
     * elements "avgsalary" with the average salary at this division 
     * at the given position. The average salary is stored in its text content 
     * and the division id as its attribute "did". 
     */
    public Element averageSalariesAtPosition(String position) throws XPathExpressionException {
        Element salaries = doc.createElement("avgsalaries");
        NodeList divisions = doc.getElementsByTagName("division");
        
        for (int j = 0; j < divisions.getLength(); j++) {
            Element avgsalary = doc.createElement("avgsalary");
            Element specificDivision = (Element)divisions.item(j);
            avgsalary.setAttribute("did", specificDivision.getAttributes().getNamedItem("did").getNodeValue());
            NodeList employee = specificDivision.getElementsByTagName("employee");
            
            Integer divisorsCounter = 0;
            Double averageSalaryCounter = 0.0;
            
            for (int i = 0; i < employee.getLength(); i++) {
                Element specificEmployee = (Element)employee.item(i);
                avgsalary.setAttribute("position", position);
                
                if ((specificEmployee.getAttributes().getNamedItem("position").getNodeValue()).equals(position)) {
                    NodeList oneSalary = specificEmployee.getElementsByTagName("salary");
                    averageSalaryCounter += Double.parseDouble(oneSalary.item(0).getTextContent());
                    divisorsCounter++;
                }
            }
            
            averageSalaryCounter /= divisorsCounter; 
            avgsalary.setTextContent(averageSalaryCounter.toString());
            salaries.appendChild(avgsalary);
        }
        
        return salaries;
    }

    /**
     * Task 1, part B: Complete this method. The method adds a new note to an
     * existing person identified by its person ID (pid) and returns 
     * this person element. If such a person does
     * not exist, the method does nothing and returns null.
     *
     * You can create and use additional (private) methods you would need.
     *
     * @param pid of the person to add the note to this person. pid must not be
     * null nor empty (the method does not check it)
     * @param note the note to be added to the person with the given pid
     * @return the Element person with the given pid and new note added 
     * or null if such person Element does not exist.
     * 
     * The demo (method Main.main) should produce the following output:
     */
    
    public Element addNote(String pid, String note) {
        NodeList personList = doc.getElementsByTagName("person");
        
        for (int i = 0; i < personList.getLength(); i++) {
            Element person = (Element) personList.item(i);
            
            if (person.hasAttribute("pid") && person.getAttribute("pid").equals(pid)) {
                    Node newNote = doc.createElement("note");
                    newNote.setTextContent(note);
                    person.appendChild(newNote);
                    return person;
            }
        }
       
        return null;
    }

    /**
     * Create a new instance of this class and read the content of the XML file 
     * with given URL. 
     */
    public static Main newInstance(URI uri) throws SAXException,
            ParserConfigurationException, IOException {
        return new Main(uri);
    }

    /**
     * Create a new instance of this class and read the content of the given XML
     * file.
     */
    public static Main newInstance(File file)
            throws SAXException, ParserConfigurationException, IOException {
        return newInstance(file.toURI());
    }

    /**
     * Constructor creating a new instance of task1 class reading from the file
     * at the given URI
     */
    private Main(URI uri) throws SAXException, ParserConfigurationException,
            IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(uri.toString());
    }

    public void serializetoXML(URI output)
            throws IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output.toString());
        transformer.transform(source, result);
    }

    public void serializetoXML(File output) throws IOException,
            TransformerException {
        serializetoXML(output.toURI());
    }
  
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        System.out.println("Running demo...");
        if (args.length < 1) {
            System.err.println("Input file name is missing.");
            return;
        } else if (args.length < 2) {
            System.err.println("Output file name is missing.");
            return;
        }

        File input = new File(args[0]);
        File output = new File(args[1]);

        Main task1 = newInstance(input);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        try {
            System.out.println("Average salaries at position tester:");
            trans.transform(new DOMSource(task1.averageSalariesAtPosition("tester")), new StreamResult(System.out));

            System.out.println("\nPerson with pid=1 is now:");
            trans.transform(new DOMSource(task1.addNote("1", "New note to person 1")), new StreamResult(System.out));
    
            task1.serializetoXML(output);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error in averageSalariesAtPosition or addNote", ex);
        }
        System.out.println("\nDemo finished, see the above output to check whether it is correct.");
    }

    Document getDocument() {
        return doc;
    }

}
