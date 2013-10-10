package com.craigmile.ali.jirahelper;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class UriLaucher {

	public void launchUriInBrowser(URI uri) {
		if (Desktop.isDesktopSupported()) {
			//System.err.println("Launching: " + uri);
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(uri);
				}
				catch(IOException ioe) {
					ioe.printStackTrace();
				}
			} else {
				System.err.println("BROWSE is not supported in this VM :(");				
			}
		} else {
			System.err.println("Desktop commands are not supported in this VM :(");
		}
	}

}
