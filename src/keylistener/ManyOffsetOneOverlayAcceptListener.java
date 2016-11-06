package keylistener;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Editor;
import marker.Marker2;
import util.ManyOffsetEditorAction;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by runed on 11/6/2016.
 */
public class ManyOffsetOneOverlayAcceptListener extends AcceptListener {
    private List<Integer> offsets;
    private ManyOffsetEditorAction action;

    public ManyOffsetOneOverlayAcceptListener(VersionTwoCustomAction callingAction, Editor editor, Marker2 marker,List<Integer> offsets, ManyOffsetEditorAction action) {
        super(callingAction, editor, marker);
        this.offsets = offsets;
        this.action = action;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //escape
        if (e.getKeyChar() == '\u001B') {
            super.handleEscapeDuringAcceptForSingleOverlayAction();
        } else {
            action.performAction(offsets, editor);
            editor.getContentComponent().removeKeyListener(this);
            Arrays.stream(callingAction.getSuspendedKeylisteners()).forEach(editor.getContentComponent()::addKeyListener);
            callingAction.setExitOnEscape(true);
            callingAction.exitAction(editor);
        }
    }
}
