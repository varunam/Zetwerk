package com.zetwerk.app.zetwerk.data.model;

/**
 * Created by varun.am on 19/01/19
 */
public class Employee {
    
    private String employeeName;
    private long employeeSalary;
    private String employeeDob;
    private String[] skills;
    private long employeeId;
    
    public Employee(String employeeName, long employeeSalary, String employeeDob, String[] skills, long employeeId) {
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
    
    public String[] getSkills() {
        return skills;
    }
    
    public void setSkills(String[] skills) {
        this.skills = skills;
    }
    
    public long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }
}
