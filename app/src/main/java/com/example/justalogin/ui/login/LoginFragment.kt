package com.example.justalogin.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.justalogin.R
import com.example.justalogin.databinding.FragmentLoginBinding
import com.example.justalogin.ui.feed.FeedActivity
import com.example.justalogin.ui.feed.FeedActivity.Companion.USER_INFO
import com.example.justalogin.utils.hideSoftKeyboard
import com.example.justalogin.utils.makeLink
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var viewBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentLoginBinding.inflate(inflater, container, false)

        setUpComposable()
        setUpSpannable()
        setUpObservable()
        setUpListeners()

        return viewBinding.root
    }

    /**
     * Set up the composable view to handle message from login process including loader
     */
    private fun setUpComposable() {
        /* The view is divided using jetpack compose and the old fashioned way with xml, just to
        * show both, all view could be handled using just one method */
        viewBinding.cvDialogContainer.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent { LoginProcessScreen(viewModel) }
        }
    }

    /**
     * Set up click for Sign up
     */
    private fun setUpSpannable() {
        with(viewBinding) {
            tvHaveAccountQuestion.makeLink(
                Pair(resources.getString(R.string.legend_signup)) { viewModel.handleNotImplemented() }
            )
        }
    }

    /**
     * Set up observables as we need to handle actions
     */
    private fun setUpObservable() {
        /* Observer for event to continue with a new path*/
        /* All this kind of events could be organized and collected as the same way as viewState
        * already does, as above I decided to separate them just to show Live data and Flows */
        viewModel.viewEvent.observe(viewLifecycleOwner) { viewEvent ->
            when (viewEvent) {
                is LoginViewEvent.SuccessLogin -> {
                    /*Just for PoC purposes*/
                    viewModel.bePrepared()
//                    requireActivity().finish()
                    with(viewBinding.cardLogin) {
                        etUserEntry.text?.clear()
                        etPassEntry.text?.clear()
                    }

                    startActivity(Intent(requireActivity(), FeedActivity::class.java).apply {
                        putExtra(USER_INFO, viewEvent.userInfo)
                    })
                }

                LoginViewEvent.NotImplemented -> {
                    handleComingSoon()
                }

                LoginViewEvent.TooManyTries -> {}// Start too many tries path

                LoginViewEvent.SuspendedUser -> {} // Start appeal path

                else -> {}
            }
        }
    }

    /**
     * Set up listeners of [viewBinding]
     */
    private fun setUpListeners() {
        with(viewBinding) {
            with(cardLogin) {
                etUserEntry.apply {
                    setOnEditorActionListener { _, _, _ ->
                        etPassEntry.requestFocus()
                    }
                }

                etPassEntry.apply {
                    setOnEditorActionListener { _, _, _ ->
                        requireActivity().hideSoftKeyboard()
                        viewModel.validateEntriesForLogin(
                            etUserEntry.text.toString(),
                            this.text.toString()
                        )
                        true
                    }
                }

                bnLogin.setOnClickListener {
                    requireActivity().hideSoftKeyboard()
                    viewModel.validateEntriesForLogin(
                        etUserEntry.text.toString(),
                        etPassEntry.text.toString()
                    )
                }

                tvForgot.setOnClickListener {
                    viewModel.handleNotImplemented()
                }
            }
        }
    }

    /**
     * Handle a feature not implemented
     */
    private fun handleComingSoon() {
        Toast.makeText(
            requireContext(), "Coming Soon!!", Toast.LENGTH_SHORT
        ).show()
    }
}