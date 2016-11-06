package action;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import keylistener.ManyOffsetOneOverlayAcceptListener;
import keylistener.OneOffsetOneOverlayAcceptListener;
import keylistener.TwoOffsetOneOverlayAcceptListener;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by runed on 16-10-2016.
 */
public class InsertCaretToTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        markerPanel.removeMarkers(Collections.singletonList(marker));
        util.EditorUtil.performMarkRange(marker.getStartOffset(), marker.getStartOffset() + 1, editor);
        //wait for either request to undo (escape) or implicit confirmation (anything else)
        suspendedKeylisteners = editor.getContentComponent().getKeyListeners();
        Arrays.stream(suspendedKeylisteners)
                .forEach(editor.getContentComponent()::removeKeyListener);
        exitOnEscape = false;
        editor.getContentComponent().addKeyListener(new OneOffsetOneOverlayAcceptListener(this, editor, marker, EditorUtil::performInsertCaret));
        decommissionPopup(popup);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
        List<Integer> offsets = markers.stream().map(marker -> marker.getStartOffset()).collect(Collectors.toList());
        Marker2 copy = markers.get(0);
        markers.forEach(marker -> util.EditorUtil.performMarkRange(marker.getStartOffset(), marker.getStartOffset() + 1, editor));
        markerPanel.removeMarkers(markers);
        //wait for either request to undo (escape) or implicit confirmation (anything else)
        suspendedKeylisteners = editor.getContentComponent().getKeyListeners();
        Arrays.stream(suspendedKeylisteners)
                .forEach(editor.getContentComponent()::removeKeyListener);
        exitOnEscape = false;
        editor.getContentComponent().addKeyListener(
                new ManyOffsetOneOverlayAcceptListener(this, editor,copy,  offsets, EditorUtil::performInsertCarets));
        decommissionPopup(popup);
    }
}
