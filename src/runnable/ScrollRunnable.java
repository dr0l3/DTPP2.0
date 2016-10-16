package runnable;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.editor.VisualPosition;
import groovy.util.logging.Log;

/**
 * Created by runed on 11-10-2016.
 */
public class ScrollRunnable implements Runnable {
    private Editor editor;
    private int linesToMove;

    public ScrollRunnable(Editor editor, int linesToMove) {
        this.editor = editor;
        this.linesToMove = linesToMove;
    }

    @Override
    public void run() {
        VisualPosition pos = editor.getCaretModel().getCurrentCaret().getLogicalPosition().toVisualPosition();
        LogicalPosition position = editor.getCaretModel().getLogicalPosition();
        int linenumber = ((pos.getLine() + linesToMove) > 0) ? (pos.getLine() + linesToMove) : 0;
        int currentcollumn = pos.getColumn();
        LogicalPosition newLogPos = new LogicalPosition(linenumber, currentcollumn);
        VisualPosition newpos = new VisualPosition(linenumber,currentcollumn);
        editor.getCaretModel().getCurrentCaret().moveToLogicalPosition(newLogPos);
        editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
    }
}
