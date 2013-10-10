package com.craigmile.ali.jirahelper.Demos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.craigmile.ali.jirahelper.Models.Model;


public class SwingDemo0SaveListener implements ActionListener {
	
	private SwingDemo0 view;
	private Model model;
	private Integer number = 0;
	
	public SwingDemo0SaveListener(SwingDemo0 v, Model m) {
		view = v;
		model = m;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		number++;
		String text = "Save Clicked " + number + " times";
		model.setText(text); 
		
		
		view.jlab.setText( model.getText() );
		
	}

}
