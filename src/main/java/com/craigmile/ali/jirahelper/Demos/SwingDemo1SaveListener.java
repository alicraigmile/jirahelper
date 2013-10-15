package com.craigmile.ali.jirahelper.Demos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.craigmile.ali.jirahelper.Models.Model;


public class SwingDemo1SaveListener implements ActionListener {
	
	private SwingDemo1 view;
	private Model model;
	private Integer number = 0;
	
	public SwingDemo1SaveListener(SwingDemo1 v, Model m) {
		view = v;
		model = m;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		number++;
		String text = "Save Clicked " + number + " times";
		model.setText(text); 
		
		
		view.setLabelText( model.getText() );
		
	}

}
