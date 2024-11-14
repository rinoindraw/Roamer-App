package com.capstone.capstonetim.ui.login

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.capstone.capstonetim.R
import com.capstone.capstonetim.databinding.FragmentLoginBinding
import com.capstone.capstonetim.ui.option.OptionActivity
import com.capstone.capstonetim.ui.option.OptionActivity.Companion.EXTRA_TOKEN
import com.capstone.capstonetim.utils.ConstVal.KEY_EMAIL
import com.capstone.capstonetim.utils.ConstVal.KEY_IS_LOGIN
import com.capstone.capstonetim.utils.ConstVal.KEY_USER_NAME
import com.capstone.capstonetim.utils.SessionManager
import com.capstone.capstonetim.utils.gone
import com.capstone.capstonetim.utils.show
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var pref: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())

        playAnimation()
        initAction()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {
        binding.apply {
            btBlmPunyaAkun.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
            )
            btMasuk.setOnClickListener {
                if (etMasukPassword.error.isNullOrEmpty() && etEmail.error.isNullOrEmpty()) {
                    loginUser()
                    findNavController().popBackStack(R.id.loginFragment, false)
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_login_format),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etMasukPassword.text.toString()

        showLoading(true)

        lifecycleScope.launchWhenResumed {
            try {
                loginViewModel.loginUser(email, password).collect { result ->
                    result.onSuccess { response ->
                        val token = response.id ?: ""
                        val username = response.name ?: ""

                        pref.saveAuthToken(token)
                        pref.setStringPreference(KEY_EMAIL, email)
                        pref.setStringPreference(KEY_USER_NAME, username)
                        pref.setBooleanPreference(KEY_IS_LOGIN, true)

                        Intent(requireContext(), OptionActivity::class.java).also { intent ->
                            intent.putExtra(EXTRA_TOKEN, token)
                            intent.putExtra(KEY_USER_NAME, username)
                            startActivity(intent)
                            requireActivity().finish()
                        }

                        Toast.makeText(
                            requireContext(),
                            getString(R.string.login_success_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    result.onFailure { error ->
                        Snackbar.make(
                            binding.root,
                            error.localizedMessage ?: getString(R.string.login_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    showLoading(false)
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    e.localizedMessage ?: getString(R.string.login_error_message),
                    Snackbar.LENGTH_SHORT
                ).show()

                showLoading(false)
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.etEmail.isClickable = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etMasukPassword.isClickable = !isLoading
        binding.etMasukPassword.isEnabled = !isLoading
        binding.btMasuk.isClickable = !isLoading
    }

}