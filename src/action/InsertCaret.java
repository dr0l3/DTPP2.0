package action;

import com.intellij.openapi.editor.Caret;
import marker.Marker2;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class InsertCaret extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        editor.getCaretModel().addCaret(editor.offsetToVisualPosition(offset));
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
        Caret currentCaret = editor.getCaretModel().getCurrentCaret();
        markers.forEach(marker -> editor.getCaretModel().addCaret(editor.offsetToVisualPosition(marker.getStartOffset())));
        editor.getCaretModel().removeCaret(currentCaret);
        exitAction();
    }
}
