package com.zetwerk.app.zetwerk.data.firebase;

import com.zetwerk.app.zetwerk.data.model.Employee;

/**
 * Created by varun.am on 19/01/19
 */
public interface EmployeeDeletedCallbacks {
    public void onEmployeeDeleteSuccessful(Employee employee);
    public void onEmployeeDeleteFailure(Employee employee, String errorMessage);
}
