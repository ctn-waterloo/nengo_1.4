package ca.shu.ui.lib.actions;

import java.awt.geom.Point2D;

import ca.shu.ui.lib.util.WorldLayout;
import ca.shu.ui.lib.world.IWorld;
import ca.shu.ui.lib.world.IWorldObject;

public abstract class LayoutAction extends ReversableAction {
	private static final long serialVersionUID = 1L;

	private WorldLayout savedLayout;

	private IWorld world;

	public LayoutAction(IWorld world, String description, String actionName) {
		super(description, actionName);
		this.world = world;
	}

	@Override
	protected void action() throws ActionException {
		savedLayout = new WorldLayout("", world, false);
		applyLayout();
	}

	protected abstract void applyLayout();

	protected void restoreNodePositions() {

		for (IWorldObject node : world.getGround().getChildren()) {
			Point2D savedPosition = savedLayout.getPosition(node);
			if (savedPosition != null) {
				node.setOffset(savedPosition);
			}
		}
	}

	@Override
	protected void undo() throws ActionException {
		restoreNodePositions();
	}

}