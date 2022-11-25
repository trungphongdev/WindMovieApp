package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.windmoiveapp.R
import com.example.windmoiveapp.adapter.SpinnerArrayAdapter
import com.example.windmoiveapp.constant.Categories
import com.example.windmoiveapp.databinding.FragmentUpdateMovieBinding
import com.example.windmoiveapp.extension.*
import com.example.windmoiveapp.model.MovieModel
import com.example.windmoiveapp.model.isMovieExist
import com.example.windmoiveapp.util.*
import com.example.windmoiveapp.viewmodels.MovieViewModel
import java.util.*


class UpdateMovieFragment: BaseFragment<FragmentUpdateMovieBinding>() {
    private val viewModel: MovieViewModel by viewModels()
    private var typeManagement: Int = INFO_MOVIE
    private var movieModel: MovieModel? = MovieModel()
    private val listYear = (1970..Calendar.getInstance().get(Calendar.YEAR)).toList()
    private var typeVideo: Int? = null
    private var imageUri: Uri? = null
    private var indexYearOfRelease: Int = 0

    private val getImageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            binding.imvMovieImage.setImageURI(uri)
        }

    private val getVideoResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (typeVideo == TYPE_MOVIE) {
                    binding.edtMovieUrl.setText(it.data?.data.toString())
                }
                if (typeVideo == TYPE_TRAILER) {
                    binding.edtTrailerUrl.setText(it.data?.data.toString())
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
        invalidManagementMovie()
        initListener()
        initObserver()
    }

    private fun initObserver() {
        viewModel.postMovieSuccessLiveData.observe(viewLifecycleOwner) {
            dismissProgress()
            if (it) {
                context?.showAlertDialog(getString(R.string.postMovieOnServerSucess)) {
                    initViews()
                }
            } else {
                context?.showAlertDialog(getString(R.string.postVideoFailure))
            }
        }
        viewModel.listMovieLiveData.observe(viewLifecycleOwner) {}

        viewModel.postMovieStorageLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                activity?.showAlertDialog(getString(R.string.postMovieFail)) {
                    binding.edtMovieUrl.setText(getText(R.string.emptyLabel))
                }
            } else {
                postResourceOnServerStorageSuccess()
            }
        }
        viewModel.postImageStorageLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                activity?.showAlertDialog(getString(R.string.postImageFail)) {
                    binding.imvMovieImage.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24)
                }
            } else {
                postResourceOnServerStorageSuccess()
            }
        }
        viewModel.postTrailerStorageLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                activity?.showAlertDialog(getString(R.string.postTrailerFail)) {
                    binding.edtMovieUrl.setText(getText(R.string.emptyLabel))
                }
            } else {
                postResourceOnServerStorageSuccess()
            }
        }
    }

    private fun invalidManagementMovie() {
        when (typeManagement) {
            INFO_MOVIE -> {
                enableViews()
                initViews()
                initDataSpinner(listYear.indexOf(movieModel?.yearOfRelease.convertStringToInt()))
                binding.headerBar.setTitle(getString(R.string.infoMovieLabel))
            }
            ADD_MOVIE -> {
                enableViews(true)
                initDataSpinner()
                binding.tvOption.text = getString(R.string.addMovieLabel)
                binding.headerBar.setTitle(getString(R.string.addMovieLabel))
            }
            EDIT_MOVIE -> {
                enableViews(true)
                initViews()
                initDataSpinner(listYear.indexOf(movieModel?.yearOfRelease.convertStringToInt()))
                binding.tvOption.text = getString(R.string.updateMovieLabel)
                binding.headerBar.setTitle(getString(R.string.updateMovieLabel))
            }
        }
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
    }
    
    private fun enableViews(enable: Boolean = false) {
        binding.imvMovieImage.isEnabled = enable
        binding.edtMovieName.isEnabled = enable
        binding.edtMovieDesc.isEnabled = enable
        binding.edtMovieId.isEnabled = enable
        binding.llMovieUrl.isEnabled = enable
        binding.llTrailerUrl.isEnabled = enable
        binding.llCategory.isEnabled = enable
        binding.edtDuration.isEnabled = enable
        binding.tvMovieImage.isEnabled = enable
        binding.tvOption.isVisible = enable
        binding.spYearRelease.isEnabled = enable
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
        binding.llCategory.click {
            BaseBottomSheet(items = Categories.values().map { it.type }.toList()) {
                binding.edtCategory.text?.append(Categories.values().toList()[it].name)
            }.show(childFragmentManager, BaseBottomSheet::class.simpleName)
        }
        binding.headerBar.apply {
            setEventBackListener {
                super.onBackFragment()
            }
        }
        binding.tvOption.click {
            when (typeManagement) {
                EDIT_MOVIE -> {
                    updateMovie()
                }
                ADD_MOVIE -> {
                    insertMovie()
                }
                else -> {

                }
            }
        }
    }

    private fun insertMovie() {
        val itemExist = viewModel.listMovieLiveData.value?.isMovieExist(binding.edtMovieId.text.toString())
        if (itemExist == true) {
            context?.showAlertDialog(getString(R.string.idMovieExistLabel))
        } else {
            showProgress()
            postMovieOnStorageServer()
            postTrailerOnStorageServer()
            postImageOnStorageServer()
        }
    }


    private fun updateMovie() {
        postMovieOnStorageServer()
    }

    private fun postMovieOnStorageServer() {
        val movieUri = binding.edtMovieUrl.text
        val fileName = binding.edtMovieName.text
        if (movieUri.isNullOrBlank() || fileName.isNullOrBlank()) {
            context?.showAlertDialog(getString(R.string.addFieldInsertMovie))

        } else {
            viewModel.postMovieOnServerStorage(movieUri.toString().toUri(), fileName.toString())
        }
    }

    private fun postTrailerOnStorageServer() {
        val trailerUri = binding.edtTrailerUrl.text
        val fileName = binding.edtMovieName.text
        if (binding.edtTrailerUrl.text.isNullOrBlank() || fileName.isNullOrBlank()) {
            context?.showAlertDialog(getString(R.string.addFieldInsertMovie))

        } else {
            viewModel.postTrailerOnServerStorage(trailerUri.toString().toUri(), fileName.toString())
        }
    }
    private fun postImageOnStorageServer() {
        val fileName = binding.edtMovieName.text
        if (imageUri == null || fileName.isNullOrBlank()) {
            context?.showAlertDialog(getString(R.string.addFieldInsertMovie))
        } else {
            viewModel.postImageOnServerStorage(imageUri!!, fileName.toString())
        }
    }

    private fun initDataSpinner(initValue: Int = ZERO_INDEX) {
        binding.spYearRelease.apply {
            activity?.let { ct ->
                val adapterCategory = SpinnerArrayAdapter(ct, listYear)
                adapter = adapterCategory
                adapterCategory.setChoose(initValue)
                dropDownVerticalOffset = 10
                onItemSelected { index ->
                    adapterCategory.setChoose(index)
                    indexYearOfRelease = index
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

    override fun loadData() {
        super.loadData()
        viewModel.getMovieList()
    }

    private fun getItemMovieInsert() {
        val id = binding.edtMovieId.text
        val name = binding.edtMovieName.text
        val desc = binding.edtMovieDesc.text
        val category = binding.edtCategory.text
        val duration = binding.edtDuration.text
        val image = viewModel.postImageStorageLiveData.value
        val movie = viewModel.postMovieStorageLiveData.value
        val trailer = viewModel.postTrailerStorageLiveData.value
        val yearOfRelease = listYear[indexYearOfRelease]
        if (id.isNullOrBlank() && name.isNullOrBlank()
            && desc.isNullOrBlank() && category.isNullOrBlank()
            && duration.isNullOrBlank()
        ) {
            context?.showAlertDialog(getString(R.string.addFieldInsertMovie))
        } else {
            movieModel = MovieModel(
                id = id.toString(),
                name = name.toString(),
                description = desc.toString(),
                yearOfRelease = yearOfRelease.toString(),
                duration = duration.toString(),
                image = image.toString(),
                trailerUrl = trailer.toString(),
                movieUrl = movie.toString(),
                categories = category.toString().split(BULLET)
            )
            viewModel.addMovieOnServer(movieModel ?: return)
        }
    }

    private fun postResourceOnServerStorageSuccess() {
        val image = viewModel.postImageStorageLiveData.value
        val movie = viewModel.postMovieStorageLiveData.value
        val trailer = viewModel.postTrailerStorageLiveData.value
        if (image != null && movie != null && trailer != null) {
            getItemMovieInsert()
        }
    }
}