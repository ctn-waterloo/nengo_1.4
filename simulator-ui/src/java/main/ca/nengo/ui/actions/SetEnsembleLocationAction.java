package ca.nengo.ui.actions;

import java.util.ArrayList;

import ca.nengo.model.nef.NEFEnsemble;
import ca.nengo.model.nef.impl.NEFEnsembleImpl;
import ca.nengo.ui.NengoGraphics;
import ca.nengo.ui.lib.actions.ActionException;
import ca.nengo.ui.lib.actions.StandardAction;
import ca.nengo.ui.models.nodes.UINEFEnsemble;
import ca.nengo.ui.neurosynthViewer.NeurosynthViewer;

/**
 * Opens a Neurosynth dialog for the user to choose the location of the
 * ensemble in the brain.
 * 
 * @author Tian Yu Zhang
 */
public class SetEnsembleLocationAction extends StandardAction {
	private static final NeurosynthViewer viewer = new NeurosynthViewer(NengoGraphics.getInstance());
	private static final long serialVersionUID = 1L;
    private ArrayList<UINEFEnsemble> uiNEFEnsembles;

    /**
     * @param uiNEFEnsemble the ensemble to set position for
     */
    public SetEnsembleLocationAction(ArrayList<UINEFEnsemble> uiNEFEnsembles) {
        super("Set the location of an NEFEnsemble.","Ensemble Locator");
        this.uiNEFEnsembles = uiNEFEnsembles;
    }

    protected void action() throws ActionException {
    	
    	if (uiNEFEnsembles.size() == 1) {
    		NEFEnsemble nefEnsemble = uiNEFEnsembles.get(0).getModel();
    		int[] coords = nefEnsemble.getPosition();
    		viewer.setPosition(coords);
    	}
    	
    	int[] coords = viewer.askForPosition();
    	
    	if (coords != null) {
    		for (UINEFEnsemble uiNEFEnsemble: uiNEFEnsembles) {
    			NEFEnsemble nefEnsemble = uiNEFEnsemble.getModel();
    			((NEFEnsembleImpl) nefEnsemble).setPosition(coords);
    		}
    	}
    }
}


