package action;

import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CopyToTarget extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        EditorUtil.performCopy(currentOffset,offset,editor);
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {

    }
}
