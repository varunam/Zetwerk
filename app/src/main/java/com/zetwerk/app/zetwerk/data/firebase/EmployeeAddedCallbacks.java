package com.zetwerk.app.zetwerk.data.firebase;

import com.zetwerk.app.zetwerk.data.model.Employee;

/**
 * Created by varun.am on 19/01/19
 */
public interface EmployeeAddedCallbacks {
    public void onEmployeeAdded(Employee employee);
    public void onEmployeeAddFailure(Employee employee, String errorMessage);
    public void onEmployeeUpdated(Employee employee);
    public void onEmployeeUpdateFailure(Employee employee, String errorMessage);
}

