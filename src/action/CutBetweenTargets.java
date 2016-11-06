package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import keylistener.DoubleOverlayAcceptActionListener;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CutBetweenTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        if(isSecondOverlay){
            markerPanel.removeMarkers(Collections.singletonList(marker));
            editor.getMarkupModel().removeAllHighlighters();
            if(offsetFromFirstOverlay < marker.getStartOffset()) {
                util.EditorUtil.performMarkRange(offsetFromFirstOverlay, marker.getStartOffset(), editor);
            } else {
                util.EditorUtil.performMarkRange(marker.getStartOffset(), offsetFromFirstOverlay, editor);
            }
            //wait for either request to undo (escape) or implicit confirmation (anything else)
            suspendedKeylisteners = editor.getContentComponent().getKeyListeners();
            Arrays.stream(suspendedKeylisteners).forEach(editor.getContentComponent()::removeKeyListener);
            exitOnEscape = false;
            editor.getContentComponent().addKeyListener(new DoubleOverlayAcceptActionListener(this,editor,marker,markerPanel, EditorUtil::performCut));
            decommissionPopup(popup);
            //findOffsetsAndPerformAction(EditorUtil::performCopy, marker,editor);
            //exitAction(editor);
            return;
        }
        util.EditorUtil.performMarkRange(marker.getStartOffset(), marker.getStartOffset() + 1, editor);
        setupSecondOverLay(marker, markerPanel, editor);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
    }
}
