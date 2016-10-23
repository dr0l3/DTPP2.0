package marker;

import action.VersionTwoCustomAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.markup.*;
import com.intellij.openapi.util.TextRange;
import com.intellij.ui.JBColor;
import util.EditorUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

import static util.EditorUtil.getVisibleTextRange;


/**
 * Created by runed on 15-10-2016.
 */
public class MarkerPanel2 extends JComponent{
    private static final String MARKER_SET = "ASKDLJOWIEXUHCVNMBZRFTGYPÅØÆ";
    private Editor editor;
    private ArrayList<Marker2> markerCollection;
    private String searchString;
    private JComponent parent;
    private Font fontIneditor;
    private VersionTwoCustomAction callingAction;
    private int contextPoint;
    private int selectCharCount;
    private boolean searchingInEntireDocument;

    public MarkerPanel2(Editor editor, VersionTwoCustomAction callingAction, int contextPoint) {
        this.editor = editor;
        this.parent = editor.getContentComponent();
        this.markerCollection = new ArrayList<>();
        this.fontIneditor = editor.getColorsScheme().getFont(EditorFontType.BOLD);
        this.searchString = "";
        this.callingAction = callingAction;
        this.contextPoint = contextPoint;
        this.searchingInEntireDocument = false;
        this.selectCharCount = 0;
        setupLocationAndBoundsOfPanel(editor);
    }

    public void updateMarkers(String searchString){
        String oldSearchString = this.searchString;
        if(oldSearchString.equals(searchString)){ //performance: dont rerun on same input
            System.out.println("Aborting paint, old = new search string");
            return;
        }

        System.out.println("Now searching for string: " + searchString);
        this.searchString = searchString;
        if(searchString.equals("")){ //reset marker collection if searchstring is empty
            markerCollection = new ArrayList<>();
            this.repaint();
            return;
        }

        this.markerCollection = getMatchesForStringInTextRange(searchString, getVisibleTextRange(editor), editor.getDocument());
        if(markerCollection.isEmpty()){ //if no matches in visible text search in entire document
            System.out.println("Searching in entire document");
            this.searchingInEntireDocument = true;
            this.markerCollection = getMatchesForStringInTextRange(searchString, new TextRange(0,editor.getDocument().getTextLength()), editor.getDocument());
            scrollToMarkerCollection();
            setupLocationAndBoundsOfPanel(editor);
        }
        SortMarkersAndAssignReplacementText();
        this.repaint();
    }

    private void scrollToMarkerCollection() {
        //do we go up or down?
        long markersDownwards = markerCollection.stream()
                .filter(marker2 -> marker2.getStartOffset() > contextPoint)
                .count();
        long markersUpwards = markerCollection.size()-markersDownwards;
        int offset;
        //center on first occurence
        if(markersDownwards> markersUpwards && markersDownwards > 0){
            System.out.println("Scrolling to markerlocation downwards");
            offset = markerCollection.stream()
                    .filter(marker2 -> marker2.getStartOffset() > contextPoint)
                    .sorted(new MarkerComparator(this.contextPoint))
                    .findFirst().get().getStartOffset();
            EditorUtil.performScrollToPosition(editor, offset);
            setContextPoint(offset);
        } else if(markersUpwards > 0){
            System.out.println("Scrolling to markerlocation upwards");
            offset = markerCollection.stream()
                    .filter(marker2 -> marker2.getStartOffset() < contextPoint)
                    .sorted(new MarkerComparator(this.contextPoint))
                    .findFirst().get().getStartOffset();
            EditorUtil.performScrollToPosition(editor, offset);
            setContextPoint(offset);
        }
    }


    private void SortMarkersAndAssignReplacementText() {
        this.markerCollection.sort((o1, o2) -> {
            int o1ToCaret = Math.abs(o1.getStartOffset() - this.contextPoint);
            int o2ToCaret = Math.abs(o2.getStartOffset() - this.contextPoint);
            return o1ToCaret - o2ToCaret;
        });

        char[] markerSetChars = MARKER_SET.toCharArray();
        for(int i = 0; i < markerCollection.size(); i++){
            Marker2 marker = markerCollection.get(i);
            if(i < MARKER_SET.length()-2){
                marker.setReplacementText(String.valueOf(markerSetChars[i]));
                marker.setPrimaryMarker(true);
                continue;
            }
            marker.setReplacementText(String.valueOf(markerSetChars[markerSetChars.length-1]));
            marker.setPrimaryMarker(false);
        }
    }

