package homework;

import homework.element.Element;
import homework.element.Student;
import homework.element.Students;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandle extends DefaultHandler {
    private Students students;
    private Student student;
    private Element element;
    private String msg;

    public Students getStudents(){
        return students;
    }
    @Override
    public void startDocument() {
        System.out.println("开始解析");
    }

    @Override
    public void endDocument() {
        System.out.println("结束解析");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(localName.equals("students")){
            students = new Students(uri, localName, qName, attributes);
        }else if(localName.equals("student")){
            student = new Student(uri, localName, qName, attributes);
        }else{
            element = new Element(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if(localName.equals("student")){
            students.addStudent(student);
        }else if(!localName.equals("students")){
            student.addMassage(element,msg);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        msg = new String(ch,start,length);
    }
}
