package com.example.imageeditor.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException

class ImageSaveDialogFragment(private var listener : SaveDialogListener) : DialogFragment() {


    interface SaveDialogListener {
        fun onPositiveClick(dialog: DialogFragment)
        fun onCancelClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Save Image")
            builder.setMessage("Do you want to save Image?")
                .setPositiveButton("Save") { dialog, id ->
                    listener.onPositiveClick(this)
                }
                .setNegativeButton("Don't Save") { dialog, id ->
                    listener.onCancelClick(this)
                }
            builder.create()
        } ?: throw IllegalStateException("Cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SaveDialogListener
        } catch (e: ClassCastException) {

        }
    }
}