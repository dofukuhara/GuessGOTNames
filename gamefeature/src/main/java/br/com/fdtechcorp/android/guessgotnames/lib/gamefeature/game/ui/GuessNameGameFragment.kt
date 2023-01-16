package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.databinding.GuessNameGameFragmentBinding
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.module.gameFeatureModule
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class GuessNameGameFragment : Fragment(R.layout.guess_name_game_fragment) {
    private var _binding: GuessNameGameFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: GuessNameGameFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loadKoinModules(gameFeatureModule)

        _binding = GuessNameGameFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        unloadKoinModules(gameFeatureModule)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mode = context?.resources?.getString(navArgs.gameMode.titleStringResId)
        Toast.makeText(view.context, "Game Mode [$mode]", Toast.LENGTH_SHORT).show()
    }
}