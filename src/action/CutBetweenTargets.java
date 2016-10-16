package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CutBetweenTargets extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        if(isSecondOverlay){
            int offset = marker.getStartOffset();
            int currentOffset = offsetFromFirstOverlay;
            if(offset < currentOffset){
                EditorUtil.performCut(offset,currentOffset,editor);
            } else {
                EditorUtil.performCut(currentOffset,offset,editor);
            }
            exitAction();
            return;
        }
        setupSecondOverLay(marker);
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
    }
}
