package xmlparsing;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.NodeList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dom4JParsing {
    public static void main(String[] args) throws DocumentException, IOException {
        //获取解析器
        SAXReader saxReader = new SAXReader();

        //获取xml文件输入流
        //InputStream is = Dom4jParsing.class.getClassLoader().getResourceAsStream("test.xml");
            InputStream is = Dom4JParsing.class.getResourceAsStream("/day2/student.xml");

        //如果为空直接返回
        if(is==null){
            return ;
        }

        //获取dom4j的document对象(org.dom4j.Document)
        Document document = saxReader.read(is);

        //System.out.println(document.asXML());//直接获取xml文件的所有内容

        //获取xml文档的根元素
        Element element = document.getRootElement();

        //增
        Element haha = element.addElement("haha");
        haha.addAttribute("num","15");
        haha.addText("sadasda");

        //查
        Attribute school_name = element.attribute("school_name");
        List<Element> elementByAttr = getElementByAttr(element, "student_name", "aa");
        List<Element> haha1 = getElementByName(element, "class");
        List<Element> elementByAttr1 = getElementByAttr(element, "class_name", "杰普实验");

        //删
        removeElements(elementByAttr);
        removeElements(elementByAttr1);

        //改
        Element e = element.element("haha");
        e.attribute("num").setValue("25");
        e.setText("good luck");

        //打印操作之后的xml文档缓存
        //打印处理指令
        getXmlInstructions(document);
        getHead(element);
        //打印根节点以及内部所有元素
        getBody(element);

        //将操作后的xml缓存生成新的xml文件
        /*FileWriter fw = new FileWriter("src/main/resources/day2/newTest.xml");
        document.write(fw);
        fw.close();*/

        //美化输出
        /*FileWriter fw = new FileWriter("src/main/resources/day2/newTest.xml");
        OutputFormat of = new OutputFormat();
        of.setIndent(true);
        of.setNewlines(true);
        XMLWriter xw = new XMLWriter(fw,of);
        xw.write(document);
        xw.flush();
        xw.close();*/

        //便捷式
        FileWriter fw4 = new FileWriter("src/main/resources/day2/newTest.xml");
        OutputFormat of3 = OutputFormat.createPrettyPrint();
        XMLWriter xw3 = new XMLWriter(fw4,of3);
        xw3.write(document);
        xw3.flush();
        xw3.close();
    }

    public static void removeElement(Element e){
        e.getParent().remove(e);
    }

    public static void removeElements(List<Element> list){
        for(Element e:list){
            removeElement(e);
        }
    }

    public static List<Element> getElementByAttr(Element e,String attrName,String attrValue){
        List<Element> list = new ArrayList<>();
        Attribute a = e.attribute(attrName);
        if(a!=null&&a.getValue().equals(attrValue)){
            list.add(e);
        }else {
            List elements = e.elements();
            elements.forEach(o->{
                Element element = (Element) o;
                list.addAll(getElementByAttr(element,attrName,attrValue));
            });
        }
        return list;
    }

    public static List<Element> getElementByName(Element e,String name){
        List<Element> list = new ArrayList<>();
        if(name.equals(e.getName())){
            list.add(e);
        }else{
            e.elements().forEach(o -> {
                list.addAll(getElementByName((Element) o,name));
            });
        }
        return list;
    }

    /*
    获取头部处理指令
    <?xml version="1.0" encoding="UTF-8" ?>该指令是默认指令，document不获取
    获取所有的处理指令List list = document.processingInstructions();
    <?xml-stylesheet type="text/css" href="test.css"?>
    pio.getTarget():xml-stylesheet
    pio.getName():xml-stylesheet
    pio.getText():type="text/css" href="test.css"
    pio.getStringValue():type="text/css" href="test.css"
    */
    public static void getXmlInstructions(Document document){
        System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        List list = document.processingInstructions();
        list.forEach(o->{
            ProcessingInstruction pio = (ProcessingInstruction) o;
            System.out.println("<?"+pio.getTarget()+" "+pio.getText()+"?>");
        });
    }
    /*
    获取该节点的节点名，节点属性，属性值
    */
    public static void getHead(Element e){
        System.out.print("<"+e.getName());
        //属性
        List attributes = e.attributes();
        attributes.forEach(o->{
            Attribute attribute = (Attribute) o;
            System.out.print(" "+attribute.getName()+"="+"\""+attribute.getValue()+"\"");
        });
        System.out.print(">");
    }
    public static void getBody(Element e){
        getHead(e);
        //获取子元素
        Iterator iterator = e.nodeIterator();
        while(iterator.hasNext()){
            Node node = (Node) iterator.next();
            if(node.getNodeType()==Node.ELEMENT_NODE){//如果子元素是一个元素节点
                getBody((Element) node);
            }else if(node.getNodeType()==Node.TEXT_NODE){//如果子元素是一个文本节点
                System.out.print(node.getText());
            }else if(node.getNodeType()==Node.COMMENT_NODE){
                System.out.print("<!--"+node.getText()+"-->");
            }
            System.out.println();
        }
        System.out.print("</"+e.getName()+">");
    }
}
