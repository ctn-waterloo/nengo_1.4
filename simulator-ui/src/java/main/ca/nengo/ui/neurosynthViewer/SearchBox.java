package ca.nengo.ui.neurosynthViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

class SearchBox extends JComboBox<String> {
	private static final long serialVersionUID = 1L;
	
	Model model;
	JTextField entry;
	boolean doNotNotify = false;
		
	List<ActionListener> listeners = new LinkedList<ActionListener>();
	
	SearchBox(Model newModel) {
		model = newModel;
		entry = (JTextField) getEditor().getEditorComponent();
		
		setEditable(true);
		addPopupMenuListener(new Suggester());
		entry.addKeyListener(new InputListener());
		entry.addActionListener(new EnterListener());
	}
	
	String getText() {
		return entry.getText();
	}
	
	private class Suggester implements PopupMenuListener {
		@Override
		public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
			String filterText = getText();
			filterSuggestions(filterText);
			setSelectedIndex(-1);
			entry.setText(filterText);
		}
		
		@Override
		public void popupMenuCanceled(PopupMenuEvent event) {
			doNotNotify = true;
		}

		@Override
		public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
			if (!doNotNotify) {
				notifyActionListeners();
			}
			doNotNotify = false;
		}
	}
	
	private class InputListener extends KeyAdapter {
		@Override
		public void keyTyped(KeyEvent e) {
			if (isPopupVisible()) {
				doNotNotify = true;
				hidePopup();
				String text = getText();
				if (text.length() > 0) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							showPopup();
						}
					});
				}
			}
		}
	}
	
	private void filterSuggestions(String filterText) {
		removeAllItems();
		List<String> suggestions = model.getSuggestions(filterText);
		for (String sug: suggestions) {
			addItem(sug);
		}
	}
	
	private class EnterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (! isPopupVisible()) {
				notifyActionListeners();
			}
		}
	}
	
	private void notifyActionListeners() {
		ActionEvent event = new ActionEvent(this, 
					ActionEvent.ACTION_PERFORMED, "searchBoxSelecetd");
		for (ActionListener listener: listeners) {
			listener.actionPerformed(event);
		}
	}
	
	
	@Override
	public void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}
}
