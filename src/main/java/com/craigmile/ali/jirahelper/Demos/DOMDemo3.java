package com.craigmile.ali.jirahelper.Demos;

/*
 * Parse RSS string and extract title, link and project name and ref from each item
 * (c) 2011 Ali Craigmile <ali@xgusties.com>
 */

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
//import javax.xml.xpath.*;
//import javax.xml.parsers.*;
//import java.io.IOException;
//import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class DOMDemo3 {

  public static void main(String[] args) {

	  String rssFeed =
	"<rss version=\"0.92\">" +
    "<channel>" +
    "    <title>BBC JIRA</title>" +
    "    <link>https://jira.dev.example.com/secure/IssueNavigator.jspa?reset=true&amp;jqlQuery=assignee+%3D+currentUser%28%29+AND+status+%3D+Open</link>" +
"    <description>An XML representation of a search request</description>" +
"            <language>en-uk</language> " +
"            <build-info>" +
"            <version>4.0.2</version>" +
"            <build-number>472</build-number>" +
"            <build-date>14-02-2010</build-date>" +
"            <edition>enterprise</edition>" +
"        </build-info>" +
"<item>" +
"            <title>[EXAMPLE-80] Time field in Broadcast Information displayed at GMT and not BST</title>" +
"                <link>https://jira.example.com/browse/EXAMPLE-80</link>" +
"                <project id=\"10650\" key=\"EXAMPLE\">Example Project</project>" +
"</item>" +
"<item>" +
"            <title>[EXAMPLE-81] Time field in Broadcast Information displayed at GMT and not BST</title>" +
"                <link>https://jira.example.com/browse/EXAMPLE-80</link>" +
"                <project id=\"10650\" key=\"EXAMPLE\">Example Project</project>"  +
"</item>" +
"<item>" +
"            <title>[EXAMPLE-82] Time field in Broadcast Information displayed at GMT and not BST</title>" +
"                <link>https://jira.example.com/browse/EXAMPLE-80</link>" +
"                <project id=\"10650\" key=\"EXAMPLE\">Example Project</project>" +
"</item>" +
		"</channel>" + 
		"</rss>";
	  
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
		  
		  //FileInputStream is = new FileInputStream("data/example_jira_search_rss.xml"); 
		  InputSource is = new InputSource();
		  is.setCharacterStream(new StringReader(rssFeed));

		  document = builder.parse( is );
				  

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