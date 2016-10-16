package action;

import marker.Marker2;

import java.util.List;

/**
 * Created by runed on 15-10-2016.
 */
public class JumpToTarget extends VersionTwoCustomAction {

    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        editor.getCaretModel().moveToOffset(offset);
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
        return;
    }
}
