# java-dom-xml
 
 The Document Object Model provides APIs that allows you create, modify, delete and rearrange nodes.
 
 These are the JAXP APIs used by DOMEcho:

```
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;
```

These classes are for the exceptions that can be thrown when the XML document is parsed:

```
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
```

These classes read the sample XML file and manage output:

```
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
```

Finally, import the W3C definitions for a DOM, exceptions, entities and nodes:

```
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
```
