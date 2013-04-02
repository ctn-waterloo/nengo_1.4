package ca.nengo.ui.neurosynthViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

class BrainImage extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Color CURSOR_COLOR = Color.GREEN;
	private static final int LABEL_SPACING = 5;
	
	private static Color[] HOT_GRADIENT = {
		new Color(0x8B0000), 
		new Color(0xB22222),
		new Color(0xDC143C),
		new Color(0xFF0000),
		new Color(0xFF4500),
		new Color(0xFF8C00),
		new Color(0xFFD700),
		new Color(0xFFA500),
		new Color(0xFFFF00),
		new Color(0xFFFFE0)
	};
	
	private static Color[] COLD_GRADIENT = {
		new Color(0x191970), 
		new Color(0x00008B),
		new Color(0x0000CD),
		new Color(0x0000FF),
		new Color(0x4169E1),
		new Color(0x1E90FF),
		new Color(0x6495ED),
		new Color(0x00BFFF),
		new Color(0x87CEEB)
	};
	
	private static Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	
	private Model model;
	private Plane plane;
	private PlaneData data;
	private PlaneData topBottomData;
	private PlaneData leftRightData;
	private int[] position = new int[3];
	private Dimension size;
	private Line2D topBottomCursor = new Line2D.Double();
	private Line2D leftRightCursor = new Line2D.Double();
	
	int topBottomValueStart, topBottomValueEnd;
	int leftRightValueStart, leftRightValueEnd;
	
	BrainImage(Model newModel, Plane newPlane) {
		model = newModel;
		plane = newPlane;
		data = model.getPlaneData(plane);
		topBottomData = model.getPlaneData(data.topBottomPlane);
		leftRightData = model.getPlaneData(data.leftRightPlane);
		
		position = model.getPosition(position);
		size = new Dimension(data.width, data.height);
	
		calculateDataLimits();
		addMouseListener(new PositionSetter());
		updateCursorsPosition();
	}
	
	private void calculateDataLimits() {
		PlaneData topBottomData = model.getPlaneData(data.topBottomPlane);
		int topBottomRange = data.top - data.bottom;
		double topBottomRatio = (double) data.height / topBottomRange;
		
		int topPos = data.top - topBottomData.valueEnd;
		int bottomPos = data.top - topBottomData.valueStart;
		
		topBottomValueStart = (int) Math.round(topPos * topBottomRatio);
		topBottomValueEnd = (int) Math.round(bottomPos * topBottomRatio);
		
		PlaneData leftRightData = model.getPlaneData(data.leftRightPlane);
		int leftRightRange = data.right - data.left;
		double leftRightRatio = (double) data.width / leftRightRange;
		
		int leftPos = leftRightData.valueStart - data.left;
		int rightPos = leftRightData.valueEnd - data.left;
		
		leftRightValueStart = (int) Math.round(leftPos * leftRightRatio);
		leftRightValueEnd = (int) Math.round(rightPos * leftRightRatio);
	}
	
	private void updateCursorsPosition() {
		int topBottomRange = data.top - data.bottom;
		int topBottomValue = position[topBottomData.axis];
		double relativeTopBottom = (double) (data.top - topBottomValue) / topBottomRange;
		double topBottomPos = relativeTopBottom * data.height;
		double roundedTopBottomPos = (int) Math.round(topBottomPos);
		topBottomCursor.setLine(0, roundedTopBottomPos, data.width, roundedTopBottomPos);
		
		int leftRightRange = data.right - data.left;
		int leftRightValue = position[leftRightData.axis];
		double relativeLeftRight = (double) (leftRightValue - data.left) / leftRightRange;
		double leftRightPos = relativeLeftRight * data.width;
		double rondedLeftRightPos = (int) Math.round(leftRightPos);
		leftRightCursor.setLine(rondedLeftRightPos, 0, rondedLeftRightPos, data.height);
	}
	
	private class PositionSetter extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent event) {
			int topBottomRange = data.top - data.bottom;
			double relativeTopBottom = (double) (data.height - event.getY()) / data.height;
			double topBottomPos = relativeTopBottom * topBottomRange + data.bottom;
			int roundedTopBottomPos = (int) Math.round(topBottomPos);
			position[topBottomData.axis] = roundedTopBottomPos;
			
			int leftRightRange = data.right - data.left;
			double relativeLeftRight = (double) event.getX() / data.width;
			double leftRightPos = relativeLeftRight * leftRightRange + data.left;
			int roundedLeftRightPos = (int) Math.round(leftRightPos);
			position[leftRightData.axis] = roundedLeftRightPos;
			
			model.setPosition(position);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {	
		int index = getImageIndex();		
		paintBrainImage(g, index);
		if (model.hasData()) {
			paintData(g);	
		}
		
		paintCursors(g);
		paintOrientationLabels(g);
		paintAxisLabel(g);
	}
	
	private void paintBrainImage(Graphics g, int index) {
		Image image = data.images[index];
		if (image != null) {
			g.drawImage(image, 0, 0, BACKGROUND_COLOR, null);
		} else {
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, data.width, data.height);
		}
	}
	
	private int getImageIndex() {
		int pos = position[data.axis];
		double index = (double) (pos - data.min) / data.step;
		return (int) Math.round(index);
	}
	
	private void paintData(Graphics g) {
		int dataIndex = model.getValuePlaneIndex(plane);
		Dimension planeDim = model.getValueDimension(plane);

		int[] coord = new int[3];
		coord[data.axis] = dataIndex;
		
		int topBottomRange = topBottomValueEnd - topBottomValueStart;
		double topBottomStep = (double) topBottomRange / planeDim.height;
		int leftRightRange = leftRightValueEnd - leftRightValueStart;
		double leftRightStep = (double) leftRightRange / planeDim.width;
		
		int cellWidth = (int) Math.ceil(Math.abs(leftRightStep));
		int cellHeight = (int) Math.ceil(Math.abs(topBottomStep));
		
		double leftRightPos = leftRightValueStart;
		for (int i = 0; i < planeDim.width; ++i) {
			coord[leftRightData.axis] = i;

			double topBottomPos = topBottomValueStart;
			for (int j = planeDim.height - 1; j >= 0; --j) {
				coord[topBottomData.axis] = j;
				
				float value = model.getValueOfCell(coord);
				
				if (value != 0) {
					Color color = getColorForValue(value);
					g.setColor(color);
					
					double dx = (leftRightStep > 0) ? leftRightPos : leftRightPos + leftRightStep; 
					double dy = (topBottomStep > 0) ? topBottomPos : topBottomPos + topBottomStep; 
					int x = (int) Math.round(dx);
					int y = (int) Math.round(dy);
					g.fillRect(x, y, cellWidth, cellHeight);
				}
				
				topBottomPos = topBottomPos + topBottomStep;
			}
			
			leftRightPos = leftRightPos + leftRightStep;
		}
	}
	
	private Color getColorForValue(float value) {
		double mid = 0;
		
		if (model.getDataType() == DataType.POSTERIOR_PROBABILITY) {
			mid = 0.5;
		}
		
		if (value < mid) {
			double colorRatio = (value - mid) / (model.getMinValue() - mid);
			int maxColorIndex = COLD_GRADIENT.length - 1;
			int colorIndex = (int) Math.round(colorRatio * maxColorIndex);
			return COLD_GRADIENT[colorIndex];
		} else {
			double colorRatio = (value - mid) / (model.getMaxValue() - mid);
			int maxColorIndex = HOT_GRADIENT.length - 1;
			int colorIndex = (int) Math.round(colorRatio * maxColorIndex);
			return HOT_GRADIENT[colorIndex];
		}
		
	}
	
	private void paintCursors(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(CURSOR_COLOR);
		g2.draw(topBottomCursor);
		g2.draw(leftRightCursor);
	}

	void paintOrientationLabels(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(labelFont);
		
		FontMetrics fontMetrics = g.getFontMetrics();
		int fontHeight = fontMetrics.getAscent();

		switch (plane) {
		case AXIAL:
			int lx = LABEL_SPACING;
			int ly = LABEL_SPACING + fontHeight;
			g.drawString("L", lx, ly);
			
			int rw = fontMetrics.stringWidth("R");
			int rx = data.width - LABEL_SPACING - rw;
			int ry = LABEL_SPACING + fontHeight;
			g.drawString("R", rx, ry);
			break;
			
		case CORONAL:
			int dw = fontMetrics.stringWidth("D");
			int dx = data.width - LABEL_SPACING - dw;
			int dy = LABEL_SPACING + fontHeight;
			g.drawString("D", dx, dy);
			
			int vw = fontMetrics.stringWidth("V");
			int vx = data.width - LABEL_SPACING - vw;
			int vy = data.height - LABEL_SPACING;
			g.drawString("V", vx, vy);
			break;
			
		case SAGITTAL:
			int ax = LABEL_SPACING;
			int ay = data.height / 2 + fontHeight / 2;
			g.drawString("A", ax, ay);
			
			int pw = fontMetrics.stringWidth("P");
			int px = data.width - LABEL_SPACING - pw;
			int py = data.height / 2 + fontHeight / 2;
			g.drawString("P", px, py);
			break;
		}
	}
	
	void paintAxisLabel(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(labelFont);
		
		int	x = LABEL_SPACING;
		int y = data.height - LABEL_SPACING;
		int value = position[data.axis];
		String label = "";
		switch (plane) {
		case AXIAL: 	label = "z = " + value; break;
		case CORONAL:	label = "y = " + value; break;
		case SAGITTAL:  label = "x = " + value; break;
		}
		
		g.drawString(label, x, y);
	}
	
	void notifyPositionChanged() {
		model.getPosition(position);
		updateCursorsPosition();		
		repaint();
	}
	
	void notifyDataChanged() {
		repaint();
	}
	
	@Override
	public Dimension getMinimumSize() {
		return size;
	}	
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return size;
	}
}
