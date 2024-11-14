package com.capstone.capstonetim.ui.register

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.capstone.capstonetim.R
import com.capstone.capstonetim.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private var registerJob: Job = Job()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playAnimation()
        initAction()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAction() {
        binding.apply {
            btDaftar.setOnClickListener {
                if (etMasukPassword.error.isNullOrEmpty() && etEmail.error.isNullOrEmpty()) {
                    registerUser()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_register_login),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            btBlmPunyaAkun.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment)
            )
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.tvLogin, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private fun registerUser() {
        val name = binding.etNama.text.toString().trim()
        val username = binding.etEmail.text.toString().trim()
        val password = binding.etMasukPassword.text.toString()
        showLoading(true)

        lifecycleScope.launchWhenResumed {
            if (registerJob.isActive) registerJob.cancel()

            registerJob = launch {
                registerViewModel.registerUser(name, username, password).collect { result ->
                    result.onSuccess {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.registration_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    result.onFailure { error ->
                        Snackbar.make(
                            binding.root,
                            error.localizedMessage ?: getString(R.string.registration_error_message),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.bgDim.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.etEmail.isClickable = !isLoading
        binding.etEmail.isEnabled = !isLoading
        binding.etNama.isClickable = !isLoading
        binding.etNama.isEnabled = !isLoading
        binding.etMasukPassword.isClickable = !isLoading
        binding.etMasukPassword.isEnabled = !isLoading
        binding.btDaftar.isClickable = !isLoading
    }
}