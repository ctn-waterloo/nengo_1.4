package ca.nengo.ui.neurosynthViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class NeurosynthViewer  {
	private final static String NAME = "Neurosynth Viewer";
	private final static String IMAGE_DIR = "images/neurosynth/";

	private JFrame parent;
	private Model model;
	private View view;
	private JDialog dialog;
	
	private boolean positionSelected = false;
	
	public NeurosynthViewer(JFrame parentFrame) {
		parent = parentFrame;
		dialog = new JDialog(parent, NAME, true);
		
		model = new Model();
		view = new View(model);
		model.setView(view);
		
		dialog.add(view);
		dialog.pack();
		
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);	
		dialog.setResizable(false);

		view.addCancelListener(new CancelListener());
		view.addSelectListener(new SelectListener());
		
		model.loadImages(IMAGE_DIR);
	}
	
	public void setPosition(int[] coordinates) {
		model.setPosition(coordinates);
	}	
	
	public int[] askForPosition() {		
		positionSelected = false;
		dialog.setVisible(true);
		
		if (!positionSelected) {
			return null;
		}

		return model.getPosition(null);
	}
	
	private class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dialog.setVisible(false);
		}
	}
	
	private class SelectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			positionSelected = true;
			dialog.setVisible(false);
		}
	}
}
