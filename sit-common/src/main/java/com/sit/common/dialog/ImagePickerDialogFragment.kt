package com.sit.common.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.sit.common.BuildConfig
import com.sit.common.R
import com.sit.common.databinding.FragmentImagePickerDialogBinding
import com.sit.common.ext.copyUriToFile
import com.sit.common.ext.hideShowViews
import com.sit.common.interfaces.OnItemSelected
import com.sit.common.utils.PrintLog.printMsg
import java.io.File
import java.util.Date

class ImagePickerDialogFragment(
    private val isChooseFile: Boolean = false,
    private val fileSizeInMB: Int = 15
) : DialogFragment() {

    private lateinit var binding: FragmentImagePickerDialogBinding
    private var imgFile: File? = null
    private var imagePath: Uri? = null
    private lateinit var onItemSelected: OnItemSelected<Uri>
    private lateinit var mActivity: FragmentActivity

    fun onItemSelect(onItemSelected: OnItemSelected<Uri>): ImagePickerDialogFragment {
        this.onItemSelected = onItemSelected
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_image_picker_dialog, container, false
        )

        hideShowViews(isChooseFile, binding.txtChooseFile)

        //set background for dialog
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                mActivity, R.drawable.bg_corner_radius
            )
        )
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        onClick()
        return binding.root
    }

    private fun onClick() {

        binding.txtCamera.setOnClickListener {
            /*if (mActivity.cameraPermission()) {
                //Request Camera Permission From User
                requestCameraPermission.launch(requestCamera)
            } else {
                displayCamera()
            }*/
        }

        binding.txtGallery.setOnClickListener {
            /*if (mActivity.storagePermission()) {
                //Request Storage Permission From User
                requestStoragePermission.launch(requestStorage)
            } else {
                openGallery()
            }*/
        }

        binding.txtChooseFile.setOnClickListener {
            /*if (mActivity.storagePermission()) {
                //Request Storage Permission From User
                requestStoragePermission.launch(requestStorage)
            } else {
                openFilePicker()
            }*/
        }
    }

    /*private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // PERMISSION GRANTED
                displayCamera()
            } else {
                // PERMISSION NOT GRANTED
//                showSettingDialog(mActivity, mActivity.getString(R.string.camera_permission_desc))
            }
        }

    private val requestStoragePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // PERMISSION GRANTED
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryActivityResult.launch(pickPhoto)
            } else {
                // PERMISSION NOT GRANTED
//                showSettingDialog(mActivity, mActivity.getString(R.string.gallery_permission_desc))
            }
        }*/

    private fun displayCamera() {
        val destPath: String? = mActivity.getExternalFilesDir(null)?.absolutePath
        val imagesFolder = File(destPath, "File")
        try {
            imagesFolder.mkdirs()
            imgFile = File(imagesFolder, Date().time.toString() + ".png")
            imagePath = FileProvider.getUriForFile(
                mActivity, BuildConfig.LIBRARY_PACKAGE_NAME + ".fileProvider", imgFile!!
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePath)

            cameraActivityResult.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openGallery() {
        //Select Image From Gallery
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryActivityResult.launch(pickPhoto)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "image/*",
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                    "text/plain",
                    "application/rtf",
                    "application/tex",
                    "application/vnd.wordperfect"
                )
            )
        }
        fileActivityResult.launch(intent)
    }

    private var cameraActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //capture image from camera
            printMsg("camera")
            checkFileSize()
        }
    }

    private var galleryActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            //get image from gallery
            printMsg("gallery")
            imagePath = result.data?.data
            checkFileSize()
        }
    }

    private var fileActivityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            printMsg("file")
            imagePath = result.data?.data
            checkFileSize()
        }
    }

    private fun checkFileSize() {
        if (this::onItemSelected.isInitialized) {
            imgFile = imagePath?.let { copyUriToFile(mActivity, it) }
            if (imgFile != null && imgFile!!.length() <= (fileSizeInMB * 1024 * 1024)) {
                onItemSelected.onItemSelected(imagePath)
                dismiss()
            } else {
                imgFile = null
                imagePath = null
                dismiss()
                MessageDialog(
                    mActivity,
                    String.format(
                        "%s %s MB File",
                        mActivity.getString(R.string.please_select_less_than),
                        fileSizeInMB
                    )
                ).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as FragmentActivity
    }
}