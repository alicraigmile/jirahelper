package com.craigmile.ali.jirahelper.Models;

import com.craigmile.ali.jirahelper.RssReader;

//import java.net.URI;

/**
 * A reference to an Issue in Jira.
 * This is returned by the {@link RssReader} when parsng Jira's XML feeds.
 * @author Ali Craigmile <ali@gusties.com>
 */

public class JiraIssueReference {

	public String title;
	private String link;
	private String summary;
	private String description;
	private String type;
	private String typeIconUrl;
	public JiraProjectReference project;

	public String getTitle() {
		return title;
	}
	/**
	 * The title of the Jira Issue.
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public JiraProjectReference getProject() {
		return project;
	}
	/**
	 * A reference to the project which this Jira issue belongs to.
	 * @param project
	 * @see {@link JiraProjectReference}
	 */
	public void setProject(JiraProjectReference project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "JiraIssueReference [project=" + project + ", title=" + title
		+ ", link=" + link + "]";
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getLink() {
		return link;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setTypeIconUrl(String typeIconUrl) {
		this.typeIconUrl = typeIconUrl;
	}
	public String getTypeIconUrl() {
		return typeIconUrl;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getSummary() {
		return summary;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}




}
