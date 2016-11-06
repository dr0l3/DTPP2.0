package keylistener;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

/**
 * Created by runed on 11/6/2016.
 */
public class AcceptListener implements KeyListener {
    protected VersionTwoCustomAction callingAction;
    protected Editor editor;
    protected Marker2 marker;

    public AcceptListener(VersionTwoCustomAction callingAction, Editor editor, Marker2 marker) {
        this.callingAction = callingAction;
        this.editor = editor;
        this.marker = marker;
    }

    protected void handleEscapeDuringAcceptForSingleOverlayAction(){
        callingAction.setExitOnEscape(true);
        callingAction.exitAction(editor);
        Arrays.stream(callingAction.getSuspendedKeylisteners()).forEach(editor.getContentComponent()::addKeyListener);
        editor.getContentComponent().removeKeyListener(this);
        callingAction.onActionPerformed(editor, marker.getMarkerText());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
