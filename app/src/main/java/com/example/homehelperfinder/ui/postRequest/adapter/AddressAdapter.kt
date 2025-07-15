package com.example.homehelperfinder.ui.postRequest.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.homehelperfinder.data.model.response.UserAddressResponse

class AddressAdapter(
    context : Context,
    private val addresses : List<UserAddressResponse>
) : ArrayAdapter<UserAddressResponse>(context, android.R.layout.simple_spinner_item, addresses){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = convertView as? TextView
            ?: View.inflate(context, android.R.layout.simple_spinner_item, null) as TextView

        textView.text = addresses[position].fullAddress
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = convertView as? TextView
            ?: View.inflate(context, android.R.layout.simple_spinner_dropdown_item, null) as TextView

        textView.text = addresses[position].fullAddress
        return textView
    }
}