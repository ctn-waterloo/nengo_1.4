package ca.nengo.ui.neurosynthViewer;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import java.awt.Dimension;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

enum Plane {
	AXIAL,
	CORONAL,
	SAGITTAL
};

class PlaneData {	
	final int axis;
	final int min;		// coordinate of the first image
	final int max;		// coordinate of the last image
	final int step;		// must be a factor of (max - min)
	final int width;
	final int height;
	final int valueStart; // first possible coordinate value in the brain data for this axis
	final int valueEnd;   // last possible coordinate value in the brain data for this axis
	final Plane topBottomPlane; // the plane to change by moving the cursor up and down
	final int top;		// coordinate of the top of the image
	final int bottom; 	// coordinate of the bottom of the image
	final Plane leftRightPlane;	// the plane to change by moving the cursor left and right
	final int left;		// coordinate of the left side of the image
	final int right;	// coordinate of the right side of the image
	final Image[] images;
	
	PlaneData(int axis, int min, int max, int step, int width, int height,
			  int valueStart, int valueEnd,
			  Plane topBottomPlane, int bottom, int top,
			  Plane leftRightPlane, int left, int right) {
		this.axis = axis;
		this.min = min;
		this.max = max;
		this.step = step;
		this.width = width;
		this.height = height;
		this.valueStart = valueStart;
		this.valueEnd = valueEnd;
		this.topBottomPlane = topBottomPlane;
		this.top = top;
		this.bottom = bottom;
		this.leftRightPlane = leftRightPlane;
		this.left = left;
		this.right = right;
		int numImages = (max - min) / step + 1;
		images = new Image[numImages];
	}
}

class Model {
	private EnumMap<Plane, PlaneData> planeData = new EnumMap<Plane, PlaneData>(Plane.class);

	private int[] position;
	private View view;
	
	private DataLoader dataLoader = new DataLoader();
	private Data data;
	private String term = "";
	private DataType dataType = DataType.REVERSE_INFERENCE;

	Model() {
		planeData.put(Plane.AXIAL, new PlaneData(2, -72, 84, 4, 227, 272,
												 -74, 106,
												 Plane.CORONAL, -128, 90,
												 Plane.SAGITTAL, -90, 90));
		planeData.put(Plane.CORONAL, new PlaneData(1, -108, 76, 4, 227, 227,
												 -128, 88,
												 Plane.AXIAL, -72, 108,
												 Plane.SAGITTAL, -90, 90));
		planeData.put(Plane.SAGITTAL, new PlaneData(0, -72, 72, 4, 284, 237,
													90, -90,
													Plane.AXIAL, -72, 108,
													Plane.CORONAL, 90, -128));
		
		position = new int[planeData.size()];
		Arrays.fill(position, 0);
	}
	
	void loadImages(String imageDir) {
		Iterator<Map.Entry<Plane, PlaneData>> ite = planeData.entrySet().iterator();
		while (ite.hasNext()) {
			Map.Entry<Plane, PlaneData> entry = ite.next();
			Plane plane = entry.getKey();
			PlaneData data = entry.getValue();

			String planeName = plane.toString().toLowerCase();
			String planeBase = imageDir + "/" + planeName;
			
			int pos = data.min;
			for (int i = 0; i < data.images.length; ++i) {
				String imagePath = planeBase + "_" + pos + ".jpg";
				data.images[i] = getImage(imagePath);
				pos += data.step;
			}
		}
	}
	
	private Image getImage(String path) {
		
		//InputStream imageStream = getClass().getResourceAsStream(path);
		
		FileInputStream imageStream = null;
		try {
			imageStream = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			System.err.println(path + " not found");
			return null;
		}
		
		try {
			 return ImageIO.read(imageStream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	void setView(View newView) {
		view = newView;
	}

	PlaneData getPlaneData(Plane plane) {
		return planeData.get(plane);
	}
	
	int[] getPosition(int[] dest) {
		if (dest == null || dest.length != position.length) {
			dest = new int[position.length];
		}
		System.arraycopy(position, 0, dest, 0, position.length);
		return dest;
	}
	
	void setPosition(int[] newPosition) {
		if (newPosition.length != position.length) {
			return;
		}
		
		for (PlaneData data: planeData.values()) {
			int value = newPosition[data.axis];
			if (value < data.min) {
				newPosition[data.axis] = data.min;
			} else if (value > data.max) {
				newPosition[data.axis] = data.max;
			}
		}
		
		System.arraycopy(newPosition, 0, position, 0, position.length);
		view.notifyPositionChanged();
	}
	
	void showTerm(String newTerm) {
		term = newTerm;
		loadData();
	}
	
	void setDataType(DataType type) {
		dataType = type;
		loadData();
	}
	
	private void loadData() {
		if (term.length() == 0) {
			return;
		}
		
		Data newData = dataLoader.load(term, dataType);
		
		if (newData == null) {
			view.notifyFailedToLoadData(term);
			return;
		}
		
		data = newData;
		view.notifyDataChanged();
	}
	
	DataType getDataType() {
		return dataType;
	}
	
	boolean hasData() {
		return data != null;
	}

	int getValuePlaneIndex(Plane plane) {
		PlaneData planeData = getPlaneData(plane);
		int pos = position[planeData.axis];
		int[] ranges = Data.getRanges();
		int range = ranges[planeData.axis];
		
		int posRange = planeData.valueEnd - planeData.valueStart;
		double relativePos = (double) (pos - planeData.valueStart) / posRange;
		double valueIndex = range * relativePos;
		return (int) Math.floor(valueIndex);		
	}
	
	Dimension getValueDimension(Plane plane) {
		PlaneData planeData = getPlaneData(plane);
		PlaneData topBottomData = getPlaneData(planeData.topBottomPlane);
		PlaneData leftRightData = getPlaneData(planeData.leftRightPlane);
		int[] ranges = Data.getRanges();
		int width = ranges[leftRightData.axis];
		int height = ranges[topBottomData.axis];
		
		return new Dimension(width, height);
	}
	
	float getPositionValue() {
		return getValue(position);
	}
	
	float getValue(int[] point) {
		int[] coords = new int[3];
		for (Plane plane: Plane.values()) {
			PlaneData planeData = getPlaneData(plane);
			coords[planeData.axis] = getValuePlaneIndex(plane);
		}
		return getValueOfCell(coords);
	}
	
	float getValueOfCell(int[] coord) {
		if (coord.length != 3) {
			return 0;
		}
	
		return data.getValue(coord[0], coord[1], coord[2]);
	}
	
	float getMinValue() {
		return data.getMinValue();
	}
	
	float getMaxValue() {
		return data.getMaxValue();
	}
} 
