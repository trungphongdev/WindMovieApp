package com.example.windmoiveapp.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.example.windmoiveapp.R
import com.example.windmoiveapp.extension.click

class HeaderBar(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private lateinit var tvTitle: TextView
    private lateinit var imgLogo: ImageView
    private lateinit var imgBack: ImageView
    private lateinit var imgNotification: ImageView
    private lateinit var imgSearch: ImageView
    private lateinit var imgUserAccount: ImageView
    private var eventSearchCallback: (() -> Unit)? = null
    private var eventNotificationCallback: (() -> Unit)? = null
    private var eventBackCallback: (() -> Unit)? = null
    private var eventAccountCallback: (() -> Unit)? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_header_bar, this)
        bindViewsByID(view)
        setEventClick()
        context.theme.obtainStyledAttributes(attrs, R.styleable.HeaderBar, 0, 0).apply {
            try {
                val isShowTitle = getBoolean(R.styleable.HeaderBar_isShowTitle, false)
                val title = getString(R.styleable.HeaderBar_title)
                val isShowButtonBack = getBoolean(R.styleable.HeaderBar_isShowButtonBack, false)
                val isShowLogo = getBoolean(R.styleable.HeaderBar_isShowLogo, false)
                val isShowSearch = getBoolean(R.styleable.HeaderBar_isShowSearch, false)
                val isShowNotification = getBoolean(R.styleable.HeaderBar_isShowNotification, false)
                checkShowIconSearch(isShowSearch)
                checkShowTitle(isShowTitle, title)
                checkShowLogo(isShowLogo)
                checkShowIconNotification(isShowNotification)
                checkShowIconBack(isShowButtonBack)
            } finally {
                recycle()
            }
        }

    }

    private fun setEventClick() {
        imgBack.click {
            eventBackCallback?.invoke()
        }
        imgSearch.click {
            eventSearchCallback?.invoke()
        }
        imgNotification.click {
            eventNotificationCallback?.invoke()
        }
        imgUserAccount.click {
            eventAccountCallback?.invoke()
        }
    }

    private fun checkShowIconBack(showButtonBack: Boolean) {
        imgBack.isVisible = showButtonBack
    }

    private fun checkShowIconNotification(showNotification: Boolean) {
        imgNotification.isVisible = showNotification
    }

    private fun checkShowIconSearch(showSearch: Boolean) {
        imgSearch.isVisible = showSearch
    }

    private fun checkShowTitle(showTitle: Boolean, title: String?) {
        tvTitle.isVisible = showTitle && title != null
        tvTitle.text = title
    }

    private fun checkShowLogo(showLogo: Boolean) {
        imgLogo.isVisible = showLogo
    }

    private fun bindViewsByID(view: View) {
        imgBack = view.findViewById(R.id.imgBack)
        imgLogo = view.findViewById(R.id.imgLogo)
        imgSearch = view.findViewById(R.id.imgSearch)
        imgNotification = view.findViewById(R.id.imgNotification)
        tvTitle = view.findViewById(R.id.tvTitle)
        imgUserAccount = view.findViewById(R.id.imgAccount)
    }
}