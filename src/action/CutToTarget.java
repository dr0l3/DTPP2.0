package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import marker.Marker2;

import java.util.List;

/**
 * Created by runed on 16-10-2016.
 */
public class CutToTarget extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        int currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        Document document = editor.getDocument();
        if(offset < currentOffset){
            editor.getSelectionModel().setSelection(offset,currentOffset);
        } else {
            editor.getSelectionModel().setSelection(currentOffset,offset);
        }
        editor.getSelectionModel().copySelectionToClipboard();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            if(currentOffset > offset+1){
                document.replaceString(
                        offset,currentOffset, "");
            } else {
                document.replaceString(
                        currentOffset, offset, "");
            }
        });
        editor.getSelectionModel().removeSelection();
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {

    }
}
