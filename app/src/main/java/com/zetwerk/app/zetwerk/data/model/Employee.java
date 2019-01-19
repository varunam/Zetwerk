package com.zetwerk.app.zetwerk.data.model;

import java.util.ArrayList;

/**
 * Created by varun.am on 19/01/19
 */
public class Employee {
    
    public static final String EMP_ID_BASE = "ZET0";
    public static final String DEFAULT_EMP_ID = EMP_ID_BASE + "1";
    private String employeeName;
    private long employeeSalary;
    private String employeeDob;
    private ArrayList<String> skills;
    private String employeeId;
    
    public Employee(String employeeName, long employeeSalary, String employeeDob, ArrayList<String> skills, String employeeId) {
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeDob = employeeDob;
        this.skills = skills;
        this.employeeId = employeeId;
    }
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    
    public long getEmployeeSalary() {
        return employeeSalary;
    }
    
    public void setEmployeeSalary(long employeeSalary) {
        this.employeeSalary = employeeSalary;
    }
    
    public String getEmployeeDob() {
        return employeeDob;
    }
    
    public void setEmployeeDob(String employeeDob) {
        this.employeeDob = employeeDob;
    }
    
    public ArrayList<String> getSkills() {
        return skills;
    }
    
    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
}
