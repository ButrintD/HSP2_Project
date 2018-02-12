package com.hsp.realtimechatapplication;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by Butrint on 12/20/2017.
 */

public class ActivityUtil {

    public static void showErrorDialog(String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Oops");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok,null);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
