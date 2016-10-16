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
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        if(currentOffset < offset){
            EditorUtil.performDelete(currentOffset,offset,editor);
        } else{
            EditorUtil.performDelete(offset,currentOffset, editor);
        }
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
    }
}
