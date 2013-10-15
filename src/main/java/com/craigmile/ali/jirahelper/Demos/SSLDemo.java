package com.craigmile.ali.jirahelper.Demos;

// My issues in JIRA
// https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=assignee+%3D+currentUser%28%29+AND+status+%3D+Open&tempMax=1000




import java.net.*;
import java.security.*;
//import java.net.*;
import java.io.*;
//import java.security.*;
import javax.net.ssl.*;

/**
 * Demonstration of how to handle SSL root stores, client certificates
 * and get a document over HTTPS. 
 * @author alic
 */
public class SSLDemo {

	public static void main(String[] args) {
		/*
		if (args.length != 3)
		{
			System.err.println("Syntax: x ca-cert.jks yourcert.p12 yourpassword");
			System.exit(1);
		}
		;
		String cacerts = args[0];
		String mycert = args[1];
		String passwd = args[2];
		*/

		String cacerts = "/home/alic/workspace/jssecacerts";
		String mycert = "/home/alic/workspace/example.p12";
		String passwd = "password";
		String urlToFetch = "https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=assignee+%3D+currentUser%28%29+AND+status+%3D+Open&tempMax=1000";
		
		try {
			// Step 1 - load the certificates I am willing to trust when connecting to. The 'smallest
			// you can get away here is just the 'root' certificate. 
			//
			KeyStore caStore = KeyStore.getInstance("JKS");
			InputStream caInput = new FileInputStream(new File(cacerts));
			caStore.load(caInput,"changeit".toCharArray());
			caInput.close();

			// Step 2 - load the private certificate as backed up from the browser.
			//
			KeyStore keyStore = KeyStore.getInstance("PKCS12");
			InputStream keyInput = new FileInputStream(new File(mycert));
			keyStore.load(keyInput, passwd.toCharArray());
			keyInput.close();

			// Step 3 - Initialise both the trust and cert factories with the right key material.
			//
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keyStore, passwd.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(caStore);

			// Step 4 - create a SSL context which has the private key and trust the trusted keys.
			//
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

			// And now continue as usual..
			//
			URL url = new URL(urlToFetch);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setSSLSocketFactory(context.getSocketFactory());

			InputStream ins = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(ins);
			BufferedReader in = new BufferedReader(isr);

			String thisLine;
			while ((thisLine = in.readLine()) != null)
			{
				System.out.println(thisLine);
			}
			
			in.close();
			
		} catch (Exception e)
		{
			System.err.println(e);
		}
	}
}

