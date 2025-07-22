package com.example.homehelperfinder.ui.viewFeedBack

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.homehelperfinder.R
import com.example.homehelperfinder.utils.UserManager

class ViewFeedbacckActivity : AppCompatActivity() {
    private lateinit var userMan : UserManager
    private var helperId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userMan = UserManager.getInstance(this)
        helperId = userMan.currentUserId ?: 1

        setContentView(R.layout.activity_view_feedbacck)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}