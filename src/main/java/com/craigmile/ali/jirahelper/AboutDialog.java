package com.craigmile.ali.jirahelper;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

public class AboutDialog extends JDialog{

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public static final String APPLICATION_NAME = "Jira Helper";
	public static final String APPLICATION_VERSION = "v1.0.1";
	public static final String APPLICATION_AUTHOR = "Ali Craigmile";
	public static final String APPLICATION_EMAIL = "ali@craigmile.com";
	public static final String APPLCIATION_ORGANISATION = "craigmile.com";
	
	
	public AboutDialog() {
		setTitle("About " + APPLICATION_NAME);
		setSize(370,252);
		setMinimumSize(getSize());
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{368, 0};
		gridBagLayout.rowHeights = new int[]{40, 92, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel jiraLogoLabel = new JLabel(new ImageIcon(this.getClass().getResource("/resources/images/jira.gif")));
		jiraLogoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_jiraLogoLabel = new GridBagConstraints();
		gbc_jiraLogoLabel.anchor = GridBagConstraints.SOUTH;
		gbc_jiraLogoLabel.insets = new Insets(0, 0, 5, 0);
		gbc_jiraLogoLabel.gridx = 0;
		gbc_jiraLogoLabel.gridy = 0;
		getContentPane().add(jiraLogoLabel, gbc_jiraLogoLabel);
		GridBagConstraints gbc_aboutMsg = new GridBagConstraints();
		gbc_aboutMsg.anchor = GridBagConstraints.NORTH;
		gbc_aboutMsg.fill = GridBagConstraints.HORIZONTAL;
		gbc_aboutMsg.gridx = 0;
		gbc_aboutMsg.gridy = 1;
		Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
                "font-size: " + font.getSize() + "pt; } h1 { color: blue; text-align:center} p { text-align:center}";

        JEditorPane aboutMsg = new JEditorPane("text/html","<html>\n<h1>" + APPLICATION_NAME + " " + APPLICATION_VERSION + "</h1>\n<p><b>Your Jira issues, right there in your system tray.</b></p>\n<p>by " + APPLICATION_AUTHOR + " &lt;<a href=\"mailto:"+APPLICATION_EMAIL+"\">"+APPLICATION_EMAIL+"</a>&gt;</p>\n<p>(c) 2011 "+APPLCIATION_ORGANISATION+"</p>\n</html>");
				aboutMsg.setEditable(false);  
				aboutMsg.setOpaque(false);  
				((HTMLDocument)aboutMsg.getDocument()).getStyleSheet().addRule(bodyRule);
				
		aboutMsg.addHyperlinkListener(new HyperlinkListener() {  
			public void hyperlinkUpdate(HyperlinkEvent hle) {  
				if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {  
					System.out.println(hle.getURL());  
					UriLaucher l = new UriLaucher();
					try {
						l.launchUriInBrowser(new URI(hle.getURL().toString()));
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}  
			}
		});
		getContentPane().add(aboutMsg, gbc_aboutMsg);		
		
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#createRootPane()
	 * Close this dialog when press the ESC key. 
	 */
	protected JRootPane createRootPane() {
		JRootPane rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		AbstractAction actionListener = new AbstractAction() { 
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent actionEvent) { 
				setVisible(false);

			} 
		};
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", actionListener);

		return rootPane;
	}

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AboutDialog dialog = new AboutDialog();
					dialog.setVisible(true);


				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}
