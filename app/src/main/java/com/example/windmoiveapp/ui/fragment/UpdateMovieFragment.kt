package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.adapter.SpinnerArrayAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentUpdateMovieBinding
import com.example.windmoiveapp.extension.click
import com.example.windmoiveapp.extension.loadImage
import com.example.windmoiveapp.extension.onItemSelected
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.util.BULLET
import com.example.windmoiveapp.util.INFO_MOVIE
import com.example.windmoiveapp.util.ZERO_INDEX
import com.example.windmoiveapp.viewmodels.MovieViewModel
import java.util.*


class UpdateMovieFragment: BaseFragment<FragmentUpdateMovieBinding>() {
    private val viewModel: MovieViewModel by viewModels()
    private var typeManagement: Int = INFO_MOVIE
    private var movieModel: MovieModel? = MovieModel()
    private var movieUri: Uri? = null
    private var imageUri: Uri? = null
    private var typeVideo: Int? = null

    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            binding.imvMovieImage.setImageURI(imageUri)
        }

    private val getVideoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                movieUri = it.data?.data
                if (typeVideo == TYPE_MOVIE) {
                    binding.edtMovieUrl.setText(movieUri.toString())
                }
                if (typeVideo == TYPE_TRAILER) {
                    binding.edtTrailerUrl.setText(movieUri.toString())
                }
            }
        }

    companion object {
        const val BUNDLE_TYPE_MANAGEMENT =  "BUNDLE_TYPE_MANAGEMENT"
        const val BUNDLE_CONTENT_MOVIE =  "BUNDLE_CONTENT_MOVIE"
        const val TYPE_MOVIE =  0
        const val TYPE_TRAILER =  1
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

    private fun initObserver() {
    }

    private fun initViews() {
        binding.imvMovieImage.loadImage(movieModel?.image ?: "")
        binding.edtMovieName.setText(movieModel?.name)
        binding.edtMovieDesc.setText(movieModel?.description)
        binding.edtMovieId.setText(movieModel?.id)
        binding.edtMovieUrl.setText(movieModel?.movieUrl)
        binding.edtTrailerUrl.setText(movieModel?.trailerUrl)
        binding.edtDuration.setText(movieModel?.duration)
        binding.edtCategory.setText(movieModel?.categories?.joinToString(separator = BULLET))
        initDataSpinner()

    }

    private fun initListener() {
        binding.llMovieUrl.click {
            typeVideo = TYPE_MOVIE
            pickVideo()
        }
        binding.llTrailerUrl.click {
            typeVideo = TYPE_TRAILER
            pickVideo()
        }
        binding.imvMovieImage.click {
            pickImage()
        }
        binding.tvMovieImage.click {
            pickImage()
        }
        binding.edtCategory.click {
            BaseBottomSheet(items = Categories.values().map { it.type }.toList()) {
                binding.edtCategory.text?.append(Categories.values().toList()[it].type)
            }
        }
    }

    private fun initDataSpinner() {
        binding.spYearRelease.apply {
            activity?.let { ct ->
                val listYear = (1970..Calendar.getInstance().get(Calendar.YEAR)).toList()
                val adapterCategory = SpinnerArrayAdapter(ct, listYear)
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

    private fun pickVideo() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "video/*";
        getVideoResult.launch(intent)

    }

    private fun pickImage() {
        getImageResult.launch("image/*")
    }
}