package action;

import com.intellij.openapi.editor.Editor;
import keylistener.OneOffsetOneOverlayAcceptListener;
import keylistener.TwoOffsetOneOverlayAcceptListener;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CutToTarget extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        markerPanel.removeMarkers(Collections.singletonList(marker));
        util.EditorUtil.performMarkRange(editor.getCaretModel().getOffset(), marker.getStartOffset() + 1, editor);
        //wait for either request to undo (escape) or implicit confirmation (anything else)
        suspendedKeylisteners = editor.getContentComponent().getKeyListeners();
        Arrays.stream(suspendedKeylisteners)
                .forEach(editor.getContentComponent()::removeKeyListener);
        exitOnEscape = false;
        editor.getContentComponent().addKeyListener(
                new TwoOffsetOneOverlayAcceptListener(this, editor, marker, EditorUtil::performCut));
        decommissionPopup(popup);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
    }
}
