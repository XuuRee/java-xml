/*
 * Task1Test.java
 * JUnit based test
 *
 */
package cz.muni.fi.pb138.jaro2017.task1;

import cz.muni.fi.pb138.jaro2017.task1.Main;
import java.io.IOException;
import java.net.URI;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.w3c.dom.Document;

/**
 *
 * @author originally Petr Adamek 2008, 
 * changes by Tomas Pitner, Feb 2011, Feb 2013, Feb 2014, Jan 2015, Jan 2017
 */
public class Task1Test extends TestCase {
    
    private static final String COMPANYXML = "company.xml";
    public static final String FIRSTNOTE = "//person[@pid='1']/note[1]";
    public static final String SECONDNOTE = "//person[@pid='1']/note[2]";
    private static final String SALARY_SUM_WORKER = "sum(//division[@did='development_brno']//employee[@position='worker']//salary)";
    private static final String PERSON_COUNT_WORKER = "count(//division[@did='development_brno']//employee[@position='worker'])";
    private static final String SALARY_SUM_TESTER = "sum(//division[@did='development_brno']//employee[@position='tester']//salary)";
    private static final String PERSON_COUNT_TESTER = "count(//division[@did='development_brno']//employee[@position='tester'])";

    private Main solution;
    private XPath xpath;
    private static final double DELTA = 0.001;

    public Task1Test(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        URI uri = Task1Test.class.getResource(COMPANYXML).toURI();
        if (uri == null) {
            throw new IOException("The XML file for testing cannot be found.");
        }
        solution = Main.newInstance(uri);
        xpath = XPathFactory.newInstance().newXPath();
    }

    public void testAverageSalaryAtPosition() throws XPathExpressionException {
        final Document document = solution.getDocument();
        double avg1 = (Double)xpath.evaluate(
                SALARY_SUM_WORKER, document, XPathConstants.NUMBER) 
                / (Double) xpath.evaluate(
                PERSON_COUNT_WORKER, document, XPathConstants.NUMBER);
        
        
        double avg2 = (Double)xpath.evaluate(
                SALARY_SUM_TESTER, document, XPathConstants.NUMBER) 
                / (Double) xpath.evaluate(
                PERSON_COUNT_TESTER, document, XPathConstants.NUMBER);
        
    }

    public void testAddNote() throws XPathExpressionException {
        final Document document = solution.getDocument();
        
        double origNotesCount = (Double)xpath.evaluate(
                "count(//note)", document, XPathConstants.NUMBER);
        
        String note = (String)xpath.evaluate(
                FIRSTNOTE, document, XPathConstants.STRING);
        assertEquals("Poznámka", note);
        
        solution.addNote("1", "Nová poznámka");
        
        note = (String)xpath.evaluate(
                FIRSTNOTE, document, XPathConstants.STRING);
        assertEquals("Poznámka", note);
        
        String note2 = (String)xpath.evaluate(
                SECONDNOTE, document, XPathConstants.STRING);
        assertEquals("Nová poznámka", note2);
        
        assertEquals(origNotesCount+1, (Double)xpath.evaluate(
                "count(//note)", document, XPathConstants.NUMBER), DELTA);
    }
}
