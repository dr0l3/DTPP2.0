package action;

import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CopyToTarget extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker, Editor editor, MarkerPanel2 markerPanel) {
        findOffsetsAndPerformAction(EditorUtil::performCopy,marker,editor);
        exitAction(editor);
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers, Editor editor, MarkerPanel2 markerPanel) {

    }
}
