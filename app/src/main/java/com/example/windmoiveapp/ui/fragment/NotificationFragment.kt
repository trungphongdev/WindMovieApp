package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.NotificationAdapter
import com.example.windmoiveapp.databinding.FragmentNotiticationBinding
import com.example.windmoiveapp.extension.showAlertDialog
import com.example.windmoiveapp.viewmodels.MovieViewModel


class NotificationFragment : BaseFragment<FragmentNotiticationBinding>() {
    private val adapter: NotificationAdapter by lazy { NotificationAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNotiticationBinding {
        return FragmentNotiticationBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {
        initViews()
        initListener()
        initObserver()
    }

    private fun initListener() {
/*        adapter.onItemClickMovieItem = {
            findNavController().navigateWithAnim(
                R.id.movieDetailFragment, bundleOf(
                    MovieDetailFragment.BUNDLE_CONTENT_MOVIE to it
                ), R.id.movieTrailerFragment
            )
        }*/

        adapter.onItemClickRemoveItem = {
            activity?.showAlertDialog(getString(R.string.removeNotifyLabel),
                isShowNegativeBtn = true,
                cancelListener = {

                },
                callBack = {
                    movieViewModels.removeNotification(it.id ?: "")
                    movieViewModels.getListNotification()
                })
        }

        binding.headerBar.apply {
            setEventBackListener {
                onBackFragment()
            }
            setEventBackListener {
                super.onBackFragment()
            }
        }
    }

    private fun initObserver() {
        movieViewModels.listNotification.observe(viewLifecycleOwner) {
            dismissProgress()
            if (it.isNullOrEmpty()) {
                binding.llEmptyData.root.isVisible = true
                binding.rcvNotify.isGone = true
            } else {
                adapter.setList(it)
                binding.llEmptyData.root.isGone = true
                binding.rcvNotify.isVisible = true
            }
        }
    }

    private fun initViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.apply {
            rcvNotify.adapter = adapter
            rcvNotify.itemAnimator = DefaultItemAnimator()
            rcvNotify.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun loadData() {
        super.loadData()
        showProgress()
        movieViewModels.getListNotification()
    }
}
