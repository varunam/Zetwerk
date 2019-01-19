package com.zetwerk.app.zetwerk.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.data.model.Employee;
import com.zetwerk.app.zetwerk.views.activities.AddEmployeeActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.zetwerk.app.zetwerk.apputils.Constants.EMPLOYEE_OBJECT_KEY;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {
    
    private ArrayList<Employee> employeesList;
    private EmployeeCardInteractionCallbacks employeeCardInteractionCallbacks;
    private int oldClickedPosition = 0;
    private int lastClickedPosition = 0;
    
    public EmployeeListAdapter(@NonNull EmployeeCardInteractionCallbacks employeeCardInteractionCallbacks) {
        this.employeeCardInteractionCallbacks = employeeCardInteractionCallbacks;
    }
    
    public void setEmployeesList(@NonNull ArrayList<Employee> employeesList) {
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
        Context context = holder.employeeCard.getContext();
        
        String salary = "Salary: " + employee.getSalary();
        holder.employeeSalaryTextView.setText(salary);
        
        String dob = "Date of Birth: " + employee.getDob();
        holder.employeeDobTextView.setText(dob);
        
        holder.employeeNameTextView.setText(employee.getName());
        
        holder.employeeCard.setOnClickListener(view -> {
            oldClickedPosition = lastClickedPosition;
            lastClickedPosition = position;
            notifyItemChanged(oldClickedPosition);
            notifyItemChanged(lastClickedPosition);
            employeeCardInteractionCallbacks.onEmployeeCardClicked(employee);
        });
        
        holder.viewProfile.setOnClickListener(view -> {
        
        });
        
        holder.editProfile.setOnClickListener(view -> {
            Intent intent = new Intent(context, AddEmployeeActivity.class);
            intent.putExtra(EMPLOYEE_OBJECT_KEY, employee);
            context.startActivity(intent);
        });
        
        if (position == lastClickedPosition) {
            holder.actinsLayout.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.actinsLayout.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }
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
        private LinearLayout actinsLayout;
        private View divider;
        private Button viewProfile;
        private Button editProfile;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            employeeCard = itemView.findViewById(R.id.employee_layout_card_id);
            employeeNameTextView = itemView.findViewById(R.id.employee_layout_name_id);
            employeeDobTextView = itemView.findViewById(R.id.employee_layout_dob_id);
            actinsLayout = itemView.findViewById(R.id.employee_layout_actions_id);
            divider = itemView.findViewById(R.id.employee_layout_divider_id);
            employeeSalaryTextView = itemView.findViewById(R.id.employee_layout_salary_id);
            viewProfile = itemView.findViewById(R.id.employee_layout_view_profile_id);
            editProfile = itemView.findViewById(R.id.employee_layout_edit_profile_id);
        }
    }
}
