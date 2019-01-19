package com.zetwerk.app.zetwerk.data.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;

import androidx.annotation.NonNull;

import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.EMPLOYEES;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeDatabase {
    
    private static final String TAG = EmployeeDatabase.class.getSimpleName();
    private EmployeesLoadedCallbacks employeesLoadedCallbacks;
    
    public void loadEmployeeRecords(@NonNull EmployeesLoadedCallbacks employeesLoadedCallbacks) {
        this.employeesLoadedCallbacks = employeesLoadedCallbacks;
        FirebaseDatabase.getInstance()
                .getReference(EMPLOYEES)
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
    
}
