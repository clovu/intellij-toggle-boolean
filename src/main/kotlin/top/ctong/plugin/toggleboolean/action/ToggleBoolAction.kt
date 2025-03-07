package top.ctong.plugin.toggleboolean.action

import com.intellij.codeInsight.editorActions.SelectWordUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import top.ctong.plugin.toggleboolean.settings.BoolCvsSettings
import top.ctong.plugin.toggleboolean.utils.BoolCvs
import top.ctong.plugin.toggleboolean.utils.StringUtils

class ToggleBoolAction : AnAction() {

    /**
     * Performs the action logic.
     *
     *
     * It is called on the UI thread with all data in the provided [DataContext] instance.
     *
     * @see .beforeActionPerformedUpdate
     */
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(PlatformDataKeys.EDITOR) ?: return
        val document = editor.document

        val mappings = BoolCvsSettings.getInstance().mappings

        WriteCommandAction.runWriteCommandAction(project) {
            val range = getWordRangeAtPosition(editor = editor)
            if (range != null) {
                val word = document.charsSequence.subSequence(range.startOffset, range.endOffset).toString()
                if (StringUtils.isNotBlank(word)) {
                    val mapping = mappings.associate { it.source to it.target }
                    val replaceWord = BoolCvs.findMapping(word, mapping)
                    if (replaceWord != null)
                        document.replaceString(range.startOffset, range.endOffset, replaceWord)
                }
            }
        }
    }

    private fun findElementAt(project: Project, editor: Editor): PsiElement? {
        val document = editor.document
        val caret = editor.caretModel.currentCaret
        return PsiDocumentManager.getInstance(project).getPsiFile(document)?.findElementAt(caret.offset)
    }

    private fun getWordRangeAtPosition(editor: Editor): TextRange? {
        val project = editor.project ?: return null
        val document = editor.document
        val caret = editor.caretModel.currentCaret

        val element = findElementAt(project, editor)
        val lineRange = TextRange.create(caret.visualLineStart, caret.visualLineEnd)
        val minimumRange = Ref.create(TextRange.create(0, document.textLength))

        SelectWordUtil.processRanges(
            element, document.charsSequence, caret.offset, editor, ProcessorHandler(lineRange, minimumRange)
        )

        return minimumRange.get()
    }

}