package com.craigmile.ali.jirahelper;

//TODO - re-factor to use a separate model / view

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;





public class PreferencesDialog extends JDialog  {

	public ResourceBundle applicationBundle =  ResourceBundle.getBundle("application");
	public ResourceBundle labelsBundle = ResourceBundle.getBundle("labels");

	
	class SaveButtonListener extends Observable implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			savePreferences();
			setVisible(false);
			//System.err.println("Saved Preferences, notifying observers...");

			setChanged();
			notifyObservers();
			//dispose();			
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final boolean HTTPS_PROXY_ENABLED_DEFAULT = false;
	private static final String HTTPS_PROXY_ENABLED_KEY = "httpsProxyEnabled";
	
	private static final String HTTPS_PROXY_HOST_DEFAULT = "";
	private static final String HTTPS_PROXY_HOST_KEY = "httpsProxyHost";
	
	private static final Integer HTTPS_PROXY_PORT_DEFAULT = 80;
	private static final String HTTPS_PROXY_PORT_KEY = "httpsProxyPort";

	//private static final String HTTPS_CERT_AUTHORITY_DEFAULT = "D:/workspace/jssecacerts";
	private static final String HTTPS_CERT_AUTHORITY_DEFAULT = "/home/alic/workspace/jssecacerts";
	private static final String HTTPS_CERT_AUTHORITY_KEY = "httpsCertAuthority";

	//private static final String HTTPS_CLIENT_CERT_DEFAULT = "D:/workspace/example.p12";
	private static final String HTTPS_CLIENT_CERT_DEFAULT = "/home/alic/workspace/example.p12";
	private static final String HTTPS_CLIENT_CERT_KEY = "httpsClientCert";

	private static final String HTTPS_CLIENT_PASSKEY_DEFAULT = "forge";
	private static final String HTTPS_CLIENT_PASSKEY_KEY = "httpsClientPasskey";

	private static final Integer JIRA_ISSUES_UPDATE_FREQUENCY_DEFAULT = 5;
	private static final String JIRA_ISSUES_UPDATE_FREQUENCY_KEY = "jiraIssuesUpdateFrequency";
	
	private static final String PREFERENCES_NODE = "preferencesdemo/prefs";
	private Preferences prefs = Preferences.userRoot().node(PREFERENCES_NODE);
	
	private JTextField textFieldHttpsProxyHost;
	private JSpinner spinnerHttpsProxyPort;
	private JTextField textFieldHttpsCertAuthority;
	private JTextField textFieldHttpsClientCert;
	private JPasswordField textFieldHttpsClientCertPasskey;
	private JCheckBox chckbxHttpsProxyEnabled;
	private JSpinner spinnerUpdateFrequency;

	private SaveButtonListener saveButtonListener;
	
