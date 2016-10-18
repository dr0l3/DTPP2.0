package marker;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.util.TextRange;

import java.awt.*;

/**
 * Created by runed on 15-10-2016.
 */
public class Marker2 {
    private int startOffset;
    private int endOffset;
    private String markerText;
    private String replacementText;
    private boolean primaryMarker;

    public Marker2(int startOffset, int endOffset, String markerText, String replacementText) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.markerText = markerText;
        this.replacementText = replacementText;
        this.primaryMarker = true;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public String getMarkerText() {
        return markerText;
    }

    public void setMarkerText(String markerText) {
        this.markerText = markerText;
    }

    public String getReplacementText() {
        return replacementText;
    }

    public void setReplacementText(String replacementText) {
        this.replacementText = replacementText;
    }

    public boolean isPrimaryMarker() {
        return primaryMarker;
    }

    public void setPrimaryMarker(boolean primaryMarker) {
        this.primaryMarker = primaryMarker;
    }

    public boolean isVisible(Editor editor){
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
        LogicalPosition startLogicalPosition = editor.xyToLogicalPosition(visibleArea.getLocation());
        Double endVisualX = visibleArea.getX() + visibleArea.getWidth();
        Double endVisualY = visibleArea.getY() + visibleArea.getHeight();
        LogicalPosition endLogicalPosition = editor.xyToLogicalPosition(new Point(endVisualX.intValue(), endVisualY.intValue()));

        int firstVisibleOffset = editor.logicalPositionToOffset(startLogicalPosition);
        int lastVisibleOffset = editor.logicalPositionToOffset(endLogicalPosition);
        return (firstVisibleOffset < this.startOffset) && (lastVisibleOffset > this.endOffset);
    }
}
