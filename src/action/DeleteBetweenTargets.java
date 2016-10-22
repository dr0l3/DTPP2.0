package action;

import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class DeleteBetweenTargets extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker) {
        if(isSecondOverlay){
            findOffsetsAndPerformAction(EditorUtil::performDelete, marker);
            return;
        }
        setupSecondOverLay(marker);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers) {
        return;
    }
}
