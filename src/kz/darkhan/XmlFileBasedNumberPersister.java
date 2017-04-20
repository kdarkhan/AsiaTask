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
import java.io.StringWriter;
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
        int MAX_FILE_SIZE = 1000;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("numbers");
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            /*
             Для подсчета размера XML на лету, я конвертирую каждый елемент в XML String в цикле
             Так как каждый элемент маленький, это не должно повлиять сильно на производительность
             Однако, нужно также учесть размер оборачивающего элемента <numbers>
             Поэтому изначально я начинаю не с нулевого каунтера
             Если numbers пуст, то xml содержит 64 символа и выглядит вот так
             <?xml version="1.0" encoding="UTF-8" standalone="no"?><numbers/>
             Если numbers не пуст, то xml содержит 73 символа выглядит вот так
             <?xml version="1.0" encoding="UTF-8" standalone="no"?><numbers></numbers>
             */
            int currentSize = numbers.size() > 0 ? 73 : 64;


            for (NumWrapper n : numbers) {
                Element number = doc.createElement("number");

                Element value = doc.createElement("value");
                value.appendChild(doc.createTextNode(Integer.toString(n.getNum())));
                number.appendChild(value);

                Element isEven = doc.createElement("isEven");
                isEven.appendChild(doc.createTextNode(Boolean.toString(n.isEven())));
                number.appendChild(isEven);

                rootElement.appendChild(number);

                // Compute the size of the current node by converting it to string
                StringWriter writer = new StringWriter();
                transformer.transform(new DOMSource(number), new StreamResult(writer));
                currentSize += writer.getBuffer().toString().length();

                if (currentSize > MAX_FILE_SIZE) {
                    throw new FileTooLargeException(MAX_FILE_SIZE);
                }
            }


            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            // Уберите комменты чтобы XML печатался красиво с индентами
            // Я поставил это в комментах чтобы правильно считать размер файла
            // transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Включить HEADER в начале XML
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.transform(source, result);

            System.out.println("Файл сохранен!");
            System.out.println("Размер файла: " + currentSize);

            return true;
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return false;
        } catch (FileTooLargeException e) {
            System.err.println("Слишком большой размер файла");
            e.printStackTrace();
            return false;
        }
    }
}
