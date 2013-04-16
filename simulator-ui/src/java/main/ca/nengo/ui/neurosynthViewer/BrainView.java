package ca.nengo.ui.neurosynthViewer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JSlider;

class BrainView extends JComponent {
	private static final long serialVersionUID = 1L;

	enum SliderPosition {TOP, RIGHT, BOTTOM, LEFT}
	
	Model model;
	PlaneData data;
	BrainImage image;
	BrainSlider slider;
	
	BrainView(Model newModel, Plane plane, SliderPosition sliderPos) {
		model = newModel;
		data = model.getPlaneData(plane);
		image = new BrainImage(model, plane);
		slider = new BrainSlider(model, plane);
		
		switch (sliderPos) {
		case TOP: case BOTTOM:
			slider.setOrientation(JSlider.HORIZONTAL);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			break;
		case LEFT: case RIGHT:
			slider.setOrientation(JSlider.VERTICAL);
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			break;
		default:
			slider.setOrientation(JSlider.HORIZONTAL);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			break;
		}
		
		switch (sliderPos) {
		case TOP: case LEFT:
			add(slider);
			add(image);
			break;
		case BOTTOM: case RIGHT:
			add(image);
			add(slider);
			break;
		default:
			add(image);
			add(slider);
			break;
		}
	}
	
	void notifyPositionChanged() {
		image.notifyPositionChanged();
		slider.notifyPositionChanged();
	}
	
	void notifyDataChanged() {
		image.notifyDataChanged();
	}
}
