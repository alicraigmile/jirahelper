package com.craigmile.ali.jirahelper;
// TODO - settings dialog  for feed uris
// TODO - set focus to current visible list after update


import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.craigmile.ali.jirahelper.Models.JiraIssueReference;


public class JiraHelper extends JFrame implements ActionListener, Observer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String APPLICATION_TITLE = "Jira Helper";

	private static final String[] RSS_FEED_NAMES = {"Assigned to me", "My EXAMPLE tickets", "EXAMPLE risks"};

	private static final String[] RSS_FEED_URLS = {
		"https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=assignee+%3D+currentUser%28%29+AND+status+%3D+Open&tempMax=1000",
		"https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=project+%3D+EXAMPLE+AND+resolution+%3D+Unresolved+AND+cf[10578]+%3D+currentUser%28%29&tempMax=1000",
		"https://jira.example.com/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=project+%3D+EXAMPLE+AND+issuetype+%3D+Risk+AND+resolution+%3D+Unresolved+ORDER+BY+priority+DESC%2C+key+DESC&tempMax=1000"
	};
	

	private JPanel contentPane;
	//TODO - destroy and re-create preferences frame each time so that it always has the good data
	private PreferencesDialog prefs = new PreferencesDialog();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		//System.err.println("Classpath is: " + System.getProperty("java.class.path"));

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JiraHelper frame = new JiraHelper();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void printComponent(final JComponent componenetToPrint){

		//final JComponent componenetToPrint = null;

		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setJobName(" Print Component ");

		pj.setPrintable (new Printable() {    
			public int print(Graphics pg, PageFormat pf, int pageNum) throws PrinterException{
				if (pageNum > 0){
					return Printable.NO_SUCH_PAGE;
				}

				Graphics2D g2 = (Graphics2D) pg;
				g2.translate(pf.getImageableX(), pf.getImageableY());
				componenetToPrint.paint(g2);
				return Printable.PAGE_EXISTS;
			}

		});
		if (pj.printDialog() == false)
			return;

		try {
			pj.print();
		} catch (PrinterException ex) {
			// handle exception
		}
	}



	//private Object[] jiraIssues = {"Loading..."};
	//private Object[] watchingIssues = {"Loading..."};
	private HashMap<Integer,ArrayList<Object>> jiraIssues = new HashMap<Integer, ArrayList<Object>>();
	//private HashMap<Integer,Object[]> jiraIssues = new HashMap<Integer, Object[]>();
	//private Object[] jiraIssues = new Object[]{};
			
	//private JList jiraIssuesList = null;
	private ArrayList<JList> jiraIssuesLists = new ArrayList<JList>();
	
	//private JList jiraWatchingList  =  null;
	private Button updateButton = null;
	private JLabel loadLabel = null; 
	private Timer timer = null;