    private ArrayList<Marker2> getMatchesForStringInTextRange(String typedChar, TextRange textRange, Document document) {
        ArrayList<Marker2> markers = new ArrayList<>();
        int startOffset = textRange.getStartOffset();
        int currentCaretOffset = editor.getCaretModel().getCurrentCaret().getOffset();
        String visibleText = document.getText(textRange).toLowerCase();
        visibleText = visibleText.replace("\n", " ");
        visibleText = visibleText.replace("\r", " ");
        visibleText = visibleText.replace("\t", " ");
        int index = -1;
        while(true){
            index = visibleText.indexOf(typedChar, index + 1);
            if(index == -1){
                return markers;
            }
            int offset = startOffset + index;
            //exclude current caret position
            if(offset == currentCaretOffset){
                continue;
            }
            //exclude multiple spaces in a row
            if(index > 1 && index < visibleText.length()-2 && stringOnlyContainsSpaces(this.searchString) && visibleText.charAt(index-1) == ' ' && document.getLineEndOffset(document.getLineNumber(offset)) != offset){
                continue;
            }
            markers.add(new Marker2(offset, offset, typedChar, ""));
        }
    }

    private boolean stringOnlyContainsSpaces(String toBeChecked){
        for (char character : toBeChecked.toCharArray()){
            if(!(character == ' ')){
                return false;
            }
        }
        return true;
    }


    @Override
    public void paint(Graphics g){
//        System.out.println("painting");
        g.setFont(fontIneditor);
        MarkupModel markupModel = editor.getMarkupModel();
        markupModel.removeAllHighlighters();
        for (Marker2 marker : markerCollection){
            drawBackgroundOfMarker(g, marker);
            drawMarkerChar(g,marker);
        }
    }

