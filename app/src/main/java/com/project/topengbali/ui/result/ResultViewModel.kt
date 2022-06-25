package com.project.topengbali.ui.result

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.topengbali.R
import com.project.topengbali.data.response.Topeng

class ResultViewModel : ViewModel() {

    fun getDetail(predict: Int, context: Context): LiveData<Topeng> {
        val list = MutableLiveData<Topeng>()
        val dataName = context.resources.getStringArray(R.array.name)
        val dataDesc = context.resources.getStringArray(R.array.desc)
        val dataUrl = context.resources.getStringArray(R.array.url)[predict]
        val listUrl = dataUrl.split(",")
        val data = Topeng(dataName[predict], dataDesc[predict], listUrl as ArrayList<String>)
        list.value = data
        return list
    }
}