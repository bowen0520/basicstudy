package homework;

import homework.element.Students;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class SaxParsing {
    public static Students getStudents() throws ParserConfigurationException, SAXException, IOException{
        //1.获取SAXParserFactory
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        saxParserFactory.setNamespaceAware(true);

        //2. 获取SAXParser
        SAXParser saxParser = saxParserFactory.newSAXParser();

        MyHandle handle = new MyHandle();

        saxParser.parse(SaxParsing.class.getResourceAsStream("/day2/school.xml"),handle);

        return handle.getStudents();
    }
}
