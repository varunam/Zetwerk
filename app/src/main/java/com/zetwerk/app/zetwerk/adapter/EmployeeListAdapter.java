package com.zetwerk.app.zetwerk.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {
    
    private ArrayList<Employee> employeesList;
    
    public EmployeeListAdapter() {
    
    }
    
    public void setEmployeesList(ArrayList<Employee> employeesList) {
        this.employeesList = employeesList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    
    }
    
    @Override
    public int getItemCount() {
        if (employeesList == null)
            return 0;
        else
            employeesList.size()
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
