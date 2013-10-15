package com.craigmile.ali.jirahelper.Demos;

/*
 * Simple DOM parsing of an XML file
 * (c) 2011 Ali Craigmile <ali@xgusties.com>
 */


import java.io.FileInputStream;
import java.io.IOException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
//import javax.xml.xpath.*;
//import javax.xml.parsers.*;
//import java.io.IOException;
//import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class DOMDemo {

  public static void main(String[] args) {

	  DocumentBuilderFactory builderFactory =
		  DocumentBuilderFactory.newInstance();

	  DocumentBuilder builder = null;
	  try {
		  builder = builderFactory.newDocumentBuilder();
	  } catch (ParserConfigurationException e) {
		  e.printStackTrace();  
	  }

	  Document document;
	  try {
		  document = builder.parse(
				  new FileInputStream("data/persons.xml"));

		  Element rootElement = document.getDocumentElement();
		  
		  String attrValue = rootElement.getAttribute("version");
		  System.out.println( "This is document version: " + attrValue );

		  NodeList nodes = rootElement.getChildNodes();

		  for(int i=0; i<nodes.getLength(); i++){
			  Node node = nodes.item(i);

			  if(node instanceof Element){
				  //a child element to process
				  //Element child = (Element) node;
				  //String attribute = child.getAttribute("width");
				  System.out.println( node.getTextContent() );
				  
			  }
		  }

	  } catch (SAXException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
		
  }
}