package action;

import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class MarkToTarget extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        if(offset < currentOffset){
            EditorUtil.performMark(offset,currentOffset,editor);
        } else {
            EditorUtil.performMark(currentOffset,offset,editor);
        }
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {

    }
}
