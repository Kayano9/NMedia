package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import java.math.RoundingMode

interface OnInteractionListenner {
    fun onLike(post: Post) {}
    fun onRepost(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}

    fun cancel(){}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListenner
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListenner: OnInteractionListenner
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likeCount.text = round(post.likes)
            repostCount.text = round(post.reposts)
            like.setImageResource(
                if (post.likedByMe) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24
            )
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListenner.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListenner.onEdit(post)
                                true
                            }
                        else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListenner.onLike(post)
            }
            repost.setOnClickListener {
                onInteractionListenner.onRepost(post)
            }
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


class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}