    private void drawBackgroundOfMarker(Graphics graphics, Marker2 marker) {
        Rectangle2D fontRect = parent.getFontMetrics(fontIneditor).getStringBounds(String.valueOf(marker.getMarkerText()), graphics);

        graphics.setColor(JBColor.WHITE);

        double x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getX();
        double y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getEndOffset())).getY();

        graphics.fillRect((int)x, (int)y, (int) fontRect.getWidth(), (int) fontRect.getHeight());
        if(marker.getMarkerText().length()> 1) {
            graphics.setColor(JBColor.GRAY);
            x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset()+1)).getX();
            y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getEndOffset())).getY();
            float bottomYOfMarkerChar = (float) (y + fontIneditor.getSize());
            graphics.drawString(marker.getMarkerText().substring(1, marker.getMarkerText().length()), (int) x, (int) bottomYOfMarkerChar);
        }
    }

    private void drawMarkerChar(Graphics g, Marker2 marker) {
        double x = parent.getX() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getX();
        double y = parent.getY() + editor.logicalPositionToXY(editor.offsetToLogicalPosition(marker.getStartOffset())).getY();
        float bottomYOfMarkerChar = (float) (y + fontIneditor.getSize());

        if(marker.isPrimaryMarker()) {
            g.setColor(JBColor.RED);
        } else {
            g.setColor(JBColor.YELLOW);
        }

        ((Graphics2D)g).drawString(marker.getReplacementText(), (float)x, bottomYOfMarkerChar);
        MarkupModel markupModel = editor.getMarkupModel();
        TextAttributes attributes = new TextAttributes();
        attributes.setEffectType(EffectType.SEARCH_MATCH);
        attributes.setBackgroundColor(JBColor.WHITE);
        markupModel.addRangeHighlighter(marker.getStartOffset(), marker.getEndOffset(), HighlighterLayer.SELECTION, attributes, HighlighterTargetArea.EXACT_RANGE);

    }

    private void setupLocationAndBoundsOfPanel(Editor editor) {
        System.out.println("Setting up the visible area");
        this.setLocation(0, 0);
        this.invalidate();
        Rectangle visibleArea = editor.getScrollingModel().getVisibleAreaOnScrollingFinished();
        System.out.println("Visible area: " + visibleArea.toString());
        int x = (int) (parent.getLocation().getX() + visibleArea.getX() + editor.getScrollingModel().getHorizontalScrollOffset());
        this.setBounds(x, (int) (visibleArea.getY()), (int) visibleArea.getWidth(), (int) visibleArea.getHeight());
    }

    public void handleSelectFirstOccurence(boolean upwards){
        Optional<Marker2> goTomarker = markerCollection.stream()
                .filter(marker2 -> marker2.getStartOffset() < this.contextPoint ==  upwards)
                .sorted(new MarkerComparator(this.contextPoint))
                .findFirst();
        if(goTomarker.isPresent()) {
            callingAction.initiateActionAtMarker(goTomarker.get());
        }
    }

    public void handleSelect(String selectedChar) {
        if(selectedChar.equals("\n")){
            selectCharCount++;
        }
        if(selectedChar.equals("\n") && callingAction.isSelecting() && selectCharCount > 1){
            widenMarkerNet();
            return;
        }
        Optional<Marker2> marker = markerCollection.stream()
                .filter(marker2 -> marker2.getReplacementText().toLowerCase().equals(selectedChar))
                .findFirst();
        if(marker.isPresent()) {
            callingAction.initiateActionAtMarker(marker.get());
        }
    }

    private void widenMarkerNet() {
        System.out.println("Æ selected");
        ArrayList<Marker2> selectedMarkers = new ArrayList<>();
        markerCollection.stream()
                .filter(marker2 -> marker2.getReplacementText().toLowerCase().equals(MARKER_SET.substring(MARKER_SET.length()-1).toLowerCase()) || !marker2.isVisible(editor))
                .forEach(selectedMarkers::add);
        markerCollection = selectedMarkers;
        //if no visible markers
        int numberOfVisibleMarkers = numberOfVisibleMarkers();
        System.out.println("Number of visible markers: " + numberOfVisibleMarkers);
        if(numberOfVisibleMarkers < 1){
            if(!searchingInEntireDocument) {
                this.searchingInEntireDocument = true;
                this.markerCollection = getMatchesForStringInTextRange(searchString, new TextRange(0, editor.getDocument().getTextLength()), editor.getDocument());
            }
            scrollToMarkerCollection();
            setupLocationAndBoundsOfPanel(editor);
        }
        SortMarkersAndAssignReplacementText();
        this.repaint();
    }

    private int numberOfVisibleMarkers() {
        long numberOfVisibleMarkers = markerCollection.stream()
                .filter(marker2 -> marker2.isVisible(editor))
                .count();
        return (int) numberOfVisibleMarkers;
    }

    public void handleSelectAll() {
        callingAction.initiateActionAtMarkers(markerCollection);
    }

    public int getContextPoint() {
        return contextPoint;
    }

    public void setContextPoint(int contextPoint) {
        this.contextPoint = contextPoint;
    }

    public void searchFurther(boolean up){
        //sanity check
        if(this.markerCollection.size() == 0 || this.searchString.equals("")){
            return;
        }
        //if not searching in entire document, do so
        if(!searchingInEntireDocument){
            this.searchingInEntireDocument = true;
            this.markerCollection = getMatchesForStringInTextRange(searchString, new TextRange(0, editor.getDocument().getTextLength()), editor.getDocument());
        }
        //scroll to some position
        Optional<Marker2> contextMarker = this.markerCollection.stream()
                .filter(marker2 -> !marker2.isVisible(editor) && marker2.getStartOffset() < contextPoint == up)
                .sorted(new MarkerComparator(this.contextPoint))
                .findFirst();
        if(!contextMarker.isPresent()){
            return;
        }
        int offset = contextMarker.get().getStartOffset();
        //set the context point
        this.contextPoint = offset;
        //repaint etc.
        EditorUtil.performScrollToPosition(editor,offset);
        setupLocationAndBoundsOfPanel(editor);
        SortMarkersAndAssignReplacementText();
        this.repaint();
    }

    public int getSelectCharCount() {
        return selectCharCount;
    }

    public void setSelectCharCount(int selectCharCount) {
        this.selectCharCount = selectCharCount;
    }
}
