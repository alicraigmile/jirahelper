package com.craigmile.ali.jirahelper;

//import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
//import java.net.URI;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;


import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.craigmile.ali.jirahelper.Models.JiraIssueReference;
import com.craigmile.ali.jirahelper.Models.JiraProjectReference;

import javax.xml.parsers.*;


/**
 * A Parser for Jira created RSS feeds.
 * @author Ali Craigmile <ali@craigmile.com>
 * @see {@link JiraIssueReference}
 */

public class RssReader {

	/**
	 * @param rssFeed A string containing the XML data.
	 * @return List<{@link JiraIssueReference}>
	 */
	public List<JiraIssueReference> parse(String rssFeed)  {

		//ArrayList<String> foundItemsList = new ArrayList<String>();
		ArrayList<JiraIssueReference> foundJiraTickets = new ArrayList<JiraIssueReference>();

		DocumentBuilderFactory builderFactory =
			DocumentBuilderFactory.newInstance();

		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();  
		}

		Document document = null;
		try {

			//FileInputStream is = new FileInputStream("data/example_jira_search_rss.xml"); 
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(rssFeed));

			document = builder.parse( is );


			Element rootElement = document.getDocumentElement();

			//String attrValue = rootElement.getAttribute("version");
			//System.out.println( "This is document version: " + attrValue );


			NodeList nodes = rootElement.getElementsByTagName("item");
			for(int i=0; i<nodes.getLength(); i++){
				// System.out.println( node.getTextContent() );
				Node node = nodes.item(i);

				JiraIssueReference jira = new JiraIssueReference();

				if(node instanceof Element){
					Element child = (Element) node;
					NodeList titleNodes = child.getElementsByTagName("title");
					if (titleNodes.getLength() >= 0) {
						String title = titleNodes.item(0).getTextContent();
						//System.out.println( "title : " + title );
						//foundItemDescription += "title : " + title;
						jira.setTitle(title);
					}
					NodeList linkNodes = child.getElementsByTagName("link");
					if (linkNodes.getLength() >= 0) {
						String link = linkNodes.item(0).getTextContent();
						//System.out.println( "link : " + link );
						//foundItemDescription += "link : " + link;
						//jira.setUri(new URI(link));
						jira.setLink(link);
					}
					NodeList typeNodes = child.getElementsByTagName("type");
					if (typeNodes.getLength() >= 0) {
						String type = typeNodes.item(0).getTextContent();
						//System.out.println( "link : " + link );
						//foundItemDescription += "link : " + link;
						//jira.setUri(new URI(link));
						jira.setType(type);
						
						String typeIconUrl = typeNodes.item(0).getAttributes().getNamedItem("iconUrl").getTextContent();
						jira.setTypeIconUrl(typeIconUrl );
					}
					NodeList summaryNodes = child.getElementsByTagName("summary");
					if (summaryNodes.getLength() >= 0) {
						String summary = summaryNodes.item(0).getTextContent();
						jira.setSummary(summary);
					}
					NodeList descriptionNodes = child.getElementsByTagName("description");
					if (descriptionNodes.getLength() >= 0) {
						String description = descriptionNodes.item(0).getTextContent();
						jira.setDescription(description);
					}
					NodeList projectNodes = child.getElementsByTagName("project");
					if (projectNodes.getLength() >= 0) {
						Element projectNode = (Element) projectNodes.item(0);
						String projectName = projectNode.getTextContent();
						String projectKey = projectNode.getAttribute("key");
						jira.setProject(new JiraProjectReference(projectName, projectKey));
					}

				}

				//foundItemsList.add(foundItemDescription);
				foundJiraTickets.add(jira);
			}


		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//return foundItemsList;
		return foundJiraTickets;


	}
}