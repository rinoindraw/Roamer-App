package com.capstone.capstonetim.ui.option

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.capstone.capstonetim.R
import com.capstone.capstonetim.databinding.ActivityOptionBinding
import com.capstone.capstonetim.ui.main.MainActivity
import com.capstone.capstonetim.utils.ConstVal
import com.capstone.capstonetim.utils.SessionManager
import com.capstone.capstonetim.utils.gone
import com.capstone.capstonetim.utils.show
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class OptionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOptionBinding

    private lateinit var pref: SessionManager
    private lateinit var token: String
    private lateinit var username: String

    private val optionViewModel: OptionViewModel by viewModels()

    private val selectedTextViews: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        pref = SessionManager(this)
        token = pref.fetchAuthToken().toString()
        username = pref.getUsername().toString()

        val textViews = listOf(
            binding.textView1,
            binding.textView2,
            binding.textView3,
            binding.textView4,
            binding.textView5,
            binding.textView6,
            binding.textView7,
            binding.textView8,
            binding.textView9,
            binding.textView10,
        )

        for (textView in textViews) {
            textView.setOnClickListener { onTextViewClick(textView) }
        }

        initAction()
    }

    private fun onTextViewClick(textView: TextView) {
        val clickedData = textView.text.toString()

        if (selectedTextViews.contains(clickedData)) {
            selectedTextViews.remove(clickedData)
            textView.background = null
        } else {
            selectedTextViews.add(clickedData)
            val clickedDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.edit_shape_border)
            clickedDrawable?.let {
                textView.background = it
            }
        }
    }

    private fun initAction() {
        binding.apply {
            val randomPlaces = mutableListOf<String>()

            lifecycleScope.launchWhenResumed {
                try {
                    for (textView in listOf(
                        textView1, textView2, textView3, textView4, textView5,
                        textView6, textView7, textView8, textView9, textView10
                    )) {
                        optionViewModel.getRandomPlace().collect { result ->
                            result.onSuccess { randomPlace ->
                                textView.text = randomPlace.name
                                randomPlaces.add(randomPlace.name)

                                if (textView == textView10) {
                                    binding.btNext.setOnClickListener {
                                        val preferences = selectedTextViews.joinToString(", ")
                                        uploadPreference(preferences)
                                    }
                                }
                            }
                            result.onFailure { error ->
                                Snackbar.make(
                                    binding.root,
                                    error.localizedMessage ?: getString(R.string.random_place_error),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.root,
                        e.localizedMessage ?: getString(R.string.random_place_error),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun uploadPreference(preferences: String) {
        showLoading(true)

        lifecycleScope.launchWhenResumed {
            try {
                optionViewModel.uploadUserPreference(preferences).collect { result ->
                    result.onSuccess { response ->

                        pref.saveAuthToken(token)
                        pref.setStringPreference(ConstVal.KEY_USER_NAME, username)
                        pref.setBooleanPreference(ConstVal.KEY_IS_LOGIN, true)

                        Toast.makeText(
                            this@OptionActivity,
                            getString(R.string.preference_upload_success),
                            Toast.LENGTH_SHORT
                        ).show()

                        val intent = Intent(this@OptionActivity, MainActivity::class.java)
                        intent.putExtra(EXTRA_TOKEN, token)
                        intent.putExtra(ConstVal.KEY_USER_NAME, username)
                        startActivity(intent)
                        finish()
                    }
                    result.onFailure { error ->
                        Snackbar.make(
                            binding.root,
                            error.localizedMessage ?: getString(R.string.preference_upload_error),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    showLoading(false)
                }
            } catch (e: Exception) {
                Snackbar.make(
                    binding.root,
                    e.localizedMessage ?: getString(R.string.preference_upload_error),
                    Snackbar.LENGTH_SHORT
                ).show()

                showLoading(false)
            }
        }
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        if (isLoading) binding.bgDim.show() else binding.bgDim.gone()
        binding.textView1.isClickable = !isLoading
        binding.textView2.isEnabled = !isLoading
        binding.textView3.isClickable = !isLoading
        binding.textView4.isEnabled = !isLoading
        binding.textView5.isClickable = !isLoading
        binding.textView6.isClickable = !isLoading
    }
}
