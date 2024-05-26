package com.abloom.mery.presentation.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.abloom.domain.user.model.Sex
import com.abloom.mery.BuildConfig
import com.abloom.mery.R
import com.abloom.mery.databinding.FragmentBrideGroomSelectionBinding
import com.abloom.mery.presentation.common.base.BaseFragment
import com.mixpanel.android.mpmetrics.MixpanelAPI
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class BrideGroomSelectionFragment :
    BaseFragment<FragmentBrideGroomSelectionBinding>(R.layout.fragment_bride_groom_selection) {

    private val signUpViewModel: SignUpViewModel by viewModels({ requireParentFragment() })
    private val marryDateFragment by lazy { MarryDateFragment() }
    private val signUpFragmentManager by lazy { parentFragmentManager }
    private lateinit var mp: MixpanelAPI

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initBindingViewModel()
        mp = MixpanelAPI.getInstance(requireContext(), BuildConfig.MIX_PANEL_TOKEN, false);
    }

    private fun initListener() {
        binding.groomBut.setOnClickListener {
            moveToMarryDateFragment()
            signUpViewModel.selectSex(Sex.MALE)

            val props = JSONObject()
            props.put("Sex", "예비 신랑")
            mp.people.set("Sex", "예비 신랑")
            mp.track("signup_sex_type", props)
        }

        binding.brideBut.setOnClickListener {
            moveToMarryDateFragment()
            signUpViewModel.selectSex(Sex.FEMALE)

            val props = JSONObject()
            props.put("Sex", "예비 신부")
            mp.people.set("Sex", "예비 신부")
            mp.track("signup_sex_type", props)
        }
    }

    private fun initBindingViewModel() {
        binding.viewModel = signUpViewModel
    }

    private fun moveToMarryDateFragment() {
        signUpFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            replace(R.id.fragmentContainerView, marryDateFragment)
            addToBackStack(null)
            commit()
        }
    }
}
