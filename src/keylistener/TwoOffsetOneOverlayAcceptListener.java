package keylistener;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import util.TwoOffsetEditorAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Created by runed on 11/6/2016.
 */
public class TwoOffsetOneOverlayAcceptListener extends AcceptListener implements KeyListener {
    private TwoOffsetEditorAction action;

    public TwoOffsetOneOverlayAcceptListener(VersionTwoCustomAction callingAction, Editor editor, Marker2 marker, TwoOffsetEditorAction action) {
        super(callingAction, editor, marker);
        this.action = action;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //escape
        if (e.getKeyChar() == '\u001B') {
            super.handleEscapeDuringAcceptForSingleOverlayAction();
        } else {
            callingAction.findOffsetsAndPerformAction(action, marker, editor);
            editor.getContentComponent().removeKeyListener(this);
            Arrays.stream(callingAction.getSuspendedKeylisteners()).forEach(editor.getContentComponent()::addKeyListener);
            callingAction.setExitOnEscape(true);
            callingAction.exitAction(editor);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
