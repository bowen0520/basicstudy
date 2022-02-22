package homework;

import homework.element.Students;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.IOException;

public class Do {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, TransformerConfigurationException, IOException {
        Students students = SaxParsing.getStudents();
        students.write(TFHandle.getHandle());
    }
}
