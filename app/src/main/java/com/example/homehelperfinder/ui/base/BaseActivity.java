package com.example.homehelperfinder.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.ui.MenuActivity;
import com.example.homehelperfinder.utils.Logger;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(getClass().getSimpleName(), "onCreate");

        // Hide action bar by default
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(getClass().getSimpleName(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(getClass().getSimpleName(), "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(getClass().getSimpleName(), "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(getClass().getSimpleName(), "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName(), "onDestroy");
        hideProgressDialog();
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    protected void showProgressDialog() {
        showProgressDialog("Loading...");
    }

    protected void showProgressDialog(String message) {
        if (progressDialog != null && !isFinishing()) {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void showLoading(String message) {
        showProgressDialog(message);
    }

    protected void hideLoading() {
        hideProgressDialog();
    }

    protected Context getContext() {
        return this;
    }

    protected void logError(String message, Throwable throwable) {
        Logger.e(getClass().getSimpleName(), message, throwable);
    }

    protected void logInfo(String message) {
        Logger.i(getClass().getSimpleName(), message);
    }

    protected void logDebug(String message) {
        Logger.d(getClass().getSimpleName(), message);
    }

    /**
     * Setup menu navigation FAB
     * Call this method in onCreate after setContentView to enable menu navigation
     */
    protected void setupMenuNavigation() {
        FloatingActionButton fabMenu = findViewById(R.id.fab_menu);
        if (fabMenu != null) {
            fabMenu.setOnClickListener(v -> {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                Logger.d(getClass().getSimpleName(), "Navigated to Menu from FAB");
            });
        }
    }

    /**
     * Handle options menu item selection
     * Override this method in child activities if needed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_reviews) {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.review.MyReviewsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.notification.NotificationActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_profile) {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.editProfile.CustomerEditProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_helper_reports) {
            Intent intent = new Intent(this, com.example.homehelperfinder.ui.reports.helper.HelperReportsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
