package API;

import action.JumpToTarget;
import action.VersionTwoCustomAction;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;

/**
 * Created by runed on 16-10-2016.
 */
public class SearchPopupListener implements JBPopupListener {
    private VersionTwoCustomAction callingAction;

    public SearchPopupListener(VersionTwoCustomAction callingAction) {
        this.callingAction = callingAction;
    }

    @Override
    public void beforeShown(LightweightWindowEvent lightweightWindowEvent) {

    }

    @Override
    public void onClosed(LightweightWindowEvent lightweightWindowEvent) {
        callingAction.exitAction();
    }
}
