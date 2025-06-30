package com.example.homehelperfinder.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.homehelperfinder.utils.Logger;

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
}
