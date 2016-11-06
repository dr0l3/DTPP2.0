package keylistener;

import action.VersionTwoCustomAction;
import marker.MarkerPanel2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by runed on 15-10-2016.
 */
public class SearchKeyListener implements KeyListener {
    private VersionTwoCustomAction callingAction;
    private MarkerPanel2 markerPanel;

    public SearchKeyListener(VersionTwoCustomAction callingAction, MarkerPanel2 markerPanel2) {
        this.callingAction = callingAction;
        this.markerPanel = markerPanel2;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(!callingAction.isSelecting()){
            return;
        }
        System.out.println("Handling select");
        markerPanel.handleSelect(String.valueOf(e.getKeyChar()));
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