	public PreferencesDialog() {
		setTitle("Jira Helper Settings");
		getContentPane().setLayout(new BorderLayout(0,0));
		setSize(360,320);
		
		JButton btnNewButton = new JButton("Save");
		getContentPane().add(btnNewButton);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Connection", null, panel_2, null);
		panel_2.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("118px"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("200px:grow"),
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("25px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblUpdateFrequencyminutes = new JLabel(labelsBundle.getString("update.frequency"));
		panel_2.add(lblUpdateFrequencyminutes, "2, 2, right, default");
		
		spinnerUpdateFrequency = new JSpinner();
		panel_2.add(spinnerUpdateFrequency, "4, 2");
		
		JLabel lblHttpsCertAuthority = new JLabel(labelsBundle.getString("https.cert.authority"));
		panel_2.add(lblHttpsCertAuthority, "2, 4, right, default");
		
		textFieldHttpsCertAuthority = new JTextField();
		textFieldHttpsCertAuthority.setColumns(10);
		panel_2.add(textFieldHttpsCertAuthority, "4, 4, fill, default");
		
		JLabel lblHttpsClientCert = new JLabel(labelsBundle.getString("https.client.cert"));
		panel_2.add(lblHttpsClientCert, "2, 6, right, default");
		
		textFieldHttpsClientCert = new JTextField();
		textFieldHttpsClientCert.setColumns(10);
		panel_2.add(textFieldHttpsClientCert, "4, 6, fill, default");
		
		JLabel lblHttpsClientPasskey = new JLabel(labelsBundle.getString("https.client.passkey"));
		panel_2.add(lblHttpsClientPasskey, "2, 8, right, default");
		
		textFieldHttpsClientCertPasskey = new JPasswordField();
		textFieldHttpsClientCertPasskey.setColumns(10);
		panel_2.add(textFieldHttpsClientCertPasskey, "4, 8, fill, default");
		
		chckbxHttpsProxyEnabled = new JCheckBox(labelsBundle.getString("https.proxy.enabled"));
		panel_2.add(chckbxHttpsProxyEnabled, "4, 12");
		
		JLabel lblNewLabel = new JLabel(labelsBundle.getString("https.proxy.host"));
		panel_2.add(lblNewLabel, "2, 14, right, default");
		
		textFieldHttpsProxyHost = new JTextField();
		panel_2.add(textFieldHttpsProxyHost, "4, 14, fill, default");
		textFieldHttpsProxyHost.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel(labelsBundle.getString("https.proxy.port"));
		panel_2.add(lblNewLabel_1, "2, 16, right, default");
		
		spinnerHttpsProxyPort = new JSpinner();
		spinnerHttpsProxyPort.setModel(new SpinnerNumberModel(80, 1, 1024, 1));
		panel_2.add(spinnerHttpsProxyPort, "4, 16");
		
		JButton saveButton = new JButton("Save");
		
		setSaveButtonListener(new SaveButtonListener());
		saveButton.addActionListener(getSaveButtonListener());
		
		panel_2.add(saveButton, "4, 18, left, default");
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Feeds", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JEditorPane lblNewLabel_2 = new JEditorPane();
		lblNewLabel_2.setEditable(false);
		lblNewLabel_2.setText(labelsBundle.getString("feeds.note"));
		panel.add(lblNewLabel_2);
		
		populateForm();
	}
		
	public Integer getUpdateFrequency() {
		return prefs.getInt(JIRA_ISSUES_UPDATE_FREQUENCY_KEY, JIRA_ISSUES_UPDATE_FREQUENCY_DEFAULT);
	}
	
	public String getHttpsCertAuthority() {
		return prefs.get(HTTPS_CERT_AUTHORITY_KEY, HTTPS_CERT_AUTHORITY_DEFAULT);
	}
	
	public String getHttpsClientCert() {
		return prefs.get(HTTPS_CLIENT_CERT_KEY, HTTPS_CLIENT_CERT_DEFAULT);
	}
	
	public String getHttpsClientCertPasskey() {
		return prefs.get(HTTPS_CLIENT_PASSKEY_KEY, HTTPS_CLIENT_PASSKEY_DEFAULT);
	}

	public boolean getHttpsProxyEnabled() {
		return prefs.getBoolean(HTTPS_PROXY_ENABLED_KEY, HTTPS_PROXY_ENABLED_DEFAULT);
	}
	
	public String getHttpsProxyHost() {
		return prefs.get(HTTPS_PROXY_HOST_KEY, HTTPS_PROXY_HOST_DEFAULT);
	}

	public Integer getHttpsProxyPort() {
		return prefs.getInt(HTTPS_PROXY_PORT_KEY, HTTPS_PROXY_PORT_DEFAULT);
	}	 

	public void populateForm() {

		spinnerUpdateFrequency.setValue( getUpdateFrequency() );

		textFieldHttpsCertAuthority.setText( getHttpsCertAuthority() );
		textFieldHttpsClientCert.setText( getHttpsClientCert() );
		textFieldHttpsClientCertPasskey.setText( getHttpsClientCertPasskey() );
		
		chckbxHttpsProxyEnabled.setSelected( getHttpsProxyEnabled() );
		textFieldHttpsProxyHost.setText( getHttpsProxyHost() );
		spinnerHttpsProxyPort.setValue( getHttpsProxyPort() );

	}


	public void savePreferences() {
		System.err.println("savePreferences()");
		prefs.putInt(JIRA_ISSUES_UPDATE_FREQUENCY_KEY, ((Integer)spinnerUpdateFrequency.getValue()));
		
		prefs.put(HTTPS_CERT_AUTHORITY_KEY, textFieldHttpsCertAuthority.getText());
		prefs.put(HTTPS_CLIENT_CERT_KEY, textFieldHttpsClientCert.getText());
		prefs.put(HTTPS_CLIENT_PASSKEY_KEY, new String(textFieldHttpsClientCertPasskey.getPassword()));
		
		prefs.putBoolean(HTTPS_PROXY_ENABLED_KEY, chckbxHttpsProxyEnabled.isSelected());
		prefs.put(HTTPS_PROXY_HOST_KEY, textFieldHttpsProxyHost.getText());
		prefs.putInt(HTTPS_PROXY_PORT_KEY, ((Integer) spinnerHttpsProxyPort.getValue()));
	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreferencesDialog dialog = new PreferencesDialog();
					dialog.setVisible(true);

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#createRootPane()
	 * Close this dialog when press the ESC key. 
	 */
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		Action actionListener = new AbstractAction() { 
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent actionEvent) { 
				setVisible(false);
			} 
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	public void setSaveButtonListener(SaveButtonListener saveButtonListener) {
		this.saveButtonListener = saveButtonListener;
	}

	public SaveButtonListener getSaveButtonListener() {
		return saveButtonListener;
	}


	

	
}
