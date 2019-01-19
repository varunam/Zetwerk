package com.zetwerk.app.zetwerk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee_card, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeesList.get(position);
        
        String salary = "Salary: " + employee.getSalary();
        holder.employeeSalaryTextView.setText(salary);
        
        String dob = "Date of Birth: " + employee.getDob();
        holder.employeeDobTextView.setText(dob);
        
        holder.employeeNameTextView.setText(employee.getName());
    }
    
    @Override
    public int getItemCount() {
        if (employeesList == null)
            return 0;
        else
            return employeesList.size();
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder {
        
        private TextView employeeNameTextView, employeeDobTextView, employeeSalaryTextView;
        private CardView employeeCard;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            employeeCard = itemView.findViewById(R.id.employee_layout_card_id);
            employeeNameTextView = itemView.findViewById(R.id.employee_layout_name_id);
            employeeDobTextView = itemView.findViewById(R.id.employee_layout_dob_id);
            employeeSalaryTextView = itemView.findViewById(R.id.employee_layout_salary_id);
            
        }
    }
}
