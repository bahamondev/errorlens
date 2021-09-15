package com.bahamondev.errorlens

import com.intellij.openapi.command.undo.DocumentReferenceManager
import com.intellij.openapi.editor.EditorLinePainter
import com.intellij.openapi.editor.LineExtensionInfo
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.*
import com.intellij.ui.SimpleTextAttributes
import java.util.*
import kotlin.collections.ArrayList


internal class ErrorLinePainter : EditorLinePainter() {
    override fun getLineExtensions(
        project: Project,
        file: VirtualFile,
        lineNumber: Int
    ): MutableCollection<LineExtensionInfo>? {
        val attributes = TextAttributes(
            SimpleTextAttributes.REGULAR_ATTRIBUTES.fgColor,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.bgColor,
            null,
            null,
            SimpleTextAttributes.REGULAR_ATTRIBUTES.fontStyle
        )

        val psiFile = PsiManager.getInstance(project).findFile(file)
        val document = psiFile?.let { PsiDocumentManager.getInstance(project).getDocument(it) }

        if (psiFile == null) {
            return Collections.singletonList(LineExtensionInfo(" ERROR NO FILE", attributes))
        } else if (document == null) {
            return Collections.singletonList(LineExtensionInfo(" ERROR NO DOCUMENT", attributes))
        }
        val lineErrors = mutableListOf<PsiErrorElement>()
        psiFile.accept(object : PsiRecursiveElementWalkingVisitor() {
            override fun elementFinished(element: PsiElement?) {
                if (element is PsiErrorElement) {
                    if (lineNumber == document.getLineNumber(element.textOffset)) {
                        lineErrors.add(element)
                    }
                }
            }
        })

        if (lineErrors.isEmpty()) {
            return Collections.singletonList(LineExtensionInfo(" NO ERRORS", attributes))
        }

        return Collections.singletonList(LineExtensionInfo(lineErrors.first().errorDescription, attributes))
    }
}