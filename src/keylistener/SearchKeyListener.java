package keylistener;

import action.VersionTwoCustomAction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by runed on 15-10-2016.
 */
public class SearchKeyListener implements KeyListener {
    private VersionTwoCustomAction callingAction;

    public SearchKeyListener(VersionTwoCustomAction callingAction) {
        this.callingAction = callingAction;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(callingAction.isSelecting()){
            /*if((e.getKeyChar() == '\n')){
                return;
            }*/
            if(e.getKeyChar() == KeyEvent.VK_F2 || e.getKeyChar() == KeyEvent.VK_F3){
                System.out.println("up or down");
                return;
            }
            System.out.println("Handling select");
            callingAction.handleSelect(String.valueOf(e.getKeyChar()));
            return;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
