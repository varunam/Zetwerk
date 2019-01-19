package com.zetwerk.app.zetwerk.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.adapter.EmployeeListAdapter;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeDatabase;
import com.zetwerk.app.zetwerk.data.firebase.EmployeesLoadedCallbacks;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zetwerk.app.zetwerk.apputils.Constants.EMPLOYEE_COUNT_KEY;

public class EmployeeListActivity extends AppCompatActivity implements EmployeesLoadedCallbacks, View.OnClickListener {
    
    private static final int SIGN_IN = 101;
    private RecyclerView employeeRecyclerView;
    private Button addEmployeeButton;
    private ProgressDialog progressDialog;
    
    private ArrayList<Employee> employeesList;
    private EmployeeDatabase employeeDatabase;
    private EmployeeListAdapter employeeListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (userNotSignedIn()) {
            showSignInUsingPhoneNumberUi();
        }
        
        init();
        employeeDatabase.loadEmployeeRecords(this);
        showLoader("Loading employee records...");
        
    }
    
    private void init() {
        progressDialog = new ProgressDialog(this);
        employeesList = new ArrayList<>();
        employeeDatabase = new EmployeeDatabase();
        employeeListAdapter = new EmployeeListAdapter();
        
        employeeRecyclerView = findViewById(R.id.employees_recycler_view_id);
        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeRecyclerView.setAdapter(employeeListAdapter);
        
        addEmployeeButton = findViewById(R.id.add_employee_id);
        addEmployeeButton.setOnClickListener(this);
    }
    
    private boolean userNotSignedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() == null;
    }
    
    private void showSignInUsingPhoneNumberUi() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                SIGN_IN);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null)
                    toast("Sign-in success: " + user.getPhoneNumber());
            } else {
                toast("Sign-in failed, Please try again");
            }
        }
    }
    
    public void toast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    
    @Override
    public void onEmployeeRecordsLoaded(ArrayList<Employee> employees) {
        hideLoader();
        this.employeesList = employees;
        employeeListAdapter.setEmployeesList(employees);
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_employee_id: {
                Intent intent = new Intent(this, AddEmployeeActivity.class);
                intent.putExtra(EMPLOYEE_COUNT_KEY, employeesList.size());
                startActivity(intent);
            }
        }
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
}
