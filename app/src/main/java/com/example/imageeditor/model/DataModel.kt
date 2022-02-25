package com.example.imageeditor.model

import android.net.Uri
import com.example.imageeditor.EditorTools

data class EditedImage(
    val uri:Uri,
    val editorTool: EditorTools
)