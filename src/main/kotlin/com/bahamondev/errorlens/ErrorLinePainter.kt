package com.bahamondev.errorlens

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorLinePainter
import com.intellij.openapi.editor.LineExtensionInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiRecursiveElementWalkingVisitor
import com.intellij.ui.SimpleTextAttributes
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

        val lineErrors = findLineErrors(psiFile, lineNumber, document)

        return lineErrors.firstOrNull()?.let { Collections.singletonList(errorDescriptionToLineInfo(it)) }
    }

    private fun errorDescriptionToLineInfo(errorElement: PsiErrorElement) =
        LineExtensionInfo(" ${errorElement.errorDescription}", textAttributes())

    private fun textAttributes(): TextAttributes {
        return TextAttributes(
            SimpleTextAttributes.REGULAR_ATTRIBUTES.fgColor,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.bgColor,
            null,
            null,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.fontStyle
        )
    }

    private fun findLineErrors(
        psiFile: PsiFile,
        lineNumber: Int,
        document: Document
    ): MutableList<PsiErrorElement> {
        val lineErrors = mutableListOf<PsiErrorElement>()

        psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun elementFinished(element: PsiElement?) {
                if (element is PsiErrorElement && lineNumber == document.lineNumber(element)) {
                    lineErrors.add(element)
                }
            }
        })

        return lineErrors
    }

    fun Document.lineNumber(element: PsiElement): Int? {
        return if (element.textOffset< this.textLength) this.getLineNumber(element.textOffset) else null
    }

}
