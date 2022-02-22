package homework.element;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.transform.sax.TransformerHandler;

public class Element {
    String uri;
    String localName;
    String qName;
    AttributesImpl attributes;

    public Element(String uri, String localName, String qName, Attributes attributes) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        this.attributes = new AttributesImpl();
        for(int i = 0;i<attributes.getLength();i++){
            this.attributes.addAttribute(attributes.getURI(i),attributes.getLocalName(i),attributes.getQName(i),attributes.getType(i),attributes.getValue(i));
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
        this.qName = this.uri==null?localName:uri+":"+localName;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
        this.qName = this.uri==null?localName:uri+":"+localName;
    }

    public String getqName() {
        return qName;
    }

    public String getAttributeName(int i) {
        return attributes.getLocalName(i);
    }

    public String getAttributeValue(int i){
        return attributes.getValue(i);
    }

    public String getAttributeValue(String name){
        return attributes.getValue(name);
    }

    public void setAttributeName(int i,String name) {
        attributes.setLocalName(i,name);
    }

    public void setAttributeValue(int i,String value) {
        attributes.setLocalName(i,value);
    }

    public void addAttribute(String name,String value,String type) {
        attributes.addAttribute(uri,name,uri==null?name:uri+":"+name,type,value);
    }

    public void start(TransformerHandler handler) throws SAXException {
        handler.startElement(uri,localName,qName,attributes);
    }
    public void end(TransformerHandler handler) throws SAXException {
        handler.endElement(uri,localName,qName);
    }
}
