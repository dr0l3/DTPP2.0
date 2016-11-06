package keylistener;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import marker.MarkerPanel2;
import util.EditorUtil;
import util.TwoOffsetEditorAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Created by runed on 11/6/2016.
 */
@SuppressWarnings("Duplicates")
public class DoubleOverlayAcceptActionListener implements KeyListener {
    private VersionTwoCustomAction callingAction;
    private Editor editor;
    private Marker2 marker;
    private MarkerPanel2 markerPanel;
    private TwoOffsetEditorAction action;

    public DoubleOverlayAcceptActionListener(VersionTwoCustomAction callingAction, Editor editor, Marker2 marker, MarkerPanel2 markerPanel, TwoOffsetEditorAction action) {
        this.callingAction = callingAction;
        this.editor = editor;
        this.marker = marker;
        this.markerPanel = markerPanel;
        this.action = action;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //escape
        if (e.getKeyChar() == '\u001B') {
            callingAction.setExitOnEscape(true);
            callingAction.exitAction(editor);
            Arrays.stream(callingAction.getSuspendedKeylisteners()).forEach(editor.getContentComponent()::addKeyListener);
            editor.getContentComponent().removeKeyListener(this);
            util.EditorUtil.performMarkRange(marker.getStartOffset(), marker.getStartOffset() + 1, editor);
            callingAction.setupSecondOverLay(marker, markerPanel, editor);
        } else {
            callingAction.findOffsetsAndPerformAction(action, marker,editor);
            editor.getContentComponent().removeKeyListener(this);
            Arrays.stream(callingAction.getSuspendedKeylisteners()).forEach(editor.getContentComponent()::addKeyListener);
            callingAction.setExitOnEscape(true);
            callingAction.exitAction(editor);
            //Arrays.stream(keyListeners).forEach(kl -> kl.keyTyped(e));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
