package action;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by runed on 16-10-2016.
 */
public class PasteToTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        findSingleOffsetAndPerformAction(EditorUtil::performPasteFromClipboard, marker,editor);
        exitAction(editor);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
        List<Integer> offsets = markers.stream().map(Marker2::getStartOffset).collect(Collectors.toList());
        EditorUtil.performPasteFromClipboard(offsets, editor);
        exitAction(editor);
    }
}
