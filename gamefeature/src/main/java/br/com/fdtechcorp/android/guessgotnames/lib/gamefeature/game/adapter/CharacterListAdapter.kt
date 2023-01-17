package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.fdtechcorp.android.guessgotnames.lib.designsystem.util.UnitConverter
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.databinding.CharacterCardBinding
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import com.squareup.picasso.Picasso

class CharacterListAdapter(
    private var dataset: List<CharacterModel>,
    private val clickListener: CharacterClickListener
    ) : RecyclerView.Adapter<CharacterListAdapter.CharacterListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterListViewHolder {
        val binding = CharacterCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val cardSize = calculateCardSize(parent)

        return CharacterListViewHolder(binding, cardSize)
    }

    override fun onBindViewHolder(holder: CharacterListViewHolder, position: Int) {
        holder.onBind(dataset[position], clickListener)
    }

    override fun getItemCount(): Int = dataset.size

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
        fun onBind(characterModel: CharacterModel, clickListener: CharacterClickListener) {
            Picasso.get()
                .load(characterModel.imageUrl)
                .resize(cardSize, cardSize)
                .into(binding.characterCardImg)

            binding.characterCardImg.setOnClickListener {
                clickListener.onClick(characterModel)
            }
        }
    }

    companion object {
        private const val DEFAULT_PADDING_VALUE_IN_DP = 8
    }
}