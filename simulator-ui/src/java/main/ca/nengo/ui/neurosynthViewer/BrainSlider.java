package ca.nengo.ui.neurosynthViewer;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class BrainSlider extends JSlider {
	private static final long serialVersionUID = 1L;
	
	private Model model;
	private PlaneData data;
	
	private int[] position = new int[3];
	private int numChangesToIgnore = 0;
	
	BrainSlider(Model newModel, Plane plane) {
		model = newModel;
		data = model.getPlaneData(plane);
		setMinimum(data.min);
		setMaximum(data.max);
		model.getPosition(position);
		setValue(position[data.axis]);
		addChangeListener(new ValueChanger());
	}
	
	private class ValueChanger implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent event) {
			if (numChangesToIgnore > 0) {
				--numChangesToIgnore;
				return;
			}
			
			model.getPosition(position);
			position[data.axis] = getValue();
			model.setPosition(position);
		}
	}
	
	void notifyPositionChanged() {
		model.getPosition(position);
		int value = position[data.axis];
		if (value != getValue()) {
			++numChangesToIgnore;
			setValue(value);
		}
	}
}
