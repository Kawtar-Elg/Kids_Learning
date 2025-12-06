package com.kidslearning.app.ui.arabic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kidslearning.app.data.model.Letter
import com.kidslearning.app.databinding.ItemLetterBinding

/**
 * Adapter pour afficher la liste des lettres
 */
class LetterAdapter(
    private val onLetterClick: (Letter) -> Unit
) : ListAdapter<Letter, LetterAdapter.LetterViewHolder>(LetterDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val binding = ItemLetterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LetterViewHolder(binding, onLetterClick)
    }
    
    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class LetterViewHolder(
        private val binding: ItemLetterBinding,
        private val onLetterClick: (Letter) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(letter: Letter) {
            binding.tvLetter.text = letter.character
            
            binding.cardLetter.setOnClickListener {
                onLetterClick(letter)
            }
        }
    }
    
    class LetterDiffCallback : DiffUtil.ItemCallback<Letter>() {
        override fun areItemsTheSame(oldItem: Letter, newItem: Letter): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Letter, newItem: Letter): Boolean {
            return oldItem == newItem
        }
    }
}
