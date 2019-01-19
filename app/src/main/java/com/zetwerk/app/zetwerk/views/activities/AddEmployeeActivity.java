package com.zetwerk.app.zetwerk.views.activities;

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
import com.zetwerk.app.zetwerk.R;

import androidx.appcompat.app.AppCompatActivity;

public class AddEmployeeActivity extends AppCompatActivity implements View.OnClickListener {
    
    private TextInputEditText employeeName, employeeSalary, employeeDob;
    private String[] skills;
    private ImageView profileImage, cameraIcon;
    private Button createButton, addSkillsButton;
    private LinearLayout skillsLayout;
    private boolean skillsLayoutShowing = false;
    
    private CheckBox skill1, skill2, skill3, skill4, skill5, skill6, skill7, skill8, skill9;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);
        
        init();
    }
    
    private void init() {
        employeeName = findViewById(R.id.profile_name_id);
        employeeSalary = findViewById(R.id.profile_salary_id);
        employeeDob = findViewById(R.id.profile_dob_id);
        createButton = findViewById(R.id.profile_create_button_id);
        createButton.setOnClickListener(this);
        addSkillsButton = findViewById(R.id.profile_add_skills_button_id);
        addSkillsButton.setOnClickListener(this);
        profileImage = findViewById(R.id.profile_picture_id);
        cameraIcon = findViewById(R.id.profile_camera_id);
        cameraIcon.setOnClickListener(this);
        skillsLayout = findViewById(R.id.profile_skills_layout_id);
        
        skill1 = findViewById(R.id.profile_skill1_id);
        skill2 = findViewById(R.id.profile_skill2_id);
        skill3 = findViewById(R.id.profile_skill3_id);
        skill4 = findViewById(R.id.profile_skill4_id);
        skill5 = findViewById(R.id.profile_skill5_id);
        skill6 = findViewById(R.id.profile_skill6_id);
        skill7 = findViewById(R.id.profile_skill7_id);
        skill8 = findViewById(R.id.profile_skill8_id);
        skill9 = findViewById(R.id.profile_skill9_id);
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
            default:
                break;
        }
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
        } else
            toast("Create clicked");
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
}
