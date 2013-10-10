package com.craigmile.ali.jirahelper.Demos;

import org.w3c.dom.*;
import javax.xml.xpath.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.SAXException;

public class XPathDemo {

  public static void main(String[] args) 
  		throws ParserConfigurationException, SAXException, 
		IOException, XPathExpressionException {

    DocumentBuilderFactory domFactory = 
    	DocumentBuilderFactory.newInstance();
	domFactory.setNamespaceAware(true); 
    DocumentBuilder builder = domFactory.newDocumentBuilder();
    Document doc = builder.parse("data/persons.xml");
    
    XPath xpath = XPathFactory.newInstance().newXPath();
       // XPath Query for showing all nodes values (if node has a name containing 'ali')
    XPathExpression expr = xpath.compile("//person[contains(name,'Ali')]/*/text()");
    Object result = expr.evaluate(doc, XPathConstants.NODESET);
    
    NodeList nodes = (NodeList) result;
    for (int i = 0; i < nodes.getLength(); i++) {
    	System.out.println(nodes.item(i).getNodeValue()); 
    }
  }
}