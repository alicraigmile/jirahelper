package com.craigmile.ali.jirahelper;

// My issues in JIRA
// https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=assignee+%3D+currentUser%28%29+AND+status+%3D+Open&tempMax=1000

import java.net.*;
import java.security.*;
//import java.security.cert.CertificateException;
//import java.net.*;
import java.io.*;
//import java.security.*;
import javax.crypto.BadPaddingException;
import javax.net.ssl.*;

/**
 * Fetch an RSS feed over HTTP+SSL (with Cert authority & Client Certs) 
 * @author Ali Craigmile <ali@craigmile.com>
 */
public class SslRssFetcher {

	private static final Integer HTTPS_TIMEOUT_SECONDS = 3; 

	private String caCerts = "";
	private String myCert = "";
	private String password = "";


	public String getCaCerts() {
		return caCerts;
	}


	public void setCaCerts(String caCerts) {
		this.caCerts = caCerts;
	}


	public String getMyCert() {
		return myCert;
	}


	public void setMyCert(String myCert) {
		this.myCert = myCert;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String fetchSslRss(String urlToFetch) throws SslRssFetcherException, BadPaddingException, SocketTimeoutException, IOException  {
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

		String rssFeed = "";


		// Step 1 - load the certificates I am willing to trust when connecting to. The 'smallest
		// you can get away here is just the 'root' certificate.
		//
		KeyStore caStore;
		try {
			caStore = KeyStore.getInstance("JKS");
			InputStream caInput = new FileInputStream(new File(caCerts));
			caStore.load(caInput,"changeit".toCharArray());
			caInput.close();
		} catch (Exception e) {
			throw new CaCertException(e);
		}


		// Step 2 - load the private certificate as backed up from the browser.
		//
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
			InputStream keyInput = new FileInputStream(new File(myCert));
			keyStore.load(keyInput, password.toCharArray());
			keyInput.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if ((e instanceof IOException) && (e.getCause() instanceof BadPaddingException)) {				
				System.err.println("wrong password");
				//e.printStackTrace();
				throw new ClientCertPasskeyException(e);
			} else {
				throw new ClientCertException(e);
			}
		}		

		try {
			// Step 3 - Initialise both the trust and cert factories with the right key material.
			//
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(keyStore, password.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(caStore);

			// Step 4 - create a SSL context which has the private key and trust the trustd keys.
			//
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

			// And now continue as usual..
			//
			URL url = new URL(urlToFetch);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setConnectTimeout(HTTPS_TIMEOUT_SECONDS * 1000); //miliseconds
			con.setSSLSocketFactory(context.getSocketFactory());

			InputStream ins = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(ins);
			BufferedReader in = new BufferedReader(isr);

			String thisLine;
			while ((thisLine = in.readLine()) != null)
			{
				//System.out.println(thisLine);
				rssFeed += thisLine;
			}
			in.close();


		} catch (SocketTimeoutException e) {
			throw new SocketTimeoutException();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rssFeed;
	}


	public SslRssFetcher(String caCerts, String myCert, String password) {
		super();
		this.caCerts = caCerts;
		this.myCert = myCert;
		this.password = password;
	}
}

