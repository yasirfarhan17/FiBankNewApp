package com.example.fibankapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.fibankapp.databinding.ActivityMainBinding

import com.example.fibankapp.base.BaseActivity
import com.example.fibankapp.base.ViewState
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {


    override val viewModel: MainViewModel by viewModels()

    override fun layoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        initUI()
        addListener()
    }

    fun initUI(){
        with(binding){
            matBtLogin.setOnClickListener {
                if(txtInputEtPan.text.isNullOrEmpty()){
                    txtInputEtPan.error="required"
                }
                else if(txtInputDay.text.isNullOrEmpty()){
                    txtInputDay.error="required"
                }
                else if(txtInputEtMonth.text.isNullOrEmpty()){
                    txtInputEtMonth.error="required"
                }
                else if(txtInputEtYear.text.isNullOrEmpty()){
                    txtInputEtYear.error="required"
                }
                else{


                    val s = txtInputEtPan.text.toString() // get your editext value here
                    Log.d("check",s)

                    val pattern: Pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}")

                    val matcher: Matcher = pattern.matcher(s)
                    if(matcher.matches()){
                        Toast.makeText(this@MainActivity,"Detail submitted Successfully",Toast.LENGTH_SHORT).show()
                    txtInputEtPan.text?.clear()
                    txtInputDay.text?.clear()
                    txtInputEtMonth.text?.clear()
                    txtInputEtYear.text?.clear()
                        finish()
                    }
                    else{
                        txtInputEtPan.error = "Please enter valid PAN number"
                    }
                }

            }
            tvDontHave.setOnClickListener {
                finish()
            }
        }
    }

    fun addListener(){
        with(binding){
                txtInputDay.addTextChangedListener {

                    if(it?.length!! >0){
                        if((txtInputDay.text.toString()).toInt()>31){
                            txtInputDay.error="Please Enter correct day"
                        }
                        if(txtInputDay.text?.length==2){
                            txtInputEtMonth.requestFocus()
                        }
                    }


                }
                txtInputEtMonth.addTextChangedListener {

                    if (it?.length!! > 0) {
                        if ((txtInputEtMonth.text.toString()).toInt() > 12) {
                            txtInputEtMonth.error = "Please Enter correct month"
                        }
                        if (txtInputEtMonth.text?.length == 2) {
                            txtInputEtYear.requestFocus()
                        }
                    }
                }
                txtInputEtYear.addTextChangedListener {

                    if (it?.length!! > 0) {
                        if ((txtInputEtYear.text.toString()).toInt() > 2022) {
                            txtInputEtYear.error = "Please Enter correct year"
                        }

                    }
                }
        }

    }

    override fun addObservers() {
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
                        }
                    }
                }


    }
}