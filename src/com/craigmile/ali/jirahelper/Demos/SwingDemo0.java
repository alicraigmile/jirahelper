package com.craigmile.ali.jirahelper.Demos;

import java.awt.*;

import javax.swing.*;

import com.craigmile.ali.jirahelper.Models.Model;


/**
 * Demonstration of a simple Swing application.
 * @author alic
 *
 */
public class SwingDemo0 {
	
	JLabel jlab;

	public void display(){
		//create frame
		JFrame jfrm=new JFrame("Jira Helper by Ali Craigmile");

		//set size
		jfrm.setSize(600,300);
		
		//wen closed?
		jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//set the layout
		jfrm.setLayout(new FlowLayout());
		
		//set visible
		jfrm.setVisible(true);
		
		jlab = new JLabel("Jira Helper will list all Jira issues assigned to you, or which you are watching and let you know when the status changes.");
		
		jfrm.add(jlab);
		
		Model model = new Model("Hello World!");
		
		JButton	saveButton = new JButton("Save");
		saveButton.addActionListener( new SwingDemo0SaveListener( this, model ) ) ;
		jfrm.add(saveButton);
	}
		
		/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				SwingDemo0 obj=new SwingDemo0();
				obj.display();
			}
		});

	}

	
}
