package action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
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
        Editor editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        initialCaret = editor.getCaretModel().getCurrentCaret();
    }

    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker,editor);
        if(initialCaret != null){
            editor.getCaretModel().removeCaret(initialCaret);
            initialCaret = null;
        }
        markerPanel.removeMarkers(Collections.singletonList(marker));
        markerPanel.repaint();
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
        for (Marker2 marker :markers) {
            findSingleOffsetAndPerformAction(EditorUtil::performInsertCaret, marker,editor);
        }
        markerPanel.removeMarkers(markers);
        markerPanel.repaint();
    }
}
