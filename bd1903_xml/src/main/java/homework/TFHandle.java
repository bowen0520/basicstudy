package homework;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class TFHandle {
    public static TransformerHandler getHandle() throws TransformerConfigurationException {

        SAXTransformerFactory sff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        TransformerHandler handler = sff.newTransformerHandler();

        Transformer tf = handler.getTransformer();

        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.STANDALONE, "yes");

        handler.setResult(new StreamResult(new File("src/main/resources/day2/newSchool.xml")));
        return handler;
    }
}
