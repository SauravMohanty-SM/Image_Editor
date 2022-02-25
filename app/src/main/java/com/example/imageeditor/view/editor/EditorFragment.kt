package com.example.imageeditor.view.editor

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.example.imageeditor.*
import com.example.imageeditor.databinding.FragmentEditBinding
import com.example.imageeditor.model.EditedImage
import com.example.imageeditor.view.ImageSaveDialogFragment
import com.example.imageeditor.view.MainActivity
import com.example.imageeditor.viewModel.ImageViewModel
import com.mukesh.image_processing.ImageProcessor

class EditorFragment : Fragment(),
    ImageSaveDialogFragment.SaveDialogListener {
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var model: ImageViewModel
    private lateinit var processor: ImageProcessor
    private val TAG = "Editor Activity"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProvider(requireActivity()).get(ImageViewModel::class.java)
        setupObserver()
        setupUI()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(view.context, MainActivity::class.java))
            }
        })
    }

    private fun setupUI() {
        processor = ImageProcessor()

        Glide.with(this).load(model.getLast().uri).into(binding.imageViewEditor)

        binding.btnCrop.setOnClickListener {
            cropImage.launch(
                options(uri = model.getLast().uri as Uri) {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                    setAllowFlipping(false)
                    setAllowRotation(false)
                }
            )
        }

        binding.btnUndo.setOnClickListener {
            model.removeLast()
        }

        binding.btnRotateLeft.setOnClickListener {
            val item: EditedImage = model.getLast()
            val rotatedImage = processor.rotate(item.uri.convertToBitmap(requireActivity()), -90F)
            if (item.editorTool == EditorTools.Rotate) {
                model.replace(
                    EditedImage(
                        rotatedImage.convertToUri(requireActivity()),
                        EditorTools.Rotate
                    )
                )
            } else {
                model.add(
                    EditedImage(
                        rotatedImage.convertToUri(requireActivity()),
                        EditorTools.Rotate
                    )
                )
            }
        }

        binding.btnRotateRight.setOnClickListener {
            val item: EditedImage = model.getLast()
            val rotatedImage = processor.rotate(item.uri.convertToBitmap(requireActivity()), 90F)
            if (item.editorTool == EditorTools.Rotate) {
                model.replace(
                    EditedImage(
                        rotatedImage.convertToUri(requireActivity()),
                        EditorTools.Rotate
                    )
                )
            } else {
                model.add(
                    EditedImage(
                        rotatedImage.convertToUri(requireActivity()),
                        EditorTools.Rotate
                    )
                )
            }
        }

        //TODO: Save Image Here
        binding.btnFilter.setOnClickListener {
            if(!model.isSaved){
                val dialog = ImageSaveDialogFragment(this@EditorFragment)
                dialog.show(requireActivity().supportFragmentManager, "Image Save Dialog")
            }else{
                Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).popBackStack()
            }
        }

    }

    private fun setupObserver() {
        model.imageListLiveData.observe(viewLifecycleOwner) {
            Glide.with(this).load(it.last().uri).into(binding.imageViewEditor)
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        try {
            if (result.isSuccessful) {
                val uri = result.uriContent!!
                model.add(EditedImage(uri, EditorTools.Crop))
            } else {
                val throwable = result.error
                Log.d(TAG, throwable.toString())
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override fun onDestroy() {
        model.clear()
        super.onDestroy()
    }

    override fun onPositiveClick(dialog: DialogFragment) {
        val bitmap = model.getLast().uri.convertToBitmap(requireContext())
        saveImage(requireActivity(), bitmap)
        model.save()
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack()
    }

    override fun onCancelClick(dialog: DialogFragment) {
        model.isSaved = false
    }
}