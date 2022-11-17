package com.colinjp.inblo.android.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentSignInBinding
import com.colinjp.inblo.android.di.dataStore
import com.colinjp.inblo.android.domain.util.PreferenceKeys
import com.colinjp.inblo.android.domain.util.UserPreferences
import com.colinjp.inblo.android.domain.util.UserPreferencesRepository
import com.colinjp.inblo.android.presentation.ui.MainActivity
import com.colinjp.inblo.domain.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment: Fragment(R.layout.fragment_sign_in) {

    private var fragmentSignInBinding: FragmentSignInBinding? = null
    private val loginViewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var userPreferencesFlow: Flow<UserPreferences>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignInBinding.bind(view)
        fragmentSignInBinding = binding


        lifecycleScope.launchWhenCreated {
            userPreferencesFlow.collect {
                if(it.userId != 0){
                    val intent = Intent(requireContext(),MainActivity::class.java)
                    requireActivity().finish()
                    startActivity(intent)
                }
            }
        }

        binding.tvSignUpLink.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
        }

        binding.btnSignIn.setOnClickListener {
            if(binding.etUsername.text.toString().isBlank() || binding.etPassword.text.toString().isBlank()){
                Toast.makeText(requireContext(),"すべての項目を入力してください。",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            loginViewModel.authUser(
                binding.etUsername.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginFlow.collect { event ->
                when(event){
                    is LoginViewModel.LoginEvent.Error -> {
                        Toast.makeText(requireContext(),event.loginErrorMessage,Toast.LENGTH_SHORT).show()
                    }
                    is LoginViewModel.LoginEvent.Success -> {
                        Toast.makeText(requireContext(),event.loginResponse.meta.message,Toast.LENGTH_SHORT).show()

                        val user = event.loginResponse.data
                        storeUserIdAndStableId(User.createFromDto(user))

                        val intent = Intent(requireContext(),MainActivity::class.java)
                        startActivity(intent)
                    }
                    is LoginViewModel.LoginEvent.Loading -> {

                    }
                }
            }
        }


    }

    private suspend fun storeUserIdAndStableId(user: User){
        requireContext().dataStore.edit { preferences ->
            preferences[PreferenceKeys.SHOW_USER_ID] = user.id!!
            preferences[PreferenceKeys.SHOW_STABLE_ID] = user.stableId!!
            preferences[PreferenceKeys.SHOW_USER_NAME] = user.username!!
            preferences[PreferenceKeys.SHOW_USER_ROLE] = if(user.roleId == 1) "Person-in-Charge" else "Trainer"
        }
    }



    override fun onDestroyView() {
        fragmentSignInBinding = null
        super.onDestroyView()
    }


}