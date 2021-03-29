package com.bebetterprogrammer.trecox.ui.userInfo

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bebetterprogrammer.trecox.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.bottom_sheet_upload_photo.*
import kotlinx.android.synthetic.main.fragment_user_info.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.HashMap


class UserInfoFragment : Fragment() {

    var category_type = arrayOf("Stationery", "Watch")
    private val permission = GetPermission()
    var imageUri: Uri? = null
    var photo: Bitmap? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var name: String? = null
    var imageurl: String? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = context?.let { ArrayAdapter(it, R.layout.list_item, category_type) }
        et_wholesalers_category?.setAdapter(adapter)

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        image_layout.setOnClickListener {
            uploadPhoto()
        }

        iv_image.setOnClickListener {
            uploadPhoto()
        }
        btn_add_info.setOnClickListener {
            progressDialog = showLoadingDialog(requireActivity(), "Adding User Information")
            addUserInfo()
        }
    }

    private fun uploadPhoto() {
        val dialog = context?.let { BottomSheetDialog(it) }
        val view = layoutInflater.inflate(R.layout.bottom_sheet_upload_photo, null)
        dialog?.setContentView(view)
        dialog?.show()

        dialog?.upload_photo_camera_layout?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context?.let { it1 ->
                    permission.checkPermissionForCamera(it1, object : PermissionListener {
                        override fun onPermissionGranted() {
                            openCamera()
                        }

                    })
                }
            } else {
                openCamera()
            }
            dialog.dismiss()
        }

        dialog?.upload_photo_gallery_layout?.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context?.let { it1 ->
                    permission.checkPermissionForGallery(it1, object : PermissionListener {
                        override fun onPermissionGranted() {
                            openGallery()
                        }

                    })
                }
            } else {
                openGallery()
            }
            dialog.dismiss()
        }
    }


    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, Companion.REQUEST_IMAGE_FROM_CAMERA)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, Companion.REQUEST_IMAGE_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_IMAGE_FROM_GALLERY) {
            imageUri = data?.data
            photo = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
            showimage(imageUri)
        }
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_IMAGE_FROM_CAMERA) {
            photo = data?.extras?.get("data") as Bitmap
            imageUri = context?.let { getImageUri(it, photo!!) }
            showimage(imageUri)
        }
    }

    private fun showimage(uri: Uri?) {
        image_layout.visibility = View.GONE
        iv_image.visibility = View.VISIBLE
        Picasso.with(context).load(uri).into(iv_image)
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            inImage,
            Calendar.getInstance().time.toString(),
            null
        )
        return Uri.parse(path)
    }

    private fun addUserInfo() {
        name = et_name.text.toString()
        val email = et_email.text.toString()
        val address = et_address.text.toString()
        val pinCode = et_pin_code.text.toString()
        val shop = et_shop.text.toString()
        val category = et_wholesalers_category.text.toString()
        val mobileNumber = et_mobile_number.text.toString()

        val riversRef = storageReference?.child("wholesaler_image/${name}")
        val uploadTask = riversRef?.putFile(imageUri!!)

        uploadTask?.addOnFailureListener {
            dismissLoadingDialog(progressDialog)
            showErrorToast(requireContext(), R.string.something_wrong_error)
        }?.addOnSuccessListener {
            storageReference?.child("wholesaler_image/${name}")?.downloadUrl?.addOnSuccessListener {
                imageurl = storageReference?.child("wholesaler_image/${name}")?.path

                val map = HashMap<String, Any>()
                map["name"] = name!!
                map["email"] = email
                map["address"] = address
                map["pinCode"] = pinCode
                map["shop"] = shop
                map["category"] = category
                map["mobileNumber"] = mobileNumber
                imageurl?.let {
                    map["profileUrl"] = imageurl!!
                }
                FirebaseDatabase.getInstance().reference.child("category_wholesalers")
                    .child(category).push()
                    .setValue(map)
                    .addOnSuccessListener {
                        dismissLoadingDialog(progressDialog)
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {

                        dismissLoadingDialog(progressDialog)
                        showErrorToast(requireContext(), R.string.something_wrong_error)
                    }

            }?.addOnFailureListener {

                dismissLoadingDialog(progressDialog)
                showErrorToast(requireContext(), R.string.something_wrong_error)
            }
        }
    }

    companion object {
        const val REQUEST_IMAGE_FROM_CAMERA = 1102
        const val REQUEST_IMAGE_FROM_GALLERY = 1103
    }
}