package com.zetwerk.app.zetwerk.data.firebase;

import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;

/**
 * Created by varun.am on 19/01/19
 */
public interface EmployeesLoadedCallbacks {
    public void onEmployeeRecordsLoaded(ArrayList<Employee> employees);
}
