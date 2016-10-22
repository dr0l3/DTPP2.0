package action;

import marker.Marker2;

import java.util.List;

/**
 * Created by runed on 15-10-2016.
 */
public class JumpToTarget extends VersionTwoCustomAction {

    @Override
    public void initiateActionAtMarker(Marker2 marker) {
        findOffsetsAndPerformAction(util.EditorUtil::performMove,marker);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers) {
        return;
    }
}
