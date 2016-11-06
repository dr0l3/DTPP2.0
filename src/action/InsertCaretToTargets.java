package action;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class InsertCaretToTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker,editor);
        exitAction(editor);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        markers.forEach(marker -> editor.getCaretModel().addCaret(editor.offsetToVisualPosition(marker.getStartOffset())));
        editor.getCaretModel().removeCaret(currentCaret);
        exitAction(editor);
    }
}
