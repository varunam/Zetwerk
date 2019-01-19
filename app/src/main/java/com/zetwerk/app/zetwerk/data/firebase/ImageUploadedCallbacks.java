package com.zetwerk.app.zetwerk.data.firebase;

import android.net.Uri;

import com.zetwerk.app.zetwerk.data.model.Employee;

/**
 * Created by varun.am on 19/01/19
 */
public interface ImageUploadedCallbacks {
    public void onImageUploadSuccessful(Employee employee, Uri imageUri);
    public void onImageUploadFailure(Employee employee, String errorMessage);
}
