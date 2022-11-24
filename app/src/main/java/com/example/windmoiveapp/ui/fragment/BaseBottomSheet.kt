package com.example.windmoiveapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.windmoiveapp.adapter.BottomSheetAdapter
import com.example.windmoiveapp.databinding.BottomSheetCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BaseBottomSheet(
    val title: String? = "",
    val items: List<Any> = arrayListOf(),
    val onItemClick: ((Int) -> Unit)? = null,
) : BottomSheetDialogFragment() {

    private var binding: BottomSheetCategoryBinding? = null
    private var adapter: BottomSheetAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomSheetCategoryBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        title?.let {
            binding?.tvTitle?.setText(it)
        }
    }

    private fun initViews() {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        adapter = BottomSheetAdapter()
        adapter?.setList(items)
        binding?.rcvItem?.apply {
            adapter = this@BaseBottomSheet.adapter
            layoutManager = LinearLayoutManager(this.context)
            itemAnimator = DefaultItemAnimator()
        }
        adapter?.onItemClick = {
            this.onItemClick?.invoke(it)
        }
    }
}