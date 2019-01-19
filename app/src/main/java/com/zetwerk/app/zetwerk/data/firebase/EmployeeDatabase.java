package com.zetwerk.app.zetwerk.data.firebase;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.EMPLOYEES;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.LAST_EMPLOYEE;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.PHOTOS;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.ZETWERK;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeDatabase {
    
    private static final String TAG = EmployeeDatabase.class.getSimpleName();
    private EmployeesLoadedCallbacks employeesLoadedCallbacks;
    
    public void loadEmployeeRecords(@NonNull EmployeesLoadedCallbacks employeesLoadedCallbacks) {
        this.employeesLoadedCallbacks = employeesLoadedCallbacks;
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(EMPLOYEES)
                .addValueEventListener(loadEmployeesValueEventListeners);
        
    }
    
    private ValueEventListener loadEmployeesValueEventListeners = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Employee> employees = new ArrayList<>();
            for (DataSnapshot employeeDataSnapshot : dataSnapshot.getChildren()) {
                Employee employee = employeeDataSnapshot.getValue(Employee.class);
                if (employee != null) {
                    employees.add(employee);
                    Log.d(TAG, "Added employee: " + employee.getName());
                } else {
                    Log.e(TAG, "Employee received null: " + dataSnapshot.getValue());
                }
            }
            if (employeesLoadedCallbacks != null)
                employeesLoadedCallbacks.onEmployeeRecordsLoaded(employees);
        }
        
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        
        }
    };
    
    public void addEmployee(Employee employee, EmployeeAddedCallbacks employeeAddedCallbacks) {
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(EMPLOYEES)
                .child(employee.getEmployeeId())
                .setValue(employee)
                .addOnSuccessListener(aVoid -> {
                    if (employeeAddedCallbacks != null)
                        employeeAddedCallbacks.onEmployeeAdded(employee);
                    Log.d(TAG, "Employee added successfully: " + employee.getName());
                })
                .addOnFailureListener(e -> {
                    if (employeeAddedCallbacks != null)
                        employeeAddedCallbacks.onEmployeeAddFailure(employee, e.getMessage());
                });
        updateLastEmployee(employee);
    }
    
    private void updateLastEmployee(Employee employee) {
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(LAST_EMPLOYEE)
                .setValue(Integer.parseInt(employee.getEmployeeId().replaceAll(Employee.EMP_ID_BASE, "")));
    }
    
    public void uploadProfileImage(Uri imageUri, Employee employee, ImageUploadedCallbacks imageUploadedCallbacks) {
        StorageReference filePath = FirebaseStorage.getInstance().getReference().child(PHOTOS).child(employee.getEmployeeId());
        if (imageUri != null) {
            filePath.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        UserProfileChangeRequest userProfile = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(imageUri)
                                .build();
                        if (FirebaseAuth.getInstance().getCurrentUser() != null)
                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfile);
                        imageUploadedCallbacks.onImageUploadSuccessful(employee, imageUri);
                    })
                    .addOnFailureListener(e -> {
                        imageUploadedCallbacks.onImageUploadFailure(employee, e.getMessage());
                    });
        } else {
            imageUploadedCallbacks.onImageUploadSuccessful(employee, null);
        }
    }
    
    public void updateEmployee(Employee employee, EmployeeAddedCallbacks employeeAddedCallbacks) {
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(EMPLOYEES)
                .child(employee.getEmployeeId())
                .setValue(employee)
                .addOnSuccessListener(aVoid -> {
                    if (employeeAddedCallbacks != null)
                        employeeAddedCallbacks.onEmployeeUpdated(employee);
                    Log.d(TAG, "Employee updated successfully: " + employee.getName());
                })
                .addOnFailureListener(e -> {
                    if (employeeAddedCallbacks != null)
                        employeeAddedCallbacks.onEmployeeUpdateFailure(employee, e.getMessage());
                });
    }
    
    public void deleteEmployee(Employee employee, @NonNull EmployeeDeletedCallbacks employeeDeletedCallbacks) {
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(EMPLOYEES)
                .child(employee.getEmployeeId())
                .removeValue()
                .addOnSuccessListener(aVoid -> employeeDeletedCallbacks.onEmployeeDeleteSuccessful(employee))
                .addOnFailureListener(e -> employeeDeletedCallbacks.onEmployeeDeleteFailure(employee, e.getMessage()));
    }
}
