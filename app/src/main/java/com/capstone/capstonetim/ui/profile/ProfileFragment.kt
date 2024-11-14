package com.capstone.capstonetim.ui.profile

import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.capstone.capstonetim.R
import com.capstone.capstonetim.databinding.FragmentProfileBinding
import com.capstone.capstonetim.ui.auth.AuthActivity
import com.capstone.capstonetim.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var pref: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = SessionManager(requireContext())

        initUI()
        initAction()

    }

    private fun initUI() {
        binding.tvProfile.text = pref.getUserName
        binding.tvEmail.text = pref.getEmail

    }

    private fun initAction() {

        binding.apply {

            tvLogOut.setOnClickListener {
                initLogoutDialog()
            }

            tvSetting.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            imgClose.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.action_profileFragment_to_navigation_home)
            )

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLogoutDialog() {
        val alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setTitle(getString(R.string.message_logout_confirm))
            ?.setPositiveButton(getString(R.string.action_yes)) { _, _ ->
                pref.clearAuthToken()
                pref.clearPreferences()
                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            ?.setNegativeButton(getString(R.string.action_cancel), null)
        val alert = alertDialog.create()
        alert.show()
    }

}
