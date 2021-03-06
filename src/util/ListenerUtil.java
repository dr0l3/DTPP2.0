package util;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import marker.MarkerPanel2;
import swingutil.KeyStrokeToActionBinding;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by runed on 23-10-2016.
 */
public class ListenerUtil {
    private static final String SET_SELECTING = "setSelecting";
    private static final String SEARCH_UP = "searchUp";
    private static final String SEARCH_DOWN = "searchDown";
    private static final String PERFORM_ACTION_AT_FIRST_UP = "performActionAtFirstUp";
    private static final String PERFORM_ACTION_AT_FIRST_DOWN = "performActionAtFirstDown";
    private static final String PERFORM_ACTION_AT_ALL = "performActionAtAll";

    public static List<DocumentListener> getDocumentListeners(VersionTwoCustomAction action, MarkerPanel2 markerPanel){
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (action.isSelecting()) {
                    return;
                }
                try {
                    int lengthOfText = e.getDocument().getLength();
                    String text = e.getDocument().getText(0,lengthOfText);
                    markerPanel.updateMarkers(text);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        };
        return Collections.singletonList(documentListener);
    }



    public static List<KeyStrokeToActionBinding> getKeybindings(VersionTwoCustomAction action, MarkerPanel2 markerPanel, JTextField textField){
        List<KeyStrokeToActionBinding> bindings = new ArrayList<>();
        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                SET_SELECTING,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        action.setSelecting(true);
                        textField.setEditable(false);
                        System.out.println("now selecting");
                    }
                }
        ));

        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.ALT_DOWN_MASK),
                SEARCH_UP,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        markerPanel.searchFurther(true);
                        System.out.println("Searching up");
                    }
                }
        ));

        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.ALT_DOWN_MASK),
                SEARCH_DOWN,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        markerPanel.searchFurther(false);
                        System.out.println("Searching down");
                    }
                }
        ));

        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK),
                PERFORM_ACTION_AT_FIRST_UP,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("going to first occurence up");
                        markerPanel.handleSelectFirstOccurence(true);
                    }
                }
        ));

        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK),
                PERFORM_ACTION_AT_FIRST_DOWN,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("going to first occurence down");
                        markerPanel.handleSelectFirstOccurence(false);
                    }
                }
        ));

        bindings.add(new KeyStrokeToActionBinding(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK),
                PERFORM_ACTION_AT_ALL,
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("doing all the things");
                        markerPanel.handleSelectAll();
                    }
                }
        ));
        return bindings;
    }

}
