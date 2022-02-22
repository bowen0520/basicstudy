package xmlparsing;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DomParsing {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        //DocumentBuilderFactory.newInstance()获取DocumentBuilderFactory(document创建工厂类)
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //factory.newDocumentBuilder()获取DocumentBuilder（document创建类）
        DocumentBuilder builder = factory.newDocumentBuilder();

        //获取xml文件输入流
        //InputStream is = Dom4jParsing.class.getClassLoader().getResourceAsStream("test.xml");
        InputStream is = DomParsing.class.getResourceAsStream("/day1/test1.xml");

        //如果为空直接返回
        if(is==null){
            return ;
        }
        //获取xml相关的Document,相当于整个xml文件的缓存
        Document parse = builder.parse(is);

        //获取xml文档的根元素
        Element element = parse.getDocumentElement();

        //增
        Element haha = parse.createElement("haha");
        Text textNode = parse.createTextNode("\r\n");
        Attr nums = parse.createAttribute("nums");
        nums.setValue("45");
        haha.appendChild(textNode);
        haha.setAttributeNode(nums);
        element.appendChild(haha);

        //查
        List<Element> elements = getElementByChildElement(element, "id", "121");
        List<Element> list = getElementByAttr(element, "teacher", "dqw");
        //List<Element> haha1 = getElementByName(element, "haha");

        //删
        removeElements(elements);
        removeElements(list);
        //removeElements(haha1);

        //改
        NodeList byTagName = element.getElementsByTagName("haha");
        for(int i = 0;i<byTagName.getLength();i++){
            Node item = byTagName.item(i);
            NamedNodeMap attributes = item.getAttributes();
            attributes.item(0).setNodeValue("10");
            NodeList childNodes = item.getChildNodes();
            childNodes.item(0).setNodeValue("good luck");
        }

        //打印操作之后的xml文档缓存
        //打印处理指令
        getXmlInstructions(parse);
        //打印根节点以及内部所有元素
        getBody(element);

        //将操作后的xml缓存生成新的xml文件
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer form = tFactory.newTransformer();
        form.transform(new DOMSource(parse), new StreamResult(new File("src/main/resources/day1/newTest.xml")));
    }

    public static void removeElement(Element e){
        e.getParentNode().removeChild(e);
    }

    public static void removeElements(List<Element> list){
        for(Element e:list){
            removeElement(e);
        }
    }

    public static List<Element> getElementByAttr(Element e,String attrName,String attrValue){
        List<Element> list = new ArrayList<>();
        Attr a = e.getAttributeNode(attrName);
        if(a!=null&&a.getValue().equals(attrValue)){
            list.add(e);
        }else {
            NodeList childNodes = e.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node node = childNodes.item(j);
                if (node.getNodeType() == Node.ELEMENT_NODE) {//如果子元素是一个元素节点
                    list.addAll(getElementByAttr((Element) node,attrName,attrValue));
                }
            }
        }
        return list;
    }

    public static List<Element> getElementByChildElement(Element e,String childElementName,String childElementValue){
        List<Element> list = new ArrayList<>();
        if(childElementName.equals(e.getTagName())&&childElementValue.equals(e.getChildNodes().item(0).getNodeValue())){
            list.add((Element) e.getParentNode());
        }else{
            NodeList childNodes = e.getChildNodes();
            for (int j = 0;j<childNodes.getLength();j++){
                Node node = childNodes.item(j);
                if(node.getNodeType()==Node.ELEMENT_NODE){//如果子元素是一个元素节点
                    list.addAll(getElementByChildElement((Element) node,childElementName,childElementValue));
                }
            }
        }
        return list;
    }

    public static List<Element> getElementByName(Element e,String name){
        List<Element> list = new ArrayList<>();
        if(name.equals(e.getTagName())){
            list.add(e);
        }else{
            NodeList childNodes = e.getChildNodes();
            for (int j = 0;j<childNodes.getLength();j++){
                Node node = childNodes.item(j);
                if(node.getNodeType()==Node.ELEMENT_NODE){//如果子元素是一个元素节点
                    list.addAll(getElementByName((Element) node,name));
                }
            }
        }
        return list;
    }

    //<?xml version="1.0" encoding="UTF-8"?>
    public static void getXmlInstructions(Document parse){
        //获取处理指令
        System.out.println("<?xml version=\""+parse.getXmlVersion()+"\" encoding=\""+parse.getXmlEncoding()+"\"?>");
    }

    public static void getHead(Element e){
        System.out.print("<"+e.getTagName());
        //属性
        NamedNodeMap attributes = e.getAttributes();
        for(int i = 0;i<attributes.getLength();i++){
            Attr attr = (Attr) attributes.item(i);
            String attrName = attr.getName();
            String attrValue = attr.getValue();
            System.out.print(" "+attrName+"="+"\""+attrValue+"\"");
        }
        System.out.print(">");
    }

    public static void getBody(Element e){
        getHead(e);
        //获取子元素
        NodeList childNodes = e.getChildNodes();
        for (int j = 0;j<childNodes.getLength();j++){
            Node node = childNodes.item(j);
            if(node.getNodeType()==Node.ELEMENT_NODE){//如果子元素是一个元素节点
                getBody((Element) node);
            }else if(node.getNodeType()==Node.TEXT_NODE){//如果子元素是一个文本节点
                System.out.print(node.getNodeValue());
            }else if(node.getNodeType()==Node.COMMENT_NODE){
                System.out.print("<!--"+node.getNodeValue()+"-->");
            }
        }
        System.out.print("</"+e.getTagName()+">");
    }
}