//	private SwingWorker worker = null;
	//private Integer workerRunCount = 0;
	
	private JTabbedPane tabbedPane = null;

	/**
	 * Create the frame.
	 */
	public JiraHelper() {
		setTitle(APPLICATION_TITLE);
		setBounds(100, 100, 450, 300);

		if (SystemTray.isSupported()) {
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		} else {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		
		setupFeeds();
		setupTrayIcon();
		setupMenu();
		
		URL jiraIconURL =  this.getClass().getResource("/resources/images/jira.gif");
//		//System.err.println("URL: " + jiraIconURL.toString());
		Image image = Toolkit.getDefaultToolkit().getImage(jiraIconURL);
		setIconImage(image);
		

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel_1.add(tabbedPane);

		for (Integer tabIndex=0; tabIndex<RSS_FEED_NAMES.length; tabIndex++) {

			ColouredClickableListCellRenderer a = new ColouredClickableListCellRenderer();
	
							
			Object[] tabJiraIssues = jiraIssues.get((Object) tabIndex).toArray();
			//Object[] tabJiraIssues = new Object[]{};
			JList jiraIssuesList = new JList(tabJiraIssues);
			jiraIssuesLists.add(jiraIssuesList);
			//jiraIssuesLists.set(tabIndex, jiraIssuesList); // remember this list so that we can update it later
			
			jiraIssuesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jiraIssuesList.setCellRenderer(a);
			jiraIssuesList.setLayoutOrientation(JList.VERTICAL);
	
			//jiraIssuesList.addMouseListener(new JiraHelperMouseAdapter(jiraIssuesList, this));
			jiraIssuesList.addMouseListener(new MouseListener() {
	
				@Override
				public void mouseClicked(MouseEvent e){
					Integer tabIndex = 0;
					if(e.getClickCount() == 2){
						int index = jiraIssuesLists.get(tabIndex).locationToIndex(e.getPoint());
						ListModel dlm = jiraIssuesLists.get(tabIndex).getModel();
						Object item = dlm.getElementAt(index);;
						jiraIssuesLists.get(tabIndex).ensureIndexIsVisible(index);
	
						if (item instanceof JiraIssueReference) {
							JiraIssueReference jira = (JiraIssueReference) item;
							UriLaucher l = new UriLaucher();
							try {
								l.launchUriInBrowser(new URI(jira.getLink()));
							} catch (URISyntaxException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							System.out.println("Double clicked on " + item);
						}
					}
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
	
				}
			});
	
			
			//VerticalOnlyScroller jiraIssuesScrollPane = new VerticalOnlyScroller(); //jiraIssuesList);
			JScrollPane jiraIssuesScrollPane = new JScrollPane(jiraIssuesList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			//jiraIssuesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
			tabbedPane.addTab(RSS_FEED_NAMES[tabIndex], null, jiraIssuesScrollPane, null);
			jiraIssuesScrollPane.setViewportView(jiraIssuesList);
			
		}

		//jiraIssuesList.setMinimumSize(new Dimension(100,75));
		//jiraIssuesList.setPreferredSize(new Dimension(200,150));
		//jiraIssuesList.setMaximumSize(new Dimension(400,300));

		//jiraWatchingList = new JList(watchingIssues);
		//tabbedPane.addTab("Watching", null, jiraWatchingList, null);

		Panel panel = new Panel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		contentPane.add(panel);

		loadLabel = new JLabel(new ImageIcon(this.getClass().getResource("/resources/images/load.gif")));
		loadLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(loadLabel);
		loadLabel.setVisible(false);

		updateButton = new Button("Update Issues");
		updateButton.setActionCommand("update");
		updateButton.addActionListener(this);
		//updateButton.setMnemonic(KeyEvent.VK_U);
		panel.add(updateButton);
	
		Integer updateJiraIssuesListFrequency = prefs.getUpdateFrequency();//minutes
		//System.err.println("Update timer set with frequency of " + updateJiraIssuesListFrequency.toString() + " minutes.");

		timer = new Timer(updateJiraIssuesListFrequency*60*1000, this); // every 5 minutes
		timer.setActionCommand("timer");
		timer.start();

		//		//Our user probably can't wait that long for their first data, so update it now too..
		this.startLatestJiraIssuesWorker();		
	}


	private void setupFeeds() {
		
	 	for (Integer feedIndex=0; feedIndex<RSS_FEED_NAMES.length; feedIndex++) {
			ArrayList<Object> myIssues = new ArrayList<Object>();	
			
			String feedName = RSS_FEED_NAMES[feedIndex];
			myIssues.add("Loading '" + feedName + "' feed...");
			jiraIssues.put(feedIndex, myIssues);
		}

	}
	
	private void setupMenu() {

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		JMenuItem updateMenuItem = new JMenuItem("Update Issues");
		updateMenuItem.setActionCommand("update");
		updateMenuItem.addActionListener(this);		
		updateMenuItem.setMnemonic(KeyEvent.VK_U);
		menu.add(updateMenuItem);

		JMenuItem settingsMenuItem = new JMenuItem("Settings");
		settingsMenuItem.setActionCommand("showSettings");
		settingsMenuItem.addActionListener(this);		
		settingsMenuItem.setMnemonic(KeyEvent.VK_E);
		menu.add(settingsMenuItem);

		JSeparator separator = new JSeparator();
		menu.add(separator);

		JMenuItem printMenuItem = new JMenuItem("Print"); 
		printMenuItem.setActionCommand("print");
		printMenuItem.addActionListener(this);		
		printMenuItem.setMnemonic(KeyEvent.VK_P);
		menu.add(printMenuItem);

		JSeparator separator_1 = new JSeparator();
		menu.add(separator_1);

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("exit");
		exitMenuItem.addActionListener(this);		
		exitMenuItem.setMnemonic(KeyEvent.VK_X);
		menu.add(exitMenuItem);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		menuBar.add(helpMenu);

		JMenuItem aboutMenuItem = new JMenuItem("About " + APPLICATION_TITLE);
		aboutMenuItem.setActionCommand("about");
		aboutMenuItem.addActionListener(this);		
		aboutMenuItem.setMnemonic(KeyEvent.VK_A);
		helpMenu.add(aboutMenuItem);
		
	}

	private void setupTrayIcon() {

		final JFrame myFrame = this;
		
		if (SystemTray.isSupported()) {

			//System.err.println("TrayIcon IS supported.");

			SystemTray tray = SystemTray.getSystemTray();

			URL imageURL =  getClass().getResource("/resources/images/jira.gif");
			Image image = Toolkit.getDefaultToolkit().getImage(imageURL);

			ActionListener exitListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Exiting...");
					System.exit(0);
				}
			};

			PopupMenu popup = new PopupMenu();
			MenuItem defaultItem = new MenuItem("Exit");
			defaultItem.addActionListener(exitListener);
			popup.add(defaultItem);

			TrayIcon trayIcon = new TrayIcon(image, "Loading...", popup);
			
			trayIcon.addMouseListener(new MouseListener() {

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					Integer clickCount = e.getClickCount();
					if (clickCount == 2) {
						if (! myFrame.isVisible()) {
							myFrame.setVisible(true);
						}						
						myFrame.toFront();
					} else {
						//TODO - show popup menu on 1 left button click

						//TrayIcon trayIcon = (TrayIcon) e.getSource()
						//trayIcon.getPopupMenu();	
						//evt.getComponent(), evt.getX(), evt.getY()					
						//trayIcon.displayMessage("Caption", "This is an example of Notifications", TrayIcon.MessageType.INFO);
					}
	
				}
			});

			trayIcon.setImageAutoSize(true);

			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				//System.err.println("TrayIcon could not be added.");
			}			


		} else {

			// System Tray is not supported
			//System.err.println("TrayIcon not supported.");

		}
		
	}

	private ArrayList<Object> getLatestJiraIssues(Integer feedIndex) {

		String httpsCertAuthority = prefs.getHttpsCertAuthority();
		String httpsClientCert = prefs.getHttpsClientCert();
		String httpsClientPasskey = prefs.getHttpsClientCertPasskey();
		Boolean httpsProxyEnabled = prefs.getHttpsProxyEnabled();
		String httpsProxyHost = prefs.getHttpsProxyHost();
		Integer httpsProxyPort = prefs.getHttpsProxyPort();

		ArrayList<Object> data = new ArrayList<Object>();				

		SslRssFetcher rs = new SslRssFetcher(httpsCertAuthority, httpsClientCert, httpsClientPasskey);

		// Switch proxies on or off in the VM depending on preference
		if (httpsProxyEnabled) {
			//System.err.println("HTTPS proxying is turned on");
			System.setProperty("https.proxyHost", httpsProxyHost);
			System.setProperty("https.proxyPort", httpsProxyPort.toString());
		} else {
			//System.err.println("HTTPS proxying is turned off");
			System.clearProperty("https.proxyHost");
			System.clearProperty("https.proxyPort");			
		}

		String rssFeed;
		try {
			//System.err.println("Loading " + RSS_FEED_URLS[feedIndex]);
			rssFeed = rs.fetchSslRss(RSS_FEED_URLS[feedIndex]);

			RssReader rssReader = new RssReader();

			ArrayList<JiraIssueReference> items = 
				(ArrayList<JiraIssueReference>) rssReader.parse(rssFeed);

			Iterator<JiraIssueReference> it = items.iterator();
			while (it.hasNext()) {
				JiraIssueReference item = (JiraIssueReference) it.next();
				data.add(item);	
			}

			if (items.size() == 0) {
				data.add("No jira issues were found");		
			}

		} catch (Exception e) {
			data.add("There was a problem: " + e.getClass().getCanonicalName());		
			data.add("See STDERR for more details.");		
			e.printStackTrace();
		}

		return data;
	}


	private void startLatestJiraIssuesWorker() {

		SwingWorker<ArrayList<Object>, Void> worker = new SwingWorker<ArrayList<Object>, Void>() {
			@Override
			public ArrayList<Object> doInBackground() {		    	
				
				//Disable the UI
				updateButton.setEnabled(false);
				loadLabel.setVisible(true);
				
				ArrayList<Object> latestJiraIssues = new ArrayList<Object>();
				
				for (Integer feedIndex=0; feedIndex<RSS_FEED_NAMES.length; feedIndex++) {
					//System.err.println("start loop: " + feedIndex);
				//	Integer feedIndex = 1;					
					ArrayList<Object> feedLatestJiraIssues = getLatestJiraIssues(feedIndex);
					//System.err.println("feedLatestJiraIssues: " + feedLatestJiraIssues.toArray().length);
					//feedLatestJiraIssues.add("Worker run count = " + workerRunCount); // number of times loaded
					//latestJiraIssues.set(feedIndex, feedLatestJiraIssues);
					latestJiraIssues.add(feedLatestJiraIssues);
					//System.err.println("stop loop: " + feedIndex);
				}
				
				return latestJiraIssues;
			}

			@Override
			public void done() {
				//TODO - turn off loading gfx
				try {
//					//System.err.println("done()");
					ArrayList<Object> latestJiraIssues = get();					
					
					Integer allTabsJiraIssueCount = 0;
					
					for (Integer tabIndex = 0; tabIndex < tabbedPane.getTabCount(); tabIndex++) {
							
						//System.err.println("Updating tab " + tabIndex);
						
						ArrayList<Object> myJiraIssues = (ArrayList<Object>) latestJiraIssues.get(tabIndex); 
						
						Integer selectedIndex = jiraIssuesLists.get(tabIndex).getSelectedIndex(); //preserve user selection
						jiraIssuesLists.get(tabIndex).setListData(myJiraIssues.toArray());  //change data
						jiraIssuesLists.get(tabIndex).setSelectedIndex(selectedIndex);  //preserve user selection
						jiraIssuesLists.get(tabIndex).requestFocus();
	
						// Count the Jira issues
						Integer jiraIssuesCount = 0;
						for (Integer i = 0; i<myJiraIssues.size(); i++) {
							if (myJiraIssues.get(i) instanceof JiraIssueReference) {
								jiraIssuesCount++;
							}
						}
						allTabsJiraIssueCount += jiraIssuesCount;
	
						// Update the tab to reflect how many tickets there are									
						tabbedPane.setTitleAt(tabIndex, RSS_FEED_NAMES[tabIndex] + " (" + jiraIssuesCount + ")");
					}

					// Re-enable the UI
					loadLabel.setVisible(false);
					updateButton.setEnabled(true);

					
					// Update the tray icon toolTip to show the number of issues found
					String toolTip = allTabsJiraIssueCount + " Jira issues in total";
					SystemTray tray = SystemTray.getSystemTray();
					tray.getTrayIcons()[0].setToolTip(toolTip);
					
					
					
				} catch (InterruptedException ignore) {
					
				} catch (java.util.concurrent.ExecutionException e) {
					String why = null;
					Throwable cause = e.getCause();
					if (cause != null) {
						why = cause.getMessage();
					} else {
						why = e.getMessage();
					}
					//System.err.println("Error getting latest Jira Issues : " + why);
				}

				// TODO - check that the timer has the correct duration (i.e. if config has changed...)
				timer.restart(); // wait full 5 mins after completion before looking again
			}
		};
		worker.execute();
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		if (actionCommand == "update") { this.actionUpdatePerformed(e); }
		else if (actionCommand == "about") { this.actionAboutPerformed(e); }
		else if (actionCommand == "exit") { this.actionExitPerformed(e); }
		else if (actionCommand == "print") { this.actionPrintPerformed(e); }
		else if (actionCommand == "showSettings") { this.actionShowSettingsPerformed(e); }
		else if (actionCommand == "timer") { this.actionTimerPerformed(e); }
	}


	private void actionShowSettingsPerformed(ActionEvent e) {

		//System.err.println("show settings!");

		PreferencesDialog prefs = new PreferencesDialog();
		prefs.setLocationRelativeTo(this);
		prefs.setModal(true);
//		Action saveListener = new AbstractAction() { 
//			public void actionPerformed(ActionEvent actionEvent) { 
//				//setVisible(false);
//				System.err.println("actionPerformed inside saveListener");
//			} 
//		};
		//prefs.setSaveListener(saveListener);
		//prefs.setSaveListener(saveListener);
		prefs.getSaveButtonListener().addObserver(this);
		prefs.setVisible(true);

		//when we're done, re-initialiase the prefs
		//prefs = new PreferencesDialog();

		//prefs.setEnabled(true);
		//prefs.setVisible(true);

	}

	private void actionPrintPerformed(ActionEvent e) {
		//System.err.println("print!");
		// TODO - refactor actionPrintPerformed out of the event handler thread
		Integer tabIndex = 0;
		this.printComponent(this.jiraIssuesLists.get(tabIndex)); // jiraWatchingList
	}

	private void actionExitPerformed(ActionEvent e) {
		// no niceties, just get out#
		//System.err.println("Exit!");
		this.dispose();
		System.exit(0);

	}

	private void actionUpdatePerformed(ActionEvent e) {
		//System.err.println("Update!");
		//NOTE: next 2 lines rolled into startLatestJiraIssuesWorker
		//this.updateButton.setEnabled(false);
		//this.loadLabel.setVisible(true);		
		this.startLatestJiraIssuesWorker();

	}

	private void actionAboutPerformed(ActionEvent e) {
		//System.err.println("about!");
		AboutDialog about = new AboutDialog();
		about.setLocationRelativeTo(this);
		about.setModal(true);
		about.setVisible(true);

	}

	private void actionTimerPerformed(ActionEvent e) {
		//TODO = re-add this check somehow
//		if (this.worker != null && ! this.worker.isDone()) {
//			return;
//		}

		//if it's not busy and we've waited long enough... start it up again
		this.startLatestJiraIssuesWorker();	

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		//re-initialise preferences
		prefs = new PreferencesDialog();
	}




}
