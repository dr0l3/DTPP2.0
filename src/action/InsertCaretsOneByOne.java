package action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Caret;
import marker.Marker2;
import util.EditorUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by runed on 23-10-2016.
 */
public class InsertCaretsOneByOne extends VersionTwoCustomAction {
    private Caret initialCaret;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        super.actionPerformed(anActionEvent);
        initialCaret = editor.getCaretModel().getCurrentCaret();
    }

    @Override
    public void initiateActionAtMarker(Marker2 marker) {
        findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker);
        if(initialCaret != null){
            editor.getCaretModel().removeCaret(initialCaret);
            initialCaret = null;
        }
        markerPanel.removeMarkers(Collections.singletonList(marker));
        markerPanel.repaint();
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers) {
        for (Marker2 marker :markers) {
            findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker);
        }
        markerPanel.removeMarkers(markers);
        markerPanel.repaint();
    }
}
