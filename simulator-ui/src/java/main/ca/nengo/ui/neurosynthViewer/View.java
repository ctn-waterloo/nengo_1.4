package ca.nengo.ui.neurosynthViewer;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

class View extends JComponent {
	private static final long serialVersionUID = 1L;
	private final static int SPACING = 5;
	
	Model model;
	
	SearchBox searchBox;
	BrainView axialView;
	BrainView coronalView;
	BrainView sagittalView;
	JTextField valueText;
	BrainPosition positionView;
	JComboBox<DataType> typeCombo;
	JButton cancelButton;
	JButton selectButton;
	
	View(Model newModel) {
		model = newModel;
		
		Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		
		JPanel searchPanel = new JPanel();
		JLabel searchLabel = new JLabel("Term:");
		searchLabel.setFont(font);
		
		searchBox = new SearchBox(model);
		searchBox.addActionListener(new Searcher());
		searchBox.setFont(font);
		
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new Searcher());
		searchButton.setFont(font);
		
		JPanel displayPanel = new JPanel();
		JPanel leftDisplayPanel = new JPanel();
		JPanel rightDisplayPanel = new JPanel();
		
		axialView = new BrainView(model, Plane.AXIAL, BrainView.SliderPosition.RIGHT);
		coronalView = new BrainView(model, Plane.CORONAL, BrainView.SliderPosition.RIGHT);
		sagittalView = new BrainView(model, Plane.SAGITTAL, BrainView.SliderPosition.BOTTOM);

		JPanel valuePanel = new JPanel();
		JLabel valueLabel = new JLabel("Value:");
		valueLabel.setFont(font);
		
		valueText = new JTextField(5);
		valueText.setText("N/A");
		valueText.setEditable(false);
		valueText.setFont(font);

		JPanel typePanel = new JPanel();
		JLabel typeLabel = new JLabel("Value:");
		typeLabel.setFont(font);
		
		DataType[] dataTypes = DataType.values();		
		typeCombo = new JComboBox<DataType>(dataTypes);
		typeCombo.addActionListener(new TypeSetter());
		typeCombo.setSelectedItem(DataType.REVERSE_INFERENCE);
		typeCombo.setEditable(false);
		typeCombo.setFont(font);
		
		positionView = new BrainPosition(model);
		positionView.setFont(font);
		
		JPanel buttonsPanel = new JPanel();
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(font);
		selectButton = new JButton("Select");
		selectButton.setFont(font);
		
		setLayout(new BorderLayout());
		add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchPanel.setBorder(BorderFactory.createEmptyBorder(SPACING, SPACING, SPACING, SPACING));
		searchPanel.add(searchLabel);
		searchPanel.add(Box.createHorizontalStrut(SPACING));
		searchPanel.add(searchBox);
		searchPanel.add(Box.createHorizontalStrut(SPACING));
		searchPanel.add(searchButton);

		add(displayPanel, BorderLayout.CENTER);
		displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.X_AXIS));
		displayPanel.setBorder(BorderFactory.createEmptyBorder(SPACING, SPACING, SPACING, SPACING));
		displayPanel.add(leftDisplayPanel);
		displayPanel.add(Box.createHorizontalStrut(SPACING));
		displayPanel.add(rightDisplayPanel);
		
		leftDisplayPanel.setLayout(new BoxLayout(leftDisplayPanel, BoxLayout.Y_AXIS));
		leftDisplayPanel.add(axialView);
		leftDisplayPanel.add(Box.createVerticalStrut(SPACING));
		leftDisplayPanel.add(coronalView);
		
		rightDisplayPanel.setLayout(new BoxLayout(rightDisplayPanel, BoxLayout.Y_AXIS));
		rightDisplayPanel.add(sagittalView);
		rightDisplayPanel.add(Box.createVerticalGlue());
		
		rightDisplayPanel.add(typePanel);
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.X_AXIS));
		typePanel.add(typeLabel);
		typePanel.add(Box.createHorizontalStrut(SPACING));
		typePanel.add(typeCombo);
		typePanel.setMaximumSize(typePanel.getPreferredSize());
		rightDisplayPanel.add(Box.createVerticalGlue());

		rightDisplayPanel.add(valuePanel);
		valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.X_AXIS));
		valuePanel.add(valueLabel);
		valuePanel.add(Box.createHorizontalStrut(SPACING));
		valuePanel.add(valueText);
		valuePanel.setMaximumSize(valuePanel.getPreferredSize());
		rightDisplayPanel.add(Box.createVerticalGlue());
		
		rightDisplayPanel.add(positionView);
		positionView.setMaximumSize(positionView.getPreferredSize());
		
		add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(SPACING, SPACING, SPACING, SPACING));
		buttonsPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(Box.createHorizontalStrut(SPACING));
		buttonsPanel.add(selectButton);
		
		rightDisplayPanel.add(Box.createVerticalGlue());
	}
	
	private class Searcher implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			String term = searchBox.getText();
			if (term.length() > 0) {
				model.showTerm(term);
			}
		}
	}
	
	private class TypeSetter implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			DataType type = (DataType) typeCombo.getSelectedItem();
			model.setDataType(type);
		}
	}
	
	void notifyPositionChanged() {
		axialView.notifyPositionChanged();
		coronalView.notifyPositionChanged();
		sagittalView.notifyPositionChanged();
		positionView.notifyPositionChanged();
		setValueText();
	}
	
	void notifyDataChanged() {
		axialView.notifyDataChanged();
		coronalView.notifyDataChanged();
		sagittalView.notifyDataChanged();
		setValueText();
	}
	
	private void setValueText() {
		String valueString = "N/A";
		
		if (model.hasData()) {
			float value = model.getPositionValue();
			if (model.getDataType() == DataType.POSTERIOR_PROBABILITY) {
				valueString = ((int) Math.round(value * 100)) + "%";
			} else {
				valueString = Float.toString(value);
			}
		}
		
		valueText.setText(valueString);
	}
	
	void notifyFailedToLoadData(String term) {
		model.clearData();
		final String message = "Failed to load data for \"" + term + "\" "
					   + "from  the Neurosynth website.";
		final String title = "Failed to Load Data";
		final int messageType = JOptionPane.ERROR_MESSAGE;
		final JComponent parent = this;

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(parent, message, title, messageType);
			}
		});
	}
	
	void addCancelListener(ActionListener cancelListener) {
		cancelButton.addActionListener(cancelListener);
	}
	
	void removeCancelListener(ActionListener cancelListener) {
		cancelButton.removeActionListener(cancelListener);
	}
	
	void addSelectListener(ActionListener selectListener) {
		selectButton.addActionListener(selectListener);
	}
	
	void removeSelectListener(ActionListener selectListener) {
		selectButton.removeActionListener(selectListener);
	}
}
