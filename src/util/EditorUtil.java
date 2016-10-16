/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.ide.CopyPasteManager;
import com.intellij.openapi.util.TextRange;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * Created by Rune on 09-10-2015.
 */
public class EditorUtil {

    public static void performDelete(int startOffset, int endOffset, Editor editor){
        Document document = editor.getDocument();
        WriteCommandAction.runWriteCommandAction(editor.getProject(), () -> {
            document.replaceString(
                    startOffset,endOffset, "");
        });
        editor.getSelectionModel().removeSelection();

    }

    public static void perforMark(int startOffset, int endOffset, Editor editor){

    }

    public static void performPaste(java.util.List<Integer> offsets, Editor editor){
        Document document = editor.getDocument();
        Runnable runnable = () -> {
            CopyPasteManager cpmanager = CopyPasteManager.getInstance();
            try {
                if(cpmanager.getContents()== null ) {
                    return;
                }
                DataFlavor[] dataFlavor = cpmanager.getContents().getTransferDataFlavors();
                Object toBePasted = (cpmanager.getContents(dataFlavor[0]));
                for (Integer offset : offsets) {
                    if (toBePasted instanceof String) {
                        document.replaceString(offset, offset, ((String) toBePasted));
                    } else {
                        document.replaceString(offset, offset, String.valueOf(toBePasted));
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        };
        WriteCommandAction.runWriteCommandAction(editor.getProject(), runnable);
    }

    public static void performCut(int startOffset, int endOffset, Editor editor){

    }

    public static void performCopy(int startOffset, int endOffset, Editor editor){
        editor.getSelectionModel().setSelection(startOffset,endOffset);
        editor.getSelectionModel().copySelectionToClipboard();
        editor.getSelectionModel().removeSelection();
    }
    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                c != KeyEvent.CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    public static char getCounterCase(char c) {
        return Character.isUpperCase(c) ? Character.toLowerCase(c) : Character.toUpperCase(c);
    }

    public static TextRange getVisibleTextRange(Editor editor) {
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
        LogicalPosition startLogicalPosition = editor.xyToLogicalPosition(visibleArea.getLocation());
        Double endVisualX = visibleArea.getX() + visibleArea.getWidth();
        Double endVisualY = visibleArea.getY() + visibleArea.getHeight();
        LogicalPosition endLogicalPosition = editor.xyToLogicalPosition(new Point(endVisualX.intValue(), endVisualY.intValue()));

        return new TextRange(editor.logicalPositionToOffset(startLogicalPosition), editor.logicalPositionToOffset(endLogicalPosition));
    }
}
