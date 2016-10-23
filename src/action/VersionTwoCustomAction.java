package action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import keylistener.SearchKeyListener;
import marker.Marker2;
import marker.MarkerPanel2;
import popup.SearchPopupListener;
import util.OneOffsetEditorAction;
import util.TwoOffsetEditorAction;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

/**
 * Created by runed on 15-10-2016.
 */
public abstract class VersionTwoCustomAction extends AnAction {
    private static final String SET_SELECTING = "setSelecting";
    private static final String SEARCH_UP = "searchUp";
    private static final String SEARCH_DOWN = "searchDown";
    private static final String PERFORM_ACTION_AT_FIRST_UP = "performActionAtFirstUp";
    private static final String PERFORM_ACTION_AT_FIRST_DOWN = "performActionAtFirstDown";
    private static final String PERFORM_ACTION_AT_ALL = "performActionAtAll";
    protected JComponent contentComponent;
    protected Editor editor;
    protected KeyListener searchListener;
    protected MarkerPanel2 markerPanel;
    protected JTextField searchTextField;
    protected SearchPopupListener popupListener;
    protected JBPopup popup;
    protected boolean isSelecting;
    protected boolean isSecondOverlay;
    protected int offsetFromFirstOverlay;

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        //setup
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        editor = anActionEvent.getData(CommonDataKeys.EDITOR);
        if (editor == null || project == null) {
            return;
        }
        contentComponent = editor.getContentComponent();
        markerPanel = new MarkerPanel2(editor, this, editor.getCaretModel().getCurrentCaret().getOffset());
        contentComponent.add(markerPanel);
        isSelecting = false;
        isSecondOverlay = false;
        searchListener = new SearchKeyListener(this);
        searchTextField = setupSearchTextField(searchListener);
        popupListener = new SearchPopupListener(this);
        JPanel panel = new JPanel(new BorderLayout());

        removeExistingPopup();
        popup = setupPopupAndBindActionListeners(searchTextField, popupListener, panel);
        calculatePositionAndShowPopup(popup, contentComponent);
        searchTextField.requestFocus();
    }

    private void removeExistingPopup() {
        if (popup != null) {
            popup.removeListener(popupListener);
            popup.cancel();
        }
    }

    private JBPopup setupPopupAndBindActionListeners(JTextField searchTextField, SearchPopupListener listener, JPanel panel) {
        JBPopup popup;
        panel.add(searchTextField, BorderLayout.WEST);

        popup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(panel, panel)
                .addListener(listener)
                .setFocusable(true)
                .setMovable(false)
                .setShowBorder(true)
                .createPopup();
        return popup;
    }

    private void calculatePositionAndShowPopup(JBPopup popup, JComponent contentComponent) {
        RelativePoint popupLocation = JBPopupFactory.getInstance().guessBestPopupLocation(contentComponent);
        popup.show(popupLocation);
    }

    private JTextField setupSearchTextField(KeyListener searchListener) {
        JTextField textField = new JTextField();
        textField.addKeyListener(searchListener);
        textField.setText("");
        textField.setColumns(10);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (isSelecting()) {
                    return;
                }
                handleSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (isSelecting()) {
                    return;
                }
                handleSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (isSelecting()) {
                    return;
                }
                handleSearch();
            }
        });

        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), SET_SELECTING);
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.ALT_DOWN_MASK), SEARCH_UP);
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.ALT_DOWN_MASK), SEARCH_DOWN);
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK), PERFORM_ACTION_AT_FIRST_UP);
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.ALT_DOWN_MASK), PERFORM_ACTION_AT_FIRST_DOWN);
        textField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK | KeyEvent.ALT_DOWN_MASK), PERFORM_ACTION_AT_ALL);

        textField.getActionMap().put(SEARCH_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markerPanel.searchFurther(true);
                System.out.println("Searching up");
            }
        });

        textField.getActionMap().put(SEARCH_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                markerPanel.searchFurther(false);
                System.out.println("Searching down");
            }
        });

        textField.getActionMap().put(SET_SELECTING, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isSelecting = true;
                searchTextField.setEditable(false);
                System.out.println("now selecting");
            }
        });

        textField.getActionMap().put(PERFORM_ACTION_AT_FIRST_UP, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("going to first occurence up");
                handleSelectFirstOccurence(true);
            }
        });

        textField.getActionMap().put(PERFORM_ACTION_AT_FIRST_DOWN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("going to first occurence down");
                handleSelectFirstOccurence(false);
            }
        });

        textField.getActionMap().put(PERFORM_ACTION_AT_ALL, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("doing all the things");
                handleSelectAll();
            }
        });

        return textField;
    }

    public void handleSelectAll() {
        markerPanel.handleSelectAll();
    }

    public void handleSelectFirstOccurence(boolean upwards) {
        markerPanel.handleSelectFirstOccurence(upwards);
    }

    public void handleSearch() {
        markerPanel.updateMarkers(searchTextField.getText());
        contentComponent.repaint();
    }

    public void handleSelect(String selectedChar) {
        markerPanel.handleSelect(selectedChar);
    }

    public void exitAction() {
        editor.getScrollingModel().scrollToCaret(ScrollType.MAKE_VISIBLE);
        markerPanel.updateMarkers("");
        contentComponent.remove(markerPanel);
        contentComponent.repaint();
        if (!popup.isDisposed()) {
            popup.cancel();
        }
        this.popup = null;
    }

    public boolean isSelecting() {
        return isSelecting;
    }

    protected void setupSecondOverLay(Marker2 marker) {
        markerPanel.setSelectCharCount(0);
        isSecondOverlay = true;
        offsetFromFirstOverlay = marker.getStartOffset();
        isSelecting = false;
        searchListener = new SearchKeyListener(this);
        searchTextField = setupSearchTextField(searchListener);
        popupListener = new SearchPopupListener(this);
        JPanel panel = new JPanel(new BorderLayout());
        removeExistingPopup();
        popup = setupPopupAndBindActionListeners(searchTextField, popupListener, panel);
        calculatePositionAndShowPopup(popup, contentComponent);
        searchTextField.requestFocus();
        handleSearch();
    }

    public abstract void initiateActionAtMarker(Marker2 marker);

    public abstract void initiateActionAtMarkers(List<Marker2> markers);

    public void findOffsetsAndPerformAction(TwoOffsetEditorAction toBePerformed, Marker2 marker) {
        int offset = marker.getStartOffset();
        int currentOffset;
        if (isSecondOverlay) {
            currentOffset = offsetFromFirstOverlay;
        } else {
            currentOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        }
        if (currentOffset < offset) {
            toBePerformed.performAction(currentOffset, offset, editor);
        } else {
            toBePerformed.performAction(offset, currentOffset, editor);
        }
        //TODO: should not be part of this method
        exitAction();
    }

    public void findSingleOffsetAndPerformAction(OneOffsetEditorAction toBePerformed, Marker2 marker) {
        int offset = marker.getStartOffset();
        toBePerformed.performAction(offset, editor);
        //TODO: should not be part of this method
        exitAction();
    }
}
