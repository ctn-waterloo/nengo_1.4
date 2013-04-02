package ca.nengo.ui.neurosynthViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

public class NeurosynthViewer  {
	private final static String NAME = "Neurosynth Viewer";
	private final static String IMAGE_DIR = "images/neurosynth/";

	private Model model;
	private View view;
	private JFrame frame;
	private ImageLoader imageLoader = new ImageLoader();
	
	public NeurosynthViewer() {
		frame = new JFrame(NAME);
		
		model = new Model();
		view = new View(model);
		model.setView(view);
		
		frame.add(view);
		frame.pack();
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	
		frame.setResizable(false);

		view.addCancelListener(new CancelListener());
		view.addSelectListener(new SelectListener());
		frame.addWindowListener(new CloseListener());
		
		model.loadImages(IMAGE_DIR);
		
		//imageLoader.execute();
	}
	
	private class ImageLoader extends SwingWorker<Object, Object> {
		@Override
		protected Object doInBackground() throws Exception {
			model.loadImages(IMAGE_DIR);
			return null;
		}
	}
	
	public int[] getCoordinates() {
		/*while (!imageLoader.isDone()) {
			Thread.yield();
		}*/
		
		frame.setVisible(true);
		return null;
	}
	
	private class CancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	private class SelectListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] position = model.getPosition(null);
			System.out.println(Arrays.toString(position));
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	private class CloseListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent we) {
			// TODO: trigger the same events as cancel
		}
	}
	
}
