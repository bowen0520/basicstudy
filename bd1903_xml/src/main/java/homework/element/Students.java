package homework.element;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.transform.sax.TransformerHandler;
import java.util.ArrayList;
import java.util.List;

public class Students extends Element{
    List<Student> list = new ArrayList<>();

    public Students(String uri, String localName, String qName, Attributes attributes) {
        super(uri, localName, qName, attributes);
    }

    public void addStudent(Student student){
        list.add(student);
    }

    public void write(TransformerHandler handler) throws SAXException {
        handler.startDocument();
        this.start(handler);
        list.forEach(o->{
            try {
                o.write(handler);
            } catch (SAXException e) {
                e.printStackTrace();
            }
        });
        this.end(handler);
        handler.endDocument();
    }

    public List<Student> getStudentByAttr(String name,String value){
        List<Student> list = new ArrayList<>();
        for(int i = 0;i<list.size();i++){
            Student student = list.get(i);
            student.getAttributeValue(name);
            if(student.getAttributeValue(name).equals(value)){
                list.add(student);
            }
        }
        return list;
    }

    public void removStudentbyAttr(String name,String value){
        for(int i = 0;i<list.size();i++){
            Student student = list.get(i);
            student.getAttributeValue(name);
            if(student.getAttributeValue(name).equals(value)){
                list.remove(i);
            }
        }
    }
}
