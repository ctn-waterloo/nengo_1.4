package ca.shu.ui.lib.activities;

import ca.shu.ui.lib.util.Util;
import ca.shu.ui.lib.world.WorldObject;
import edu.umd.cs.piccolo.activities.PActivity;
import edu.umd.cs.piccolo.activities.PActivity.PActivityDelegate;

/**
 * Pulsates the target World Object until finished.
 * 
 * @author Shu Wu
 */
public class Pulsator {
	public static final long PULSATION_RATE_PER_SEC = 1;

	private static final long PULSATION_STATE_TRANSITION = (1000 / (PULSATION_RATE_PER_SEC * 2));

	private WorldObject target;
	private float originalTransparency;
	private boolean isPulsating = true;
	PActivity fadeActivity;

	private enum PulsationState {
		FADING_IN, FADING_OUT
	}

	PulsationState pulsationState = PulsationState.FADING_OUT;

	public Pulsator(WorldObject wo) {
		this.target = wo;
		originalTransparency = wo.getTransparency();
		pulsate();
	}

	public void finish() {
		isPulsating = false;
		fadeActivity.terminate(PActivity.TERMINATE_AND_FINISH);
		target.setTransparency(originalTransparency);
	}

	private void pulsate() {
		if (isPulsating) {
			Util.Assert(fadeActivity == null || !fadeActivity.isStepping(),
					"activities are overlapping");

			if (pulsationState == PulsationState.FADING_IN) {
				pulsationState = PulsationState.FADING_OUT;
				fadeActivity = new Fader(target, PULSATION_STATE_TRANSITION, 1f);
			} else if (pulsationState == PulsationState.FADING_OUT) {
				pulsationState = PulsationState.FADING_IN;
				fadeActivity = new Fader(target, PULSATION_STATE_TRANSITION, 0f);
			} else {
				throw new UnsupportedOperationException();
			}
			fadeActivity.setDelegate(myFaderDelegate);

			target.addActivity(fadeActivity);
		}
	}

	PActivityDelegate myFaderDelegate = new PActivityDelegate() {
		public void activityFinished(PActivity activity) {
			pulsate();
		}

		public void activityStarted(PActivity activity) {
			// do nothing
		}

		public void activityStepped(PActivity activity) {
			// do nothing
		}
	};

}