package com.colinjp.inblo.android.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.colinjp.inblo.android.R
import com.colinjp.inblo.android.databinding.FragmentSignInBinding
import com.colinjp.inblo.android.databinding.FragmentSignUpBinding
import com.colinjp.inblo.android.domain.util.initChoices
import com.colinjp.inblo.android.presentation.ui.MainActivity
import com.colinjp.inblo.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment: Fragment(R.layout.fragment_sign_up) {

    var fragmentSignUpBinding: FragmentSignUpBinding? = null
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignUpBinding.bind(view)
        fragmentSignUpBinding = binding

        binding.tvSignInLink.setOnClickListener {
            SignUpFragmentDirections.actionSignUpFragmentToSignInFragment().also {
                findNavController().navigate(it)
            }
        }

//        lifecycleScope.launchWhenCreated {
//            loginViewModel.stables.collect { stables ->
//                when(stables){
//                    is DataState.Data -> {
//                        binding.acStableName.initChoices(
//                            requireContext(),
//                            stables.data.map { it.name }, { _,pos ->
//                                if(pos != 0){
//                                    binding.acStableName.tag = stables.data[pos - 1].id.toString()
//                                }else{
//                                    binding.acStableName.tag = "0"
//                                }
//
//                            }
//                        )
//                        Timber.v("stables-> $stables")
//                    }
//                    DataState.Empty -> { }
//                    is DataState.Error -> {
//                        //TODO:
//                        Timber.e(stables.exception)
//                    }
//                    is DataState.Loading -> {
//                        //TODO:
//
//                    }
//                }
//            }
//
//        }
//        loginViewModel.getStables()

        binding.btnSignUp.setOnClickListener {

            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val email = binding.etEmail.text.toString()
            val role = when(binding.rgRole.checkedRadioButtonId){
                -1 -> -1
                R.id.role_1 -> 2
                R.id.role_2 -> 1
                else -> -1
            }
//            val raceTrack = binding.etRacetrackName.text.toString()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
//            val stableId = binding.acStableName.tag.toString()
            val stableCode = binding.etStableCode.text.toString()

            if(firstName.isBlank() ||
                    lastName.isBlank() ||
                    stableCode.isBlank() ||
                    email.isBlank() ||
                    role == -1 ||
//                    raceTrack.isBlank() ||
                    username.isBlank() ||
                    password.isBlank()
            ){

                Toast.makeText(requireContext(),"すべての項目を入力してください。",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.registerUser(
                firstName,lastName,email,username,password,role,stableCode
            )


        }



        lifecycleScope.launchWhenStarted {
            loginViewModel.loginFlow.collect { event ->
                when(event){
                    is LoginViewModel.LoginEvent.Error -> {
//                        Toast.makeText(requireContext(),"通信に問題があります。後ほどアクセスしてください。", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(),event.loginErrorMessage, Toast.LENGTH_SHORT).show()

                    }
                    is LoginViewModel.LoginEvent.Success -> {
                        val meta = event.loginResponse.meta
                        if(meta.code == 201){
                            binding.etFirstName.setText("")
                            binding.etLastName.setText("")
                            binding.etEmail.setText("")
                            binding.rgRole.clearCheck()
//                            binding.etRacetrackName.setText("")
                            binding.etUsername.setText("")
                            binding.etPassword.setText("")
                            Toast.makeText(requireContext(),"ユーザー登録が完了しました！", Toast.LENGTH_SHORT).show()
                            SignUpFragmentDirections.actionSignUpFragmentToSignInFragment().also {
                                findNavController().navigate(it)
                            }
                        }

                    }
                    is LoginViewModel.LoginEvent.Loading -> {

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentSignUpBinding = null
        super.onDestroyView()
    }
}