package ru.netology.nmedia.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                repostCount.text = round(post.reposts)
                viewCount.text = round(post.views)
                likeCount.text = round(post.likes)
                like.setImageResource(
                    if (post.likedByMe) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24
                )
            }
        }
        binding.like.setOnClickListener {
            viewModel.like()
        }
        binding.repost.setOnClickListener {
            Log.d("stuff", "repost")
            viewModel.repost()
        }
    }
    fun round(a: Int): String {
        val b: String
        if (a >= 1000) {
            b = ((a / 1000.0).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "K"
        } else if (a >= 1000000) {
            b = ((a / 1000000.0).toBigDecimal().setScale(1, RoundingMode.DOWN)).toString() + "M"
        } else b = a.toString()
        return b
    }
}
