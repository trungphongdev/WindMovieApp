package com.example.windmoiveapp.ui.fragment

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.windmoiveapp.adapter.MyListMovieAdapter
import com.example.windmoiveapp.databinding.FragmentManagerUsersBinding
import com.example.windmoiveapp.viewmodels.MovieViewModel


class AccountManagerFragment : BaseFragment<FragmentManagerUsersBinding>() {
    private val adapter: MyListMovieAdapter by lazy { MyListMovieAdapter() }
    private val movieViewModels: MovieViewModel by lazy { MovieViewModel(activity?.application as Application) }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManagerUsersBinding {
        return FragmentManagerUsersBinding.inflate(inflater, container, false)
    }

    override fun onViewInitialized(
        view: View,
        savedInstanceState: Bundle?,
        isViewCreated: Boolean
    ) {

    }


}
