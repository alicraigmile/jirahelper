package com.craigmile.ali.jirahelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import com.craigmile.ali.jirahelper.Models.JiraIssueReference;

public class ColouredClickableListCellRenderer extends JEditorPane implements ListCellRenderer {

	private static final Font font = UIManager.getFont("Label.font");
	private static Integer h2Size = font.getSize() + 1;
	protected static String bodyRule = 
		"body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }" + 
		"h2 { font-size: " + h2Size + "pt; margin: 0px; padding: 0px; }" +
		"p { margin: 0px; padding: 0px; }";

	//JEditorPane aboutMsg = new JEditorPane("text/html","<html>\n<h1>" + APPLICATION_NAME + " " + APPLICATION_VERSION + "</h1>\n<p><b>Your Jira issues, right there in your system tray.</b></p>\n<p>by Ali Craigmile &lt;<a href=\"mailto:ali@craigmile.com\">ali@craigmile.com</a>&gt;</p>\n<p>(c) 2013 craigmile.com</p>\n</html>");

	/**
	 * 
	 */
	private static final long serialVersionUID = 7790201062860945405L;

	public ColouredClickableListCellRenderer() {		
		setContentType("text/html");
		setEditable(false);  
		setOpaque(true);  //allows background colours to shine through
		//setMinimumSize(new Dimension(0,0));

		((HTMLDocument)this.getDocument()).getStyleSheet().addRule(bodyRule);

		addHyperlinkListener(new HyperlinkListener() {  
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
		
//		setEditorKit(new HTMLEditorKit(){ 
//
//			private static final long serialVersionUID = 1L;
//
//			@Override 
//			public ViewFactory getViewFactory(){ 
//
//				return new HTMLFactory(){ 
//					public View create(Element e){ 
//						View v = super.create(e); 
//						if(v instanceof InlineView){ 
//							return new InlineView(e){ 
//								public int getBreakWeight(int axis, float pos, float len) { 
//									return GoodBreakWeight; 
//								} 
//								public View breakView(int axis, int p0, float pos, float len) { 
//									if(axis == View.X_AXIS) { 
//										checkPainter(); 
//										int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len); 
//										if(p0 == getStartOffset() && p1 == getEndOffset()) { 
//											return this; 
//										} 
//										return createFragment(p0, p1); 
//									} 
//									return this; 
//								} 
//							}; 
//						} 
//						else if (v instanceof ParagraphView) { 
//							return new ParagraphView(e) { 
//								protected SizeRequirements calculateMinorAxisRequirements(int axis, SizeRequirements r) { 
//									if (r == null) { 
//										r = new SizeRequirements(); 
//									} 
//									float pref = layoutPool.getPreferredSpan(axis); 
//									float min = layoutPool.getMinimumSpan(axis); 
//									// Don't include insets, Box.getXXXSpan will include them. 
//									r.minimum = (int)min; 
//									r.preferred = Math.max(r.minimum, (int) pref); 
//									r.maximum = Integer.MAX_VALUE; 
//									r.alignment = 0.5f; 
//									return r; 
//								} 
//
//							}; 
//						} 
//						return v; 
//					} 
//				}; 
//			} 
//		}); 		

	}

	@Override
	public Component getListCellRendererComponent(JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {

		//System.out.println("getListCellRendererComponent() called");

		if (value instanceof JiraIssueReference) {
			JiraIssueReference jira = (JiraIssueReference) value;
			setText(
					"<html>" + 
					"<h2>" + jira.getTitle() + " (" + jira.getType() + ")" + "</h2>" +
					"<p><a href=\""+ jira.getLink() +"\">" + jira.getLink() +"</p>\n" +
					//	jira.getDescription() + "\n" +
					//"<hr>\n" +
					"</html>"
					//jira.getTitle() + " (" + jira.getType() + ")\r" +
					//jira.getLink()
			);
		} else {
			setText(value.toString());		
		}
		
		Dimension d = getPreferredScrollableViewportSize();
		setPreferredSize(new Dimension(d.width, 120));
		//setMinimumSize(new Dimension(d.width, 20));
		//setMaximumSize(new Dimension(d.width, 6000));
		
		Color background;
		Color foreground;

		// check if this cell represents the current DnD drop location
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null
				&& !dropLocation.isInsert()
				&& dropLocation.getIndex() == index) {

			background = Color.BLUE;
			foreground = Color.WHITE;

			// check if this cell is selected
		} else if (isSelected) {
			Color color = UIManager.getColor("Panel.background");
			background = color;
			foreground = Color.BLACK;

			// unselected, and not the DnD drop location
		} else {
			background = Color.WHITE;
			foreground = Color.BLACK;
		};



		setBackground(background);
		setForeground(foreground);

		return this;

	}

}
