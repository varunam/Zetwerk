package com.zetwerk.app.zetwerk.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by varun.am on 19/01/19
 */
public class Employee implements Parcelable {
    
    public static final String EMP_ID_BASE = "ZET0";
    public static final String DEFAULT_EMP_ID = EMP_ID_BASE + "1";
    private String name;
    private long salary;
    private String dob;
    private ArrayList<String> skills;
    private String id;
    
    public Employee(){}
    
    public Employee(String name, long salary, String dob, ArrayList<String> skills, String employeeId) {
        this.name = name;
        this.salary = salary;
        this.dob = dob;
        this.skills = skills;
        this.id = employeeId;
    }
    
    protected Employee(Parcel in) {
        name = in.readString();
        salary = in.readLong();
        dob = in.readString();
        skills = in.createStringArrayList();
        id = in.readString();
    }
    
    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }
        
        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public long getSalary() {
        return salary;
    }
    
    public void setSalary(long salary) {
        this.salary = salary;
    }
    
    public String getDob() {
        return dob;
    }
    
    public void setDob(String dob) {
        this.dob = dob;
    }
    
    public ArrayList<String> getSkills() {
        return skills;
    }
    
    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }
    
    public String getEmployeeId() {
        return id;
    }
    
    public void setEmployeeId(String employeeId) {
        this.id = employeeId;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel parcel, int i) {
    
        parcel.writeString(name);
        parcel.writeLong(salary);
        parcel.writeString(dob);
        parcel.writeStringList(skills);
        parcel.writeString(id);
    }
}
