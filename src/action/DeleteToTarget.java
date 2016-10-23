package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import util.EditorUtil;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class DeleteToTarget extends VersionTwoCustomAction {
    @Override
    public void initiateActionAtMarker(Marker2 marker) {
        findOffsetsAndPerformAction(EditorUtil::performDelete,marker);
        exitAction();
    }

    @Override
    public void initiateActionAtMarkers(List<Marker2> markers) {
    }
}
