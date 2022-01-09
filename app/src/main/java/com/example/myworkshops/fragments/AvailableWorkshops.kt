package com.example.myworkshops.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.R
import com.example.myworkshops.adapters.AvailWorkshopRecyclerViewAdapter
import com.example.myworkshops.database.Database
import com.example.myworkshops.database.Workshop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvailableWorkshops : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager

    lateinit var recyclerAdapter: AvailWorkshopRecyclerViewAdapter
    lateinit var  workshopList: List<Workshop>
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_available_workshops, container, false)

        layoutManager = LinearLayoutManager(activity)

        recyclerView = view.findViewById(R.id.rv_avail_Workshops)
        progressBar = view.findViewById(R.id.progressbar)
        progressLayout = view.findViewById(R.id.progress_layout)

        progressLayout.visibility = View.VISIBLE
        progressBar.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            workshopList = getAllWorkshops(requireActivity().applicationContext)
            CoroutineScope(Dispatchers.Main).launch{
                recyclerAdapter = AvailWorkshopRecyclerViewAdapter(activity, workshopList)
                recyclerView.adapter = recyclerAdapter
                recyclerView.layoutManager = layoutManager
                progressBar.visibility = View.INVISIBLE
                progressLayout.visibility = View.INVISIBLE
            }
        }

        return view
    }

    suspend fun getAllWorkshops(context: Context): List<Workshop>{
        val dao = Database.getDatabase(context, userid = username).dao()
        return dao.getAllWorkshops()
    }

}