package action;

import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class MarkBetweenTargets extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        if(isSecondOverlay){
            int offset = marker.getStartOffset();
            int currentOffset = offsetFromFirstOverlay;
            if(currentOffset < offset){
                EditorUtil.performMark(currentOffset,offset,editor);
            } else{
                EditorUtil.performMark(offset,currentOffset, editor);
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
