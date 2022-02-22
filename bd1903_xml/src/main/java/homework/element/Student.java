package homework.element;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Student extends Element{
    Map<Element,String> map=new HashMap<>();

    public Student(String uri, String localName, String qName, Attributes attributes) {
        super(uri, localName, qName, attributes);
    }

    public void addMassage(Element e,String s){
        map.put(e,s);
    }

    public void write(TransformerHandler handler) throws SAXException {
        this.start(handler);
        Set<Map.Entry<Element, String>> entries =  map.entrySet();
        entries.forEach((o)->{
            try {
                o.getKey().start(handler);
                String msg = o.getValue();
                handler.characters(msg.toCharArray(),0,msg.length());
                o.getKey().end(handler);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        });
        this.end(handler);
    }
}
