package com.bebetterprogrammer.trecox

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tbruyelle.rxpermissions2.RxPermissions

class GetPermission {
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
     fun checkPermissionForCamera(context: Context, listener: PermissionListener) {
        val rxPermissions = RxPermissions(context as FragmentActivity)
        rxPermissions.requestEach(Manifest.permission.CAMERA)
            .subscribe { permission ->
                when {
                    permission.granted -> {
                        listener.onPermissionGranted()
                    }
                    else -> {
                        MaterialAlertDialogBuilder(context)
                            .setTitle(context.resources.getString(R.string.title_permission_dialog))
                            .setMessage(context.resources.getString(R.string.message_permission_dialog_camera))
                            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, which ->
                                (context as AppCompatActivity).openApplicationSettings()
                            }
                            .show()
                    }
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("CheckResult")
    fun checkPermissionForGallery(context: Context, listener: PermissionListener) {
        val rxPermissions = RxPermissions(context as FragmentActivity)
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe { permission ->
                when {
                    permission.granted -> {
                        listener.onPermissionGranted()
                    }
                    else -> {
                        MaterialAlertDialogBuilder(context)
                            .setTitle(context.resources.getString(R.string.title_permission_dialog))
                            .setMessage(context.resources.getString(R.string.message_permission_dialog_gallery))
                            .setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton(context.resources.getString(R.string.ok)) { dialog, which ->
                                (context as AppCompatActivity).openApplicationSettings()
                            }
                            .show()
                    }
                }
            }
    }

    fun AppCompatActivity.openApplicationSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.putExtra("app_package", packageName)
        intent.putExtra("app_uid", applicationInfo.uid)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
        finish()
    }
}