package com.abloom.mery

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.abloom.mery.databinding.ActivityHomeBinding
import com.abloom.mery.presentation.ui.home.LoginDialogFragment

class HomeActivity : AppCompatActivity() {

    private val binding: ActivityHomeBinding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        applyWindowInsetsPadding()
        showLoginDialog()
    }

    private fun showLoginDialog() {
        val bottomSheetFragment = LoginDialogFragment()
        bottomSheetFragment.show(supportFragmentManager, LoginDialogFragment().tag)
    }

    private fun applyWindowInsetsPadding() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
