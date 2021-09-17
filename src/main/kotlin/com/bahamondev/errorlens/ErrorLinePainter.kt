package com.bahamondev.errorlens

import com.intellij.codeInsight.daemon.impl.DaemonCodeAnalyzerEx
import com.intellij.codeInsight.daemon.impl.HighlightInfo
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorLinePainter
import com.intellij.openapi.editor.LineExtensionInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.ui.SimpleTextAttributes
import java.awt.Color
import java.util.Collections

internal class ErrorLinePainter : EditorLinePainter() {

    override fun getLineExtensions(
        project: Project,
        file: VirtualFile,
        lineNumber: Int
    ): MutableCollection<LineExtensionInfo>? {
        val psiFile = PsiManager.getInstance(project).findFile(file)
        val document = psiFile?.let { PsiDocumentManager.getInstance(project).getDocument(it) }
        if (psiFile == null || document == null) {
            return null
        }

        val lineError = findFirstError(document, project, lineNumber)
        return lineError?.let { errorDescriptionToLineInfo(it.description) }
    }

    private fun findFirstError(
        document: Document,
        project: Project,
        lineNumber: Int
    ): HighlightInfo? {
        var info: HighlightInfo? = null
        val retrieveFirstErrorAndStop: (HighlightInfo) -> Boolean = { info = it; false }
        DaemonCodeAnalyzerEx.processHighlights(
            document,
            project,
            HighlightSeverity.ERROR,
            document.getLineStartOffset(lineNumber),
            document.getLineEndOffset(lineNumber),
            retrieveFirstErrorAndStop
        )
        return info
    }

    private fun errorDescriptionToLineInfo(errorDescription: String) =
        //Replace separator with default IDE tab size???
        Collections.singletonList(LineExtensionInfo("    $errorDescription", textAttributes()))

    private fun textAttributes(): TextAttributes {
        return TextAttributes(
            Color.decode(Settings.ERROR_FOREGROUND_COLOR),
            SimpleTextAttributes.REGULAR_ATTRIBUTES.bgColor,
            null,
            null,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.fontStyle
        )
    }

}
