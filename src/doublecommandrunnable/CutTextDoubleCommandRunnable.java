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
package doublecommandrunnable;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;

/**
 * Created by Rune on 10-10-2015.
 */
public class CutTextDoubleCommandRunnable implements Runnable{
  private int startOffset;
  private int endOffset;
  private Editor editor;

  public CutTextDoubleCommandRunnable(int startOffset, int endOffset, Editor editor) {
    this.startOffset = startOffset;
    this.endOffset = endOffset;
    this.editor = editor;
  }

  @Override
  public void run() {
    final Document document = editor.getDocument();
    final Project project = editor.getProject();
    if(endOffset < startOffset){
      editor.getSelectionModel().setSelection(endOffset,startOffset + 1);
    } else {
      editor.getSelectionModel().setSelection(startOffset,endOffset + 1);
    }
    editor.getSelectionModel().copySelectionToClipboard();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        if(startOffset > endOffset+1){
          document.replaceString(
                  endOffset,startOffset + 1, "");
        } else {
          document.replaceString(
                  startOffset, endOffset + 1, "");
        }
      }
    };
    WriteCommandAction.runWriteCommandAction(project, runnable);
    editor.getSelectionModel().removeSelection();
  }
}
