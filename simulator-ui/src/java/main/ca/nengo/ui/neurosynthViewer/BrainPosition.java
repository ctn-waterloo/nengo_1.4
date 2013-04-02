package ca.nengo.ui.neurosynthViewer;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class BrainPosition extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static int SPACING = 5;
	
	private Model model;
	private JLabel[] labels = new JLabel[3];
	private JSpinner[] spinners = new JSpinner[3];
	private int [] position = new int[3];
	
	private int numChangesToIgnore = 0;
	
	BrainPosition(Model newModel) {
		model = newModel;
		
		labels[0] = new JLabel("x");
		labels[1] = new JLabel("y");
		labels[2] = new JLabel("z");
		
		PositionChanger positionChanger = new PositionChanger();
		for (Plane plane: Plane.values()) {
			PlaneData data = model.getPlaneData(plane);
			SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, data.min, data.max, 1);
			spinners[data.axis] = new JSpinner(spinnerModel);
			spinners[data.axis].addChangeListener(positionChanger);
		}
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(labels[0]);
		add(Box.createHorizontalStrut(SPACING));
		add(spinners[0]);
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalStrut(2 * SPACING));
		add(labels[1]);
		add(Box.createHorizontalStrut(SPACING));
		add(spinners[1]);
		add(Box.createHorizontalGlue());
		add(Box.createHorizontalStrut(2 * SPACING));
		add(labels[2]);
		add(Box.createHorizontalStrut(SPACING));
		add(spinners[2]);
	}
	
	private class PositionChanger implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent event) {
			if (numChangesToIgnore > 0) {
				--numChangesToIgnore;
				return;
			}
			
			for (int i = 0; i < position.length; ++i) {
				Integer value = (Integer) spinners[i].getValue();
				position[i] = value.intValue();
			}
			model.setPosition(position);
		}
	}
	
	@Override
	public void setFont(Font font) {
		for (JLabel label: labels) {
			label.setFont(font);
		}
		
		for (JSpinner spinner: spinners) {
			spinner.setFont(font);
		}
	}
	
	void notifyPositionChanged() {
		model.getPosition(position);
		for (int i = 0; i < position.length; ++i) {
			Integer value = (Integer) spinners[i].getValue();
			int intVal = value.intValue();
			if (intVal != position[i]) {
				++numChangesToIgnore;
				spinners[i].setValue(position[i]);
			}
		}
	}
}
