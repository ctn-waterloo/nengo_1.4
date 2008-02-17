package ca.neo.ui.util;

import ca.neo.model.Node;
import ca.neo.ui.NeoGraphics;

public class ScriptWorldWrapper {
	private NeoGraphics neoGraphics;
	
	public ScriptWorldWrapper(NeoGraphics neoGraphics) {
		super();
		this.neoGraphics = neoGraphics;
	}

	public void add(Node node) {
		neoGraphics.addNodeModel(node);
	}

	public void addPos(Node node, double posX, double posY) {
		neoGraphics.addNodeModel(node, posX, posY);
	}

	public void remove(Node node) {
		neoGraphics.removeNodeModel(node);
	}
}