package action;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ide.CopyPasteManager;
import marker.Marker2;
import util.EditorUtil;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by runed on 16-10-2016.
 */
public class PasteToTarget extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        EditorUtil.performPaste(Arrays.asList(offset), editor);
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
        Document document = editor.getDocument();
        List<Integer> offsets = markers.stream().map(Marker2::getStartOffset).collect(Collectors.toList());
        EditorUtil.performPaste(offsets, editor);
        exitAction();
    }


}
