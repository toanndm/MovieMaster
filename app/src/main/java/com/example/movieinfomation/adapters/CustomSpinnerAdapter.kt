package com.example.movieinfomation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.movieinfomation.R

class CustomSpinnerAdapter(
    context: Context,
    resource: Int,
    private var items: List<Category>
): ArrayAdapter<CustomSpinnerAdapter.Category>(context, resource, items) {
    data class Category(val name: String)
    val inflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_spinner, parent, false)
        }
        (view?.findViewById(R.id.tvItemSpinner) as TextView).text = getItem(position)!!.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.item_spinner_dropdown, parent, false)
        }
        (view?.findViewById(R.id.tvItemSpinnerDropdown) as TextView).text = getItem(position)!!.name
        return view
    }
}