package com.zetwerk.app.zetwerk.data.firebase;

import androidx.annotation.NonNull;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeDatabase {
    
    private EmployeesLoadedCallbacks employeesLoadedCallbacks;
    
    public EmployeeDatabase(@NonNull EmployeesLoadedCallbacks employeesLoadedCallbacks){
        this.employeesLoadedCallbacks = employeesLoadedCallbacks;
    }
    
    public void loadEmployeeRecords(){
    
    }

}
