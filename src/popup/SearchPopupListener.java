package popup;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import marker.MarkerPanel2;

/**
 * Created by runed on 16-10-2016.
 */
public class SearchPopupListener implements JBPopupListener {
    private VersionTwoCustomAction callingAction;
    private Editor editor;
    private MarkerPanel2 markerPanel;

    public SearchPopupListener(VersionTwoCustomAction callingAction, Editor editor) {
        this.callingAction = callingAction;
        this.editor = editor;
    }

    @Override
    public void beforeShown(LightweightWindowEvent lightweightWindowEvent) {

    }

    @Override
    public void onClosed(LightweightWindowEvent lightweightWindowEvent) {
        callingAction.exitAction(editor);
    }
}
