package com.bebetterprogrammer.trecox

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun showLoadingDialog(activity: Activity, message: String): ProgressDialog {
    return ProgressDialog.show(
        activity, "",
        message, true
    )
}

fun dismissLoadingDialog(dialog: ProgressDialog?) {
    dialog?.dismiss()
}

fun showErrorToast(context: Context, @StringRes stringResource: Int) {
    Toast.makeText(
        context,
        context.getString(stringResource),
        Toast.LENGTH_LONG
    ).show()
}