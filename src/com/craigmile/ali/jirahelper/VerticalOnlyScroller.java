package com.craigmile.ali.jirahelper;

import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.Scrollable;

public class VerticalOnlyScroller extends JScrollPane implements Scrollable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Idea found here:
	 * http://forums.sun.com/thread.jspa?messageID=10887295
	 * Coded by hand to suit my needs.
	 *
	 * This class causes whatever is put into it to get the same size as itself.
	 */

	@Override
	public Dimension getPreferredScrollableViewportSize() {

		// The default implementation of this method.
		return getPreferredSize();
	}

	@Override
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {

		// Pixels to represent SEVERAL rows.
		// Arbitrary number for kicks. Not actually needed
		// in my implementation.
		return 40;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {

		// Don't enforce height
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {

		// Do enforce width
		return true;
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {

		// Amount to expose a single row
		// Arbitrary number for kicks. Not actually needed
		// in my implementation.
		return 15;
	}

}
