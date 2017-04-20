package kz.darkhan;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

public class XmlFileBasedNumberPersister extends AbstractNumbersPersister {

    private static XmlFileBasedNumberPersister instance = null;

    private XmlFileBasedNumberPersister() {
    }

    public static XmlFileBasedNumberPersister getInstance() {
        if (instance == null) {
            instance = new XmlFileBasedNumberPersister();
        }
        return instance;
    }

    @Override
    public boolean persist(List<NumWrapper> numbers) {
        String filename = "output.xml";
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("numbers");
            doc.appendChild(rootElement);


            for (NumWrapper n : numbers) {
                Element number = doc.createElement("number");

                Element value = doc.createElement("value");
                value.appendChild(doc.createTextNode(Integer.toString(n.getNum())));
                number.appendChild(value);

                Element isEven = doc.createElement("isEven");
                isEven.appendChild(doc.createTextNode(Boolean.toString(n.isEven())));
                number.appendChild(isEven);

                rootElement.appendChild(number);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(source, result);

            System.out.println("File saved!");

            return true;
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        }
    }
}
