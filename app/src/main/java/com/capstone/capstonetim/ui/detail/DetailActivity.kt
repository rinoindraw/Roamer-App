package com.capstone.capstonetim.ui.detail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.capstone.capstonetim.R
import com.capstone.capstonetim.database.model.PlaceResponse
import com.capstone.capstonetim.databinding.ActivityDetailBinding
import com.capstone.capstonetim.ui.main.MainActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val historyListJson = intent.getStringExtra(EXTRA_DETAIL_DATA)
        val historyList = Gson().fromJson(historyListJson, Array<PlaceResponse>::class.java)
        val story = historyList?.firstOrNull()
        initStoryData(story)

        initUI()
        initAction()
    }

    private fun initUI() {
        supportActionBar?.hide()
    }

    private fun initAction() {
        binding.apply {
            imgToolbarBtnBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
                finish()
            }

            imgToolbarBtnFav.setOnClickListener {
                toggleFavoriteState()
            }
        }
    }

    private fun toggleFavoriteState() {
        isFavorite = !isFavorite
        if (isFavorite) {
            binding.imgToolbarBtnFav.setImageResource(R.drawable.ic_fav_full)
            Toast.makeText(
                this@DetailActivity,
                getString(R.string.favorite_success),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.imgToolbarBtnFav.setImageResource(R.drawable.ic_fav)
        }
        // Perform additional actions when the favorite state is toggled
    }


    private fun initStoryData(story : PlaceResponse?) {
        if (story != null) {
            binding.apply {

                Glide
                    .with(this@DetailActivity)
                    .load(story.img_link)
                    .placeholder(R.drawable.logo)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .into(imgItem)

                tvCategory.text = story.category
                tvInfo.text = story.description
                tvLocation.text = story.city
                tvName.text = story.name

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
        finish()

    }

    companion object {
        const val EXTRA_DETAIL_DATA = "extra_detail_data"
    }

}