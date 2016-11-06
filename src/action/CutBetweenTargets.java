package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CutBetweenTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        if(isSecondOverlay){
            findOffsetsAndPerformAction(EditorUtil::performCut, marker,editor);
            exitAction(editor);
            return;
        }
        setupSecondOverLay(marker, markerPanel, editor);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {
    }
}
