package com.craigmile.ali.jirahelper.Demos;

/*
 * Parse RSS file and extract title, link and project name and ref from each item
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

public class DOMDemo2 {

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
				  new FileInputStream("data/example_jira_search_rss.xml"));

		  Element rootElement = document.getDocumentElement();
		  
		  String attrValue = rootElement.getAttribute("version");
		  System.out.println( "This is document version: " + attrValue );

		  /*
		  item
		  	title
		  	link
		  	project @key | text()
		   */

		  NodeList nodes = rootElement.getElementsByTagName("item");
		  for(int i=0; i<nodes.getLength(); i++){
			  // System.out.println( node.getTextContent() );
			  Node node = nodes.item(i);
			  
			  if(node instanceof Element){
				  Element child = (Element) node;
				  NodeList titleNodes = child.getElementsByTagName("title");
				  if (titleNodes.getLength() >= 0) {
					  String title = titleNodes.item(0).getTextContent();
					  System.out.println( "title : " + title );
				  }
				  NodeList linkNodes = child.getElementsByTagName("link");
				  if (linkNodes.getLength() >= 0) {
					  String link = linkNodes.item(0).getTextContent();
					  System.out.println( "link : " + link );
				  }
				  NodeList projectNodes = child.getElementsByTagName("project");
				  if (projectNodes.getLength() >= 0) {
					  Element projectNode = (Element) projectNodes.item(0);
					  String project = projectNode.getTextContent();
					  String projectKey = projectNode.getAttribute("key");
					  System.out.println( "project : " + project + " (" + projectKey + ")");
				  }
				  
			  }
		  }

		  
		  /*
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
		  	*/

	  } catch (SAXException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
		
  }
}