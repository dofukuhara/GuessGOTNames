package br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.R
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.databinding.GuessNameGameFragmentBinding
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.adapter.CharacterClickListener
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.adapter.CharacterListAdapter
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.CharacterModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.business.model.GameState
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.game.viewmodel.GuessNameViewModel
import br.com.fdtechcorp.android.guessgotnames.lib.gamefeature.module.gameFeatureModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

class GuessNameGameFragment : Fragment(R.layout.guess_name_game_fragment) {
    private var _binding: GuessNameGameFragmentBinding? = null
    private val binding get() = _binding!!

    private val navArgs: GuessNameGameFragmentArgs by navArgs()
    private val viewModel: GuessNameViewModel by viewModel()

    private var adapter: CharacterListAdapter? = null

    private val onCharClickListener = object : CharacterClickListener {
        override fun onClick(character: CharacterModel) {
            viewModel.userGuess(character)
        }
    }

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

        viewModel.initGame(navArgs.gameMode)

        setupViewModelObservers()
    }

    private fun setupViewModelObservers() {
        setupToolbarConfigObserver()
        setupGameStateObserver()
        setupCharNameObserver()
        setupListOfCharsObserver()
    }

    private fun setupToolbarConfigObserver() {
        viewModel.toolbarConfig.observe(viewLifecycleOwner) { (titleStringResId, shouldDisplay) ->
            with(binding.guessGameToolbar) {
                title = resources.getString(titleStringResId)
                setNavigationOnClickListener { findNavController().navigateUp() }
                handleTimeProgressIndicator(shouldDisplay)
            }
        }
    }

    private fun setupGameStateObserver() {
        viewModel.gameState.observe(viewLifecycleOwner) { gameState ->
            when (gameState) {
                GameState.SETUP     -> Unit
                GameState.DATAFETCH -> Unit
                GameState.FAILURE   -> makeErrorContainerVisible()
                GameState.STARTED   -> makeGameContentVisible()
                GameState.WON       -> Unit
                is GameState.LOOSE  -> displayAlert(gameState.score)
            }
        }
    }

    private fun makeErrorContainerVisible() {
        binding.guessNameContentErrorGroup.visibility = View.VISIBLE
        binding.guessNameContentGroup.visibility = View.INVISIBLE
    }

    private fun makeGameContentVisible() {
        binding.guessNameContentErrorGroup.visibility = View.GONE
        binding.guessNameContentGroup.visibility = View.VISIBLE
    }

    private fun setupCharNameObserver() {
        viewModel.characterNameToBeGuessed.observe(viewLifecycleOwner) { charName ->
            binding.guessNameCharName.text = charName
        }
    }

    private fun setupListOfCharsObserver() {
        viewModel.gameList.observe(viewLifecycleOwner) { listOfChars ->
            if (adapter == null) {
                adapter = CharacterListAdapter(listOfChars, onCharClickListener)
                binding.guessNameGridRv.adapter = adapter
            } else {
                adapter?.updateDataset(listOfChars)
            }
        }
    }

    private fun displayAlert(score: Int) {
        context?.let { ctx ->
            AlertDialog.Builder(ctx)
                .setTitle(R.string.gamefeature_game_over_msg_title)
                .setMessage(ctx.getString(R.string.gamefeature_game_over_msg_body, score))
                .setPositiveButton(R.string.gamefeature_game_over_msg_cta) { _, _ ->
                    findNavController().navigateUp()
                }
                .setCancelable(false)
                .show()
        } ?: run {
            findNavController().navigateUp()
        }
    }
}