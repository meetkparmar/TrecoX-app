package com.bebetterprogrammer.trecox.ui

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
import androidx.core.net.toUri
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

    private val ONE_MEGABYTE: Long = 1024 * 1024
    private var category_type = arrayOf("Stationery", "Watch")
    private val permission = GetPermission()
    var imageUri: Uri? = null
    var photo: Bitmap? = null
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var name: String? = null
    var imageurl: String? = null
    var email: String? = null
    var address: String? = null
    var pinCode: String? = null
    var shop: String? = null
    var category: String? = null
    var mobileNumber: String? = null
    private var progressDialog: ProgressDialog? = null
    private lateinit var localRepository : LocalRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        localRepository = LocalRepository(requireContext())

        if (!localRepository.isFirstLaunch()) {
            name = localRepository.getName()
            email = localRepository.getEmail()
            mobileNumber = localRepository.getMobileNumber()
            address = localRepository.getAddress()
            pinCode = localRepository.getPinCode()
            shop = localRepository.getShop()
            imageUri = localRepository.getImageUri()?.toUri()
            category = localRepository.getCategory()
            updateUI()

        }

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
            when {
                et_name.text.isNullOrEmpty() -> et_name.error = "Required Field"
                et_email.text.isNullOrEmpty() -> et_email.error = "Required Field"
                et_address.text.isNullOrEmpty() -> et_address.error = "Required Field"
                et_shop.text.isNullOrEmpty() -> et_shop.error = "Required Field"
                et_pin_code.text.isNullOrEmpty() -> et_pin_code.error = "Required Field"
                et_mobile_number.text.isNullOrEmpty() -> et_mobile_number.error = "Required Field"
                et_wholesalers_category.text.isNullOrEmpty() -> et_wholesalers_category.error = "Required Field"
                imageUri == null -> Toast.makeText(context, "Please Select Photo", Toast.LENGTH_SHORT).show()
                else -> {
                    progressDialog = showLoadingDialog(requireActivity(), "Adding User Information")
                    addUserInfo()
                }
            }

        }

        btn_edit.setOnClickListener {
            edit_user_detail_layout.visibility = View.VISIBLE
            user_detail_layout.visibility = View.GONE
            iv_image.visibility = View.VISIBLE
            image_layout.visibility = View.GONE

            et_name.setText(name)
            et_email.setText(email)
            et_mobile_number.setText(mobileNumber)
            et_address.setText(address)
            et_wholesalers_category.setText(category)
            et_pin_code.setText(pinCode)
            et_shop.setText(shop)
            Picasso.with(context).load(imageUri).into(iv_image)
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
        startActivityForResult(cameraIntent,
            REQUEST_IMAGE_FROM_CAMERA
        )
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent,
            REQUEST_IMAGE_FROM_GALLERY
        )
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
        email = et_email.text.toString()
        address = et_address.text.toString()
        pinCode = et_pin_code.text.toString()
        shop = et_shop.text.toString()
        category = et_wholesalers_category.text.toString()
        mobileNumber = et_mobile_number.text.toString()

        localRepository.setCategory(category.toString())

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
                map["email"] = email!!
                map["address"] = address!!
                map["pinCode"] = pinCode!!
                map["shop"] = shop!!
                map["category"] = category!!
                map["mobileNumber"] = mobileNumber!!
                imageurl?.let {
                    map["profileUrl"] = imageurl!!
                }
                FirebaseDatabase.getInstance().reference.child("category_wholesalers")
                    .child(category!!)
                    .child(name!!)
                    .setValue(map)
                    .addOnSuccessListener {
                        localRepository.isFirstLaunch(false)
                        localRepository.setName(name!!)
                        localRepository.setEmail(email!!)
                        localRepository.setAddress(address!!)
                        localRepository.setPinCode(pinCode.toString())
                        localRepository.setMobileNumber(mobileNumber!!)
                        localRepository.setShop(shop!!)
                        localRepository.setImageUri(imageUri.toString())
                        dismissLoadingDialog(progressDialog)
                        updateUI()
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

    private fun updateUI() {
        edit_user_detail_layout.visibility = View.GONE
        user_detail_layout.visibility = View.VISIBLE
        tv_name.text = name
        tv_mobile_number.text = mobileNumber
        tv_email.text = email
        tv_address.text = address
        tv_category.text = category
        tv_pin_code.text = pinCode
        tv_category.text = category
        Picasso.with(context).load(imageUri).into(iv_user_image)
    }

    companion object {
        const val REQUEST_IMAGE_FROM_CAMERA = 1102
        const val REQUEST_IMAGE_FROM_GALLERY = 1103
    }
}