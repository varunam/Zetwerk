package com.zetwerk.app.zetwerk.views.activities;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeAddedCallbacks;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeDatabase;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

import static com.zetwerk.app.zetwerk.apputils.Constants.EMPLOYEE_OBJECT_KEY;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, EmployeeAddedCallbacks {
    
    private TextInputEditText employeeName, employeeSalary, employeeDob, employeeId;
    private String[] skills;
    private ImageView profileImage, cameraIcon;
    private Button createButton, addSkillsButton;
    private LinearLayout skillsLayout;
    private ProgressDialog progressDialog;
    private CheckBox skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8, skill9;
    private CheckBox[] skillsCheckboxes;
    
    private boolean skillsLayoutShowing = false;
    
    private EmployeeDatabase employeeDatabase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        
        init();
        if (getIntent().hasExtra(EMPLOYEE_OBJECT_KEY)) {
            Employee employee = getIntent().getParcelableExtra(EMPLOYEE_OBJECT_KEY);
            displayEmployeeProfile(employee);
            createButton.setText(getResources().getString(R.string.update));
        }
    }
    
    private void displayEmployeeProfile(Employee employee) {
        employeeName.setText(employee.getName());
        employeeDob.setText(employee.getDob());
        employeeId.setText(employee.getEmployeeId());
        employeeSalary.setText(String.valueOf(employee.getSalary()));
        showSkillsOf(employee);
    }
    
    private void showSkillsOf(Employee employee) {
        showSkillsLayout();
        ArrayList<String> skills = employee.getSkills();
        for (CheckBox checkBox : skillsCheckboxes) {
            if (skills.contains(checkBox.getText()))
                checkBox.setChecked(true);
        }
    }
    
    private void init() {
        employeeDatabase = new EmployeeDatabase();
        employeeName = findViewById(R.id.profile_name_id);
        employeeSalary = findViewById(R.id.profile_salary_id);
        
        employeeDob = findViewById(R.id.profile_dob_id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            employeeDob.setShowSoftInputOnFocus(false);
        }
        employeeDob.setOnClickListener(this);
        employeeDob.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showDatepicker();
            }
        });
        
        createButton = findViewById(R.id.profile_create_button_id);
        createButton.setOnClickListener(this);
        addSkillsButton = findViewById(R.id.profile_add_skills_button_id);
        addSkillsButton.setOnClickListener(this);
        profileImage = findViewById(R.id.profile_picture_id);
        cameraIcon = findViewById(R.id.profile_camera_id);
        cameraIcon.setOnClickListener(this);
        skillsLayout = findViewById(R.id.profile_skills_layout_id);
        employeeId = findViewById(R.id.profile_employee_id_id);
        progressDialog = new ProgressDialog(this);
        
        skill1 = findViewById(R.id.profile_skill1_id);
        skill2 = findViewById(R.id.profile_skill2_id);
        skill3 = findViewById(R.id.profile_skill3_id);
        skill4 = findViewById(R.id.profile_skill4_id);
        skill5 = findViewById(R.id.profile_skill5_id);
        skill6 = findViewById(R.id.profile_skill6_id);
        skill7 = findViewById(R.id.profile_skill7_id);
        skill8 = findViewById(R.id.profile_skill8_id);
        skill9 = findViewById(R.id.profile_skill9_id);
        skillsCheckboxes = new CheckBox[]{skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8, skill9};
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.profile_add_skills_button_id:
                if (!skillsLayoutShowing)
                    showSkillsLayout();
                break;
            case R.id.profile_create_button_id:
                createProfile();
                break;
            case R.id.profile_camera_id:
                break;
            case R.id.profile_dob_id:
                showDatepicker();
                break;
            default:
                break;
        }
    }
    
    private void showDatepicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );
        dpd.setAccentColor(getResources().getColor(R.color.colorPrimary));
        dpd.show(getSupportFragmentManager(), "Datepickerdialog");
    }
    
    private void createProfile() {
        if (TextUtils.isEmpty(employeeName.getText())) {
            setError(employeeName, "Required");
        } else if (TextUtils.isEmpty(employeeDob.getText())) {
            setError(employeeDob, "Required");
        } else if (TextUtils.isEmpty(employeeSalary.getText())) {
            setError(employeeSalary, "Required");
        } else if (!skillsAdded()) {
            toast("Please add skills to continue");
        } else if (profileImageNotUploaded()) {
            toast("Please upload your profile picture");
        } else {
            Employee employee = new Employee(
                    employeeName.getText().toString().toUpperCase().trim(),
                    Long.parseLong(employeeSalary.getText().toString()),
                    employeeDob.getText().toString().trim(),
                    getSelectedSkills(),
                    employeeId.getText().toString().trim()
            );
            
            if (createButton.getText().toString().equals(getResources().getString(R.string.update))) {
                showLoader("Adding employee...");
                employeeDatabase.updateEmployee(employee, this);
            } else {
                showLoader("Updting employee...");
                employeeDatabase.addEmployee(employee, this);
            }
        }
    }
    
    private ArrayList<String> getSelectedSkills() {
        ArrayList<String> skillSet = new ArrayList<>();
        if (skill1.isChecked())
            skillSet.add(skill1.getText().toString());
        if (skill2.isChecked())
            skillSet.add(skill2.getText().toString());
        if (skill3.isChecked())
            skillSet.add(skill3.getText().toString());
        if (skill4.isChecked())
            skillSet.add(skill4.getText().toString());
        if (skill5.isChecked())
            skillSet.add(skill5.getText().toString());
        if (skill6.isChecked())
            skillSet.add(skill6.getText().toString());
        if (skill7.isChecked())
            skillSet.add(skill7.getText().toString());
        if (skill8.isChecked())
            skillSet.add(skill8.getText().toString());
        if (skill9.isChecked())
            skillSet.add(skill9.getText().toString());
        return skillSet;
    }
    
    private boolean profileImageNotUploaded() {
        return false;
    }
    
    private boolean skillsAdded() {
        return skill1.isChecked() ||
                skill2.isChecked() ||
                skill3.isChecked() ||
                skill4.isChecked() ||
                skill5.isChecked() ||
                skill6.isChecked() ||
                skill7.isChecked() ||
                skill8.isChecked() ||
                skill9.isChecked();
    }
    
    private void setError(TextInputEditText editText, String error) {
        editText.setError(error);
        editText.requestFocus();
    }
    
    private void showSkillsLayout() {
        skillsLayoutShowing = true;
        skillsLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.from_right);
        skillsLayout.startAnimation(animation);
        addSkillsButton.setText("Choose skills");
    }
    
    public void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String dateSet = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        employeeDob.setText(dateSet);
    }
    
    public void showLoader(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    
    public void hideLoader() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    
    @Override
    public void onEmployeeAdded(Employee employee) {
        hideLoader();
        toast("Employee added: " + employee.getName());
        onBackPressed();
    }
    
    @Override
    public void onEmployeeAddFailure(Employee employee, String errorMessage) {
        hideLoader();
        toast("Please try again\n" + errorMessage);
    }
    
    @Override
    public void onEmployeeUpdated(Employee employee) {
        hideLoader();
        toast("Employee updated: " + employee.getName());
        onBackPressed();
    }
    
    @Override
    public void onEmployeeUpdateFailure(Employee employee, String errorMessage) {
        hideLoader();
        toast("Please try again\n" + errorMessage);
    }
}
