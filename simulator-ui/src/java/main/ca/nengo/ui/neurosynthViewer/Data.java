package ca.nengo.ui.neurosynthViewer;

import java.util.HashMap;

class Data {
	private static final int[] RANGES = {45, 54, 45};
	float min, max;
	
	private HashMap<Integer, HashMap<Integer, HashMap<Integer, Float>>> values
	  = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Float>>>();

	Data(float min, float max) {
		this.min = min;
		this.max = max;
	}
	
	static int[] getRanges() {
		return RANGES;
	}
	
	void setValue(int x, int y, int z, float value) {
		HashMap<Integer, HashMap<Integer, Float>> yPlane;
		if (values.containsKey(x)) {
			yPlane = values.get(x);
		} else {
			yPlane = new HashMap<Integer, HashMap<Integer, Float>>();
			values.put(x, yPlane);
		}
		
		HashMap<Integer, Float> zLine;
		if (yPlane.containsKey(y)) {
			zLine = yPlane.get(y);
		} else {
			zLine = new HashMap<Integer, Float>();
			yPlane.put(y, zLine);
		}
		
		zLine.put(z, value);
	}
	
	float getValue(int x, int y, int z) {
		if (x < 0 || x > RANGES[0] ||
			y < 0 || y > RANGES[1] ||
			z < 0 || z > RANGES[2]) {
			System.err.println("HERE");
		}
		
		HashMap<Integer, HashMap<Integer, Float>> yPlane;
		if (values.containsKey(x)) {
			yPlane = values.get(x);
		} else {
			return 0;
		}

		HashMap<Integer, Float> zLine;
		if (yPlane.containsKey(y)) {
			zLine = yPlane.get(y);
		} else {
			return 0;
		}
		
		if (zLine.containsKey(z)) {
			return zLine.get(z);
		} else {
			return 0;
		}
	}
	
	float getMinValue() {
		return min;
	}
	
	float getMaxValue() {
		return max;
	}
	
	void clear() {
		values.clear();
	}
}
