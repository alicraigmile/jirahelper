package com.craigmile.ali.jirahelper.Models;

/**
 * Represents a reference to a Jira Project
 * @author Ali Craigmile <ali@xgusties.com>
 *
 */
public class JiraProjectReference {

	public String key;
	public String name;
	
	public JiraProjectReference(String name, String key) {
		super();
		this.name = name;
		this.key = key;
	}
	/**
	 * @return 
	 */
	public String getKey() {
		return key;
	}
	
	public String getName() {
		return name;
	}
	/**
	 * @param key Identifier for this project in Jira.
	 * @see {@link getUri} for the full URI.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * @param name Name of this project in Jira.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return URI of this project in Jira.
	 */
	public String getUri() {
		return "https://jira.example.com/browse/" + key;
	}
	
	@Override
	public String toString() {
		return "JiraProjectReference [name=" + name + "]";
	}
	
	
	
}
