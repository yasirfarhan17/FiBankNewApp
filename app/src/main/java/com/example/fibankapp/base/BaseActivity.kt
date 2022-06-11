package com.example.fibankapp.base


import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.example.fibankapp.util.UiUtil
import com.example.fibankapp.util.toLiveData

abstract class BaseActivity<B : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    protected lateinit var binding: B
    protected abstract val viewModel: VM
    private lateinit var uiUtil: UiUtil
    val _viewState = MutableLiveData<ViewState>(ViewState.Idle)
    val viewState = _viewState.toLiveData()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(layoutId())
        uiUtil = UiUtil(this)
        observeViewState()
        addObservers()
    }


    private fun observeViewState() {
        viewState
            .observe(this) {
                when (it) {
                    ViewState.Idle -> {
                    }
                    ViewState.Loading -> {
                        showProgress()
                    }
                    is ViewState.Success -> {
                        hideProgress()
                        showMessage(it.message)
                    }
                    is ViewState.Error -> {
                        hideProgress()
                        handleException(it.throwable)
                    }
                }
            }
    }

    private fun handleException(throwable: String?) {
        showMessage(throwable)

    }


    private fun bindContentView(layoutId: Int) {
        binding = DataBindingUtil.setContentView(this, layoutId)

    }

    @LayoutRes
    abstract fun layoutId(): Int


    abstract fun addObservers()

    protected fun showProgress() {
        uiUtil.showProgress()
    }

    protected fun hideProgress() {
        uiUtil.hideProgress()
    }

    protected fun showMessage(
        message: String?,
        button: Boolean = false,
        buttonText: String = "Ok"
    ) {
        message?.let { uiUtil.showMessage(it, button = button, buttonText = buttonText) }
    }

    protected fun showToast(
        message: String?,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        message?.let { uiUtil.showToast(it, duration) }
    }

    fun getLayoutBinding() = binding

}