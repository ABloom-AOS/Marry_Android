package com.abloom.mery.presentation

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.abloom.mery.R
import com.abloom.mery.databinding.ActivityMainBinding
import com.abloom.mery.presentation.common.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.util.Log
import com.abloom.mery.presentation.ui.qna.ResponseSelectDialog.Companion.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mainScreen) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupWindowInsetsListener()
        setupBackPressedDispatcher()
        setupDestinationChangedListener()

        askNotificationPermission()
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setupWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback { handleBackPressed() }
    }

    private fun handleBackPressed() {
        val destination = navController.currentDestination ?: return
        if (destination.id == R.id.homeFragment) finishSoftly() else navController.navigateUp()
    }

    private fun finishSoftly() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < ASK_AGAIN_EXIT_DURATION) {
            finish()
        } else {
            backPressedTime = currentTime
            showToast(R.string.app_finish_confirm_message)
        }
    }

    // TODO("추후 애니메이션으로 배경이 서서히 변하도록 수정할 예정")
    private fun setupDestinationChangedListener() {
        navController.addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id == R.id.homeFragment) {
                binding.root.background = getColor(R.color.primary_5).toDrawable()
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                ).isAppearanceLightStatusBars = true
            } else {
                binding.root.background = Color.BLACK.toDrawable()
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                ).isAppearanceLightStatusBars = false
            }
        }
    }

    companion object {

        private const val ASK_AGAIN_EXIT_DURATION = 2_000
    }
}

