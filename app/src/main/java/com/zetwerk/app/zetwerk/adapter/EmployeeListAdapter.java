package com.zetwerk.app.zetwerk.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
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
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> implements Filterable {
    
    private ArrayList<Employee> employeesList;
    private ArrayList<Employee> filteredEmployeeList;
    private int oldClickedPosition = 0;
    private int lastClickedPosition = 0;
    
    public void setEmployeesList(@NonNull ArrayList<Employee> employeesList) {
        this.employeesList = employeesList;
        this.filteredEmployeeList = employeesList;
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
        Employee employee = filteredEmployeeList.get(position);
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
        });
        
        holder.viewProfile.setOnClickListener(view -> {
            showEmployeeProfileDialog(context, employee);
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
    
    private void showEmployeeProfileDialog(Context context, Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_employee_profile, null, false);
        builder.setView(view);
        
        AlertDialog dialog = builder.create();
        
        TextView name = view.findViewById(R.id.dialog_employee_name_id);
        name.setText(employee.getName());
        
        TextView dob = view.findViewById(R.id.employee_dialog_dob_id);
        dob.setText(employee.getDob());
        
        TextView salary = view.findViewById(R.id.employee_dialog_salary_id);
        salary.setText(String.valueOf(employee.getSalary()));
        
        TextView skills = view.findViewById(R.id.employee_dialog_skills_id);
        
        int i = 0;
        for (i = 0; i < employee.getSkills().size() - 1; i++) {
            skills.append(employee.getSkills().get(i) + ", ");
        }
        skills.append(employee.getSkills().get(i));
        
        dialog.show();
    }
    
    @Override
    public int getItemCount() {
        if (filteredEmployeeList == null)
            return 0;
        else
            return filteredEmployeeList.size();
    }
    
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchedString = charSequence.toString().toLowerCase();
                
                ArrayList<Employee> filteredList = new ArrayList<>();
                for (Employee employee : employeesList) {
                    if (employee.getName().toLowerCase().contains(searchedString)) {
                        filteredList.add(employee);
                    }
                }
                
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }
            
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredEmployeeList = (ArrayList<Employee>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
