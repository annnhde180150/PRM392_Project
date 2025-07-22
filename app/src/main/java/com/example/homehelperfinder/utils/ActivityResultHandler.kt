package com.example.homehelperfinder.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ActivityResultHandler(
    private val activity: AppCompatActivity,
    private val onSuccess: () -> Unit
) {
    private lateinit var launcher: ActivityResultLauncher<Intent>

    fun register() {
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    onSuccess()
                }
            }
        )
    }

    fun launch(intent: Intent) {
        launcher.launch(intent)
    }
}