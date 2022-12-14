package com.example.windmoiveapp.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
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


class UpdateMovieFragment : BaseFragment<FragmentUpdateMovieBinding>() {
    private val viewModel: MovieViewModel by viewModels()
    private var typeManagement: Int = INFO_MOVIE
    private var movieModel: MovieModel? = MovieModel()
    private val listYear = (1970..Calendar.getInstance().get(Calendar.YEAR)).toList()
    private var typeVideo: Int? = null
    private var imageUri: Uri? = null
    private var movieUri: Uri? = null
    private var trailerUri: Uri? = null
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
                    movieUri = it.data?.data
                    setUpVideoView(binding.videoViewMovie, it.data?.data ?: return@registerForActivityResult)
                }
                if (typeVideo == TYPE_TRAILER) {
                    trailerUri = it.data?.data
                    setUpVideoView(binding.videoViewTrailer, it.data?.data ?: return@registerForActivityResult)
                }
            }
        }

    companion object {
        const val BUNDLE_TYPE_MANAGEMENT = "BUNDLE_TYPE_MANAGEMENT"
        const val BUNDLE_CONTENT_MOVIE = "BUNDLE_CONTENT_MOVIE"
        const val TYPE_MOVIE = 0
        const val TYPE_TRAILER = 1
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
                context?.showAlertDialog(getString(R.string.postMovieOnServerSuccess)) {
                    super.onBackFragment()
                }
            } else {
                context?.showAlertDialog(getString(R.string.postVideoFailure))
            }
        }
        viewModel.listMovieLiveData.observe(viewLifecycleOwner) {}

        viewModel.postMovieStorageLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                activity?.showAlertDialog(getString(R.string.postMovieFail)) {
                    binding.videoViewMovie.setVideoURI(Uri.EMPTY)
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
                    binding.videoViewMovie.setVideoURI(Uri.EMPTY)
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
                initDataSpinner()
                binding.spYearRelease.setSelection(listYear.indexOf(movieModel?.yearOfRelease.convertStringToInt()))
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
                initDataSpinner()
                binding.spYearRelease.setSelection(listYear.indexOf(movieModel?.yearOfRelease.convertStringToInt()))
                binding.tvOption.text = getString(R.string.updateMovieLabel)
                binding.headerBar.setTitle(getString(R.string.updateMovieLabel))
            }
        }
    }

    private fun initViews() {
        binding.imvMovieImage.loadImage(movieModel?.image ?: "", R.drawable.ic_baseline_add_photo_alternate_24)
        binding.edtMovieName.setText(movieModel?.name ?: "")
        binding.edtMovieDesc.setText(movieModel?.description ?: "")
        binding.edtMovieId.setText(movieModel?.id ?: "")
        setUpVideoView(binding.videoViewMovie, movieModel?.movieUrl?.toUri() ?: return)
        setUpVideoView(binding.videoViewTrailer, movieModel?.trailerUrl?.toUri() ?: return)
        binding.edtDuration.setText(movieModel?.duration ?: "")
        binding.edtCategory.setText(movieModel?.categories?.joinToString(separator = BULLET)  ?: "")
    }

    private fun enableViews(enable: Boolean = false) {
        binding.imvMovieImage.isEnabled = enable
        binding.edtMovieName.isEnabled = enable
        binding.edtMovieDesc.isEnabled = enable
        binding.edtMovieId.isEnabled = enable
        binding.llMoviesUrl.isEnabled = enable
        binding.llTrailerUrl.isEnabled = enable
        binding.llCategory.isEnabled = enable
        binding.edtDuration.isEnabled = enable
        binding.tvMovieImage.isEnabled = enable
        binding.tvOption.isVisible = enable
        binding.spYearRelease.isEnabled = enable
    }

    private fun initListener() {
        binding.tvMovieUrl.click {
            typeVideo = TYPE_MOVIE
            pickVideo()
        }
        binding.tvTrailerUrl.click {
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
            setUpInsertMovie()
        }
    }

    private fun updateMovie() {
        postMovieOnStorageServer()
    }

    override fun loadData() {
        super.loadData()
        viewModel.getMovieList()
    }

    private fun setUpInsertMovie() {
        val id = binding.edtMovieId.text
        val name = binding.edtMovieName.text
        val desc = binding.edtMovieDesc.text
        val category = binding.edtCategory.text
        val duration = binding.edtDuration.text

        val yearOfRelease = listYear[indexYearOfRelease].toString()
        if (id.isNullOrBlank() || name.isNullOrBlank() ||
            desc.isNullOrBlank() || category.isNullOrBlank() ||
            duration.isNullOrBlank() || imageUri == null ||
            movieUri?.toString().isNullOrBlank() || trailerUri?.toString().isNullOrBlank()
        ) {
            context?.showAlertDialog(getString(R.string.addFieldInsertMovie))
        } else {
            showProgress()
            postMovieOnStorageServer()
            postTrailerOnStorageServer()
            postImageOnStorageServer()
            movieModel = MovieModel(
                id = id.toString(),
                name = name.toString(),
                description = desc.toString(),
                yearOfRelease = yearOfRelease,
                duration = duration.toString(),
                categories = category.toString().split(BULLET)
            )
        }
    }

    private fun initDataSpinner() {
        binding.spYearRelease.apply {
            activity?.let { ct ->
                val adapterCategory = SpinnerArrayAdapter(ct, listYear)
                adapter = adapterCategory
                dropDownVerticalOffset = 9
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

    private fun postResourceOnServerStorageSuccess() {
        val image = viewModel.postImageStorageLiveData.value
        val trailer =  viewModel.postTrailerStorageLiveData.value
        val movie = viewModel.postMovieStorageLiveData.value
        if (image != null && trailer != null && movie != null) {
            movieModel?.apply {
                this.image = viewModel.postImageStorageLiveData.value
                this.trailerUrl = viewModel.postTrailerStorageLiveData.value
                this.movieUrl = viewModel.postMovieStorageLiveData.value
            }
            viewModel.addMovieOnServer(movieModel ?: return)
        }
    }

    private fun postMovieOnStorageServer() {
        val fileName = binding.edtMovieName.text.toString()
        viewModel.postMovieOnServerStorage(movieUri!!, fileName)
    }

    private fun postTrailerOnStorageServer() {
        val fileName = binding.edtMovieName.text.toString()
        viewModel.postTrailerOnServerStorage(trailerUri!!, fileName)
    }

    private fun postImageOnStorageServer() {
        val fileName = binding.edtMovieName.text.toString()
        viewModel.postImageOnServerStorage(imageUri!!, fileName)
    }

    private fun setUpVideoView(videoView: VideoView, uri: Uri) {
        binding.apply {
            val mediaController = MediaController(this.root.context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.requestFocus()
            videoView.start()
        }
    }
}