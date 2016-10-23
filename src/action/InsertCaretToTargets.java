package action;

import com.intellij.openapi.editor.Caret;
import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class InsertCaretToTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker) {
        findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker);
        exitAction();
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers) {
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        markers.forEach(marker -> editor.getCaretModel().addCaret(editor.offsetToVisualPosition(marker.getStartOffset())));
        editor.getCaretModel().removeCaret(currentCaret);
        exitAction();
    }
}
