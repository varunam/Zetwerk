package com.zetwerk.app.zetwerk.views.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zetwerk.app.zetwerk.R;
import com.zetwerk.app.zetwerk.adapter.EmployeeListAdapter;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeCardInteractionCallbacks;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeDatabase;
import com.zetwerk.app.zetwerk.data.firebase.EmployeeDeletedCallbacks;
import com.zetwerk.app.zetwerk.data.firebase.EmployeesLoadedCallbacks;
import com.zetwerk.app.zetwerk.data.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.zetwerk.app.zetwerk.apputils.Constants.EMPLOYEE_COUNT_KEY;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.LAST_EMPLOYEE;
import static com.zetwerk.app.zetwerk.apputils.FirebaseConstants.ZETWERK;

public class EmployeeListActivity extends AppCompatActivity implements EmployeesLoadedCallbacks, View.OnClickListener, EmployeeCardInteractionCallbacks, EmployeeDeletedCallbacks {
    
    private static final String TAG = EmployeeListActivity.class.getSimpleName();
    private static final int SIGN_IN = 101;
    private RecyclerView employeeRecyclerView;
    private FloatingActionButton addEmployeeButton;
    private ProgressDialog progressDialog;
    
    private EmployeeDatabase employeeDatabase;
    private EmployeeListAdapter employeeListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        
        if (userNotSignedIn()) {
            showSignInUsingPhoneNumberUi();
        }
        
        init();
        employeeDatabase.loadEmployeeRecords(this);
        showLoader("Fetching awesome employees...");
        
    }
    
    private void init() {
        progressDialog = new ProgressDialog(this);
        employeeDatabase = new EmployeeDatabase();
        employeeListAdapter = new EmployeeListAdapter(this);
        
        employeeRecyclerView = findViewById(R.id.employees_recycler_view_id);
        employeeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        employeeRecyclerView.setAdapter(employeeListAdapter);
        
        addEmployeeButton = findViewById(R.id.add_employee_id);
        addEmployeeButton.setOnClickListener(this);
        
        observeLastEmployee();
    }
    
    private void observeLastEmployee() {
        FirebaseDatabase.getInstance()
                .getReference(ZETWERK)
                .child(LAST_EMPLOYEE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        setLastEmployeeCount((long) dataSnapshot.getValue());
                    }
                    
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    
                    }
                });
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
        employeeListAdapter.setEmployeesList(employees);
    }
    
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add_employee_id: {
                Intent intent = new Intent(this, AddEmployeeActivity.class);
                intent.putExtra(EMPLOYEE_COUNT_KEY, getLastEmployerCount());
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        MenuItem item = menu.findItem(R.id.searchID);
        
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        search(searchView);
        return true;
    }
    
    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                employeeListAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
    
    @Override
    public void onEmployeeCardLongClicked(Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete employee?")
                .setMessage("Are you sure to delete this employee?")
                .setPositiveButton("Sure", (dialogInterface, i) -> employeeDatabase.deleteEmployee(employee, this))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    
    @Override
    public void onEmployeeDeleteSuccessful(Employee employee) {
        toast("Employee deleted: " + employee.getName());
    }
    
    @Override
    public void onEmployeeDeleteFailure(Employee employee, String errorMessage) {
        toast("Employee delete failure\n" + errorMessage);
    }
    
    public void setLastEmployeeCount(long count) {
        SharedPreferences sharedPreferences = getSharedPreferences("count-pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("count-key", count);
        editor.apply();
        Log.d(TAG, "saved count: " + count);
    }
    
    public long getLastEmployerCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("count-pref", MODE_PRIVATE);
        long value = sharedPreferences.getLong("count-key", 0);
        Log.d(TAG, "returning " + value);
        return value;
    }
}
