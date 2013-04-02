package ca.nengo.ui.actions;

import java.util.ArrayList;

import ca.nengo.ui.lib.actions.ActionException;
import ca.nengo.ui.lib.actions.StandardAction;
import ca.nengo.ui.models.nodes.UINEFEnsemble;
import ca.nengo.ui.neurosynthViewer.NeurosynthViewer;

/**
 * TODO
 * 
 * @author TODO
 */
public class SetEnsembleLocationAction extends StandardAction {
	private final NeurosynthViewer viewer = new NeurosynthViewer();
	private static final long serialVersionUID = 1L;
    private ArrayList<UINEFEnsemble> uiNEFEnsembles;

    /**
     * @param uiNetwork TODO
     */
    public SetEnsembleLocationAction(ArrayList<UINEFEnsemble> uiNEFEnsembles) {
        super("Set the location of an NEFEnsemble.","Ensemble Locator");
        this.uiNEFEnsembles = uiNEFEnsembles;
    }

    protected void action() throws ActionException {
    	viewer.getCoordinates();
    }
}


