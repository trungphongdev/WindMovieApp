package com.example.windmoiveapp.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.adapter.CategoryArrayAdapter
import com.example.windmoiveapp.databinding.FragmentUpdateMovieBinding
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.extension.onItemSelected
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.util.INFO_MOVIE
import com.example.windmoiveapp.util.ZERO_INDEX
import com.example.windmoiveapp.viewmodels.MovieViewModel


class UpdateMovieFragment: BaseFragment<FragmentUpdateMovieBinding>() {
    private val viewModel: MovieViewModel by viewModels()
    private var typeManagement: Int = INFO_MOVIE
    private var movieModel: MovieModel? = MovieModel()
    private var movieUri: Uri? = null
    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            movieUri = uri
            binding.imvMovieImage.setImageURI(movieUri)
        }

    companion object {
        const val BUNDLE_TYPE_MANAGEMENT =  "BUNDLE_TYPE_MANAGEMENT"
        const val BUNDLE_CONTENT_MOVIE =  "BUNDLE_CONTENT_MOVIE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        typeManagement = arguments?.getInt(BUNDLE_TYPE_MANAGEMENT) ?: INFO_MOVIE
        movieModel = arguments?.getParcelable(BUNDLE_CONTENT_MOVIE)
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentUpdateMovieBinding {
        return FragmentUpdateMovieBinding.inflate(inflater)
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

    private fun initViews() {
        binding.imvMovieImage.loadImage(movieModel?.image ?: "")
        binding.edtMovieName.setText(movieModel?.name)
        binding.edtMovieDesc.setText(movieModel?.description)
        binding.edtMovieId.setText(movieModel?.id)
        binding.edtMovieUrl.setText(movieModel?.movieUrl)
        binding.edtTrailerUrl.setText(movieModel?.trailerUrl)
        binding.edtDuration.setText(movieModel?.duration)
        binding.edtYearOfRelease.setText(movieModel?.yearOfRelease)
        initDataSpinner()

    }

    private fun initDataSpinner() {
        binding.spCategory.apply {
            activity?.let { ct ->
                val adapterCategory = CategoryArrayAdapter(ct, movieModel?.categories ?: emptyList())
                adapter = adapterCategory
                adapterCategory.setChoose(ZERO_INDEX)
                //dropDownVerticalOffset = TabTradeOrders.OFFSET_TRANSACTION_HISTORY
                onItemSelected { index ->
                    adapterCategory.setChoose(index)
                       // loadDataWithDate(index)
                }
            }
        }
    }

    private fun initListener() {
    }

    private fun initObserver() {
    }


}