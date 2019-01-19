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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeCardInteractionCallbacks;
import com.zetwerk.app.zetwerk.data.model.Employee;
import com.zetwerk.app.zetwerk.views.activities.AddEmployeeActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import static com.zetwerk.app.zetwerk.apputils.Constants.EMPLOYEE_OBJECT_KEY;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.PHOTOS;

/**
 * Created by varun.am on 19/01/19
 */
public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> implements Filterable {
    
    private ArrayList<Employee> employeesList;
    private ArrayList<Employee> filteredEmployeeList;
    private int oldClickedPosition = 0;
    private int lastClickedPosition = 0;
    private EmployeeCardInteractionCallbacks employeeCardInteractionCallbacks;
    
    public EmployeeListAdapter(@NonNull EmployeeCardInteractionCallbacks employeeCardInteractionCallbacks) {
        this.employeeCardInteractionCallbacks = employeeCardInteractionCallbacks;
    }
    
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
        
        holder.employeeCard.setOnLongClickListener(view ->
                cardLongClicked(employee)
        );
        
        if (position == lastClickedPosition) {
            holder.actinsLayout.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
        } else {
            holder.actinsLayout.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }
    }
    
    private boolean cardLongClicked(Employee employee) {
        employeeCardInteractionCallbacks.onEmployeeCardLongClicked(employee);
        return true;
    }
    
    private void showEmployeeProfileDialog(Context context, Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_employee_profile, null, false);
        builder.setView(view);
        
        AlertDialog dialog = builder.create();
        
        ImageView profieImage = view.findViewById(R.id.dialog_employee_picture_id);
        androidx.swiperefreshlayout.widget.CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        
        FirebaseStorage.getInstance()
                .getReference()
                .child(PHOTOS)
                .child(employee.getEmployeeId())
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(context)
                            .load(uri)
                            .apply(new RequestOptions()
                                    .placeholder(circularProgressDrawable))
                            .into(profieImage);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Profile image download failure\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        
        
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
        
        ImageView closeDialog = view.findViewById(R.id.close_dialog_id);
        closeDialog.setOnClickListener(view1 -> dialog.dismiss());
        
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialogAnimation;
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
