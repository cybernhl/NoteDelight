package com.softartdev.notedelight.old.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.softartdev.notedelight.old.R
import com.softartdev.notedelight.old.databinding.ActivityMainBinding
import com.softartdev.notedelight.old.ui.base.BaseActivity
import com.softartdev.notedelight.old.ui.note.NoteActivity
import com.softartdev.notedelight.old.ui.signin.SignInActivity
import com.softartdev.notedelight.old.util.gone
import com.softartdev.notedelight.old.util.tintIcon
import com.softartdev.notedelight.old.util.visible
import com.softartdev.notedelight.shared.presentation.main.MainPagingViewModel
import com.softartdev.notedelight.shared.presentation.main.NotePagingResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity(contentLayoutId = R.layout.activity_main) {
    private val mainViewModel by viewModel<MainPagingViewModel>()
    private val binding by viewBinding(ActivityMainBinding::bind, android.R.id.content)
    private val mainAdapter by lazy { MainPagingAdapter(onNoteClick = this::onNoteClick) }
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainSwipeRefresh.apply {
            setProgressBackgroundColorSchemeResource(R.color.secondary)
            setColorSchemeResources(R.color.on_secondary)
            setOnRefreshListener(mainViewModel::updatePaging)
        }
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter
        }
        binding.addNoteFab.setOnClickListener {
            startActivity(NoteActivity.getStartIntent(this, 0L))
        }
        binding.mainErrorView.reloadButton.setOnClickListener {
            mainViewModel.updatePaging()
            launchJobFlow()
        }
        mainAdapter.addLoadStateListener(::onPagingLoadState)
        launchJobFlow()
        mainViewModel.updatePaging()
    }

    private fun launchJobFlow() {
        job?.cancel()
        lifecycleScope.launch {
            mainViewModel.resultStateFlow
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect(::onChanged)
        }
    }

    private fun onChanged(noteListResult: NotePagingResult) = when (noteListResult) {
        is NotePagingResult.Loading -> showProgress(true)
        is NotePagingResult.Success -> {
            showProgress(false)
            job = lifecycleScope.launch {
                noteListResult.result.collectLatest { pagingData ->
                    mainAdapter.submitData(pagingData)
                }
            }
        }
        is NotePagingResult.Error -> {
            showProgress(false)
            showError(noteListResult.error)
        }
        is NotePagingResult.NavMain -> navSignIn()
    }

    private fun onNoteClick(noteId: Long) = startActivity(NoteActivity.getStartIntent(this, noteId))

    private fun onPagingLoadState(loadState: CombinedLoadStates) {
        showProgress(show = loadState.source.refresh is LoadState.Loading)

        showEmpty(show = loadState.refresh is LoadState.NotLoading && mainAdapter.itemCount == 0)

        val errorState: LoadState.Error = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: return
        showError(message = errorState.error.message)
    }

    private fun showProgress(show: Boolean) = if (binding.mainSwipeRefresh.isRefreshing) {
        binding.mainSwipeRefresh.isRefreshing = show
    } else with(binding.mainProgressView) { if (show) visible() else gone() }

    private fun showEmpty(show: Boolean) = with(binding.mainEmptyView) {
        if (show) visible() else gone()
    }

    private fun showError(message: String?) = with(binding.mainErrorView) {
        visible()
        messageTextView.text = message
    }

    private fun navSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).tintIcon(this)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mainAdapter.removeLoadStateListener(::onPagingLoadState)
    }
}
