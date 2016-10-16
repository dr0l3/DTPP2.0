package action;

import com.intellij.openapi.editor.Document;
import marker.Marker2;
import util.EditorUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by runed on 16-10-2016.
 */
public class PasteToTargets extends VersionTwoCustomAction {
    @Override
    public void PerformActionAtMarker(Marker2 marker) {
        int offset = marker.getStartOffset();
        EditorUtil.performPasteFromClipboard(Arrays.asList(offset), editor);
        exitAction();
    }

    @Override
    public void PerformActionAtMultipleMarkers(List<Marker2> markers) {
        Document document = editor.getDocument();
        List<Integer> offsets = markers.stream().map(Marker2::getStartOffset).collect(Collectors.toList());
        EditorUtil.performPasteFromClipboard(offsets, editor);
        exitAction();
    }


}
