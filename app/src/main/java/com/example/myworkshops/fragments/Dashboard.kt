package com.example.myworkshops.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.R
import com.example.myworkshops.database.Database
import com.example.myworkshops.database.Workshop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class Dashboard : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: workshopRecyclerViewAdapter
    lateinit var  workshopList: List<Workshop>
    lateinit var txtDashboardHeader: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.dashboard_fragment, container, false)

        txtDashboardHeader = view.findViewById(R.id.txt_header_dashboard)

        layoutManager = LinearLayoutManager(activity)

        recyclerView = view.findViewById(R.id.rv)

        CoroutineScope(Dispatchers.IO).launch {
            workshopList = getWorkshops(requireActivity().applicationContext)
            CoroutineScope(Dispatchers.Main).launch{

                if(workshopList.isEmpty()){
                    txtDashboardHeader.setText("You have not applied to any Workshops yet..")
                }else{
                    txtDashboardHeader.setText("Your applied workshops!")
                }
                recyclerAdapter = workshopRecyclerViewAdapter(activity, workshopList)
                recyclerView.adapter = recyclerAdapter
                recyclerView.layoutManager = layoutManager
            }
        }

        return view

    }

    suspend fun getWorkshops(context:Context): List<Workshop>{
        val dao = Database.getDatabase(context, userid = username).dao()
        return dao.getSpecificWorkshops(true)
    }

}