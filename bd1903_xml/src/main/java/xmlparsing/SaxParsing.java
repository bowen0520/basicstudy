package xmlparsing;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class SaxParsing {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException {
        //1.获取SAXParserFactory
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

        //2. 获取SAXParser
        SAXParser saxParser = saxParserFactory.newSAXParser();

        TransformerHandler handle = getHandle();

        saxParser.parse(SaxParsing.class.getResourceAsStream("/day2/student.xml"),new DefaultHandler(){
            @Override
            public void startDocument() throws SAXException {
                System.out.println("开始解析");
                //handle.startDocument();
                //AttributesImpl attributes = new AttributesImpl();
                //attributes.addAttribute(null,"class_name","class_name","text","ruankai");
                //handle.startElement(null,"class","class",attributes);
                //handle.endElement(null,"class","class");
            }

            @Override
            public void endDocument() throws SAXException {
                System.out.println("结束解析");
                //handle.endDocument();
            }
            boolean flag = true;
            String book = "";
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                System.out.print("<"+qName);
                for(int i = 0;i<attributes.getLength();i++){
                    if(attributes.getQName(i).equals("student_id")&&attributes.getValue(i).equals("s121")){
                        flag = false;
                        book = qName;
                    }
                    System.out.print(" "+attributes.getQName(i)+"=\""+attributes.getValue(i)+"\"");
                }
                System.out.print(">");
                /*if(flag) {
                    handle.startElement(uri, localName, qName, attributes);
                }*/
            }

            @Override
            public void endElement(String uri, String localName, String qName) throws SAXException {
                System.out.println("</"+qName+">");
                /*if(flag) {
                    handle.endElement(uri, localName, qName);
                }
                if(qName.equals(book)){
                    flag = true;
                }*/
            }

            @Override
            public void characters(char[] ch, int start, int length) throws SAXException {
                System.out.print(new String(ch,start,length));
                //handle.characters(ch, start, length);
            }
        });
    }

    private static TransformerHandler getHandle() throws TransformerConfigurationException {
        //创建 SAXTransformerFactory 实例
        SAXTransformerFactory sff = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        //创建 TransformerHandler 实例
        TransformerHandler handler = sff.newTransformerHandler();
        //通过 handler 对象获取 javax.xml.transform.Transformer 对象
        Transformer tf = handler.getTransformer();
        //tf 对象用来设置结果格式
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.STANDALONE, "yes");
        //通过 handler 对象与 result 对象关联，将 xml 信息写入文件
        handler.setResult(new StreamResult(new File("src/main/resources/day2/newStudent.xml")));
        return handler;
    }
}
