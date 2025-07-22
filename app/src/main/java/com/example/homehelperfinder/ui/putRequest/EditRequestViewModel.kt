package com.example.homehelperfinder.ui.putRequest
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditRequestViewModel() : ViewModel(){
    var requestId: Int = 0
    var serviceId: Int = 0
    var addressId: Int = 0
    var startTime: MutableLiveData<String> = MutableLiveData()
    var duration: MutableLiveData<String> = MutableLiveData()
    var specialNote: MutableLiveData<String> = MutableLiveData()
    var address: MutableLiveData<String> = MutableLiveData()
    var status : String = ""
}