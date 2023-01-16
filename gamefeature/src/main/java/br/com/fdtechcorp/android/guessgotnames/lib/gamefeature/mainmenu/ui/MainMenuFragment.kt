package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.mainmenu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.databinding.MainMenuFragmentBinding
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameMode

class MainMenuFragment : Fragment(R.layout.main_menu_fragment) {
    private var _binding: MainMenuFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGameModesButtonListeners()
    }

    private fun setupGameModesButtonListeners() {
        binding.mainMenuPracticeModeBtn.setOnClickListener {
            navigateTo(GameMode.PRACTICE_MODE)
        }
        binding.mainMenuTimedModeBtn.setOnClickListener {
            navigateTo(GameMode.TIMED_MODE)
        }
    }

    private fun navigateTo(gameMode: GameMode) {
        val action = MainMenuFragmentDirections.actionMainMenuFragmentToGuessNameGameFragment(gameMode)
        findNavController().navigate(action)
    }
}