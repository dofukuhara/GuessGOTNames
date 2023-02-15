package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.fdtechcorp.android.guessgotnames.lib.common.service.ImageLoader
import br.com.fdtechcorp.android.guessgotnames.lib.designsystem.util.UnitConverter
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.databinding.CharacterCardBinding
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GuessState

class CharacterListAdapter(
    private var dataset: List<CharacterModel>,
    private val clickListener: CharacterClickListener,
    private val imageLoader: ImageLoader
    ) : RecyclerView.Adapter<CharacterListAdapter.CharacterListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val binding = CharacterCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val cardSize = calculateCardSize(parent)

        return CharacterListViewHolder(binding, cardSize)
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        holder.onBind(dataset[position], clickListener, imageLoader)
    }

    override fun getItemCount(): Int = dataset.size

    fun updateDataset(newDataset: List<CharacterModel>) {
        dataset = newDataset
        notifyDataSetChanged()
    }

    private fun calculateCardSize(view: View): Int {
        val paddingOfTwoInPx = UnitConverter.dpToPixel(view.context, DEFAULT_PADDING_VALUE_IN_DP)
        val paddingOfThreeInPx = UnitConverter.dpToPixel(view.context, DEFAULT_PADDING_VALUE_IN_DP + DEFAULT_PADDING_VALUE_IN_DP)
        val isLandscape = view.height > view.width

        val cardWidth = if (isLandscape) {
            (view.width - paddingOfTwoInPx) / 2
        } else {
            (view.width - paddingOfThreeInPx) / 3
        }
        val cardHeight = if (isLandscape) {
            (view.height - paddingOfThreeInPx) / 3
        } else {
            (view.height - paddingOfTwoInPx) / 2
        }

        return minOf(cardWidth, cardHeight)
    }

    class CharacterListViewHolder(private val binding: CharacterCardBinding, private val cardSize: Int) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(characterModel: CharacterModel, clickListener: CharacterClickListener, imageLoader: ImageLoader) {
            imageLoader.loadImage(imageUrl = characterModel.imageUrl, width = cardSize, height = cardSize, imageView = binding.characterCardImg)

            when (characterModel.guessState) {
                GuessState.NOT_SET -> binding.characterCardImg.foreground = null
                GuessState.RIGHT -> binding.characterCardImg.foreground = ContextCompat.getDrawable(binding.root.context, R.drawable.right_guess_overlay)
                GuessState.WRONG -> binding.characterCardImg.foreground = ContextCompat.getDrawable(binding.root.context, R.drawable.wrong_guess_overlay)
            }

            binding.characterCardImg.setOnClickListener {
                clickListener.onClick(characterModel)
            }
        }
    }

    companion object {
        private const val DEFAULT_PADDING_VALUE_IN_DP = 8
    }
}