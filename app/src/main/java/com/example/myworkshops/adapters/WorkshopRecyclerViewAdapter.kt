package com.example.myworkshops.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.myworkshops.Constants.Constants.username
import com.example.myworkshops.R
import com.example.myworkshops.database.Database
import com.example.myworkshops.database.Workshop
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class workshopRecyclerViewAdapter(val context: Context?, val workshops: List<Workshop>): RecyclerView.Adapter<workshopRecyclerViewAdapter.workshopRecyclerviewHolder> (){


    class workshopRecyclerviewHolder(view: View):RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.rv_title)
        val desc: TextView = view.findViewById(R.id.rv_desc)
        val price: TextView = view.findViewById(R.id.rv_price)
        val image: ImageView = view.findViewById(R.id.rv_image)
        val btnApply: Button = view.findViewById(R.id.rv_btn_apply)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): workshopRecyclerviewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_recycler_view_dashboard, parent, false)

        return workshopRecyclerviewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: workshopRecyclerviewHolder, position: Int) {

        val workshop = workshops[position]
        holder.name.text = workshop.title
        holder.desc.text = workshop.description
        holder.price.text = workshop.price
        Picasso.get().load(workshop.image).error(R.drawable.ic_dashbaord).into(holder.image)


        if(workshop.applied == true ){
            holder.btnApply.text = "Remove"
            holder.btnApply.setBackgroundResource(R.drawable.style_button_remove)

        }else{
            holder.btnApply.text = "Apply"
            holder.btnApply.setBackgroundResource(R.drawable.style_button)

        }



        holder.btnApply.setOnClickListener {



            if(workshop.applied == true){

                    val dialog =  AlertDialog.Builder(context!!).apply {
                        setTitle("Remove Course")
                        setMessage("Are you sure you want to remove this course?")
                        setPositiveButton("Yes"){text, listner->

                            CoroutineScope(Dispatchers.IO).launch{
                                if(workshop.applied == true){

                                    holder.btnApply.text = "Apply"
                                    addOrRemove(context, workshop, false )
                                    workshop.applied = false
                                    holder.btnApply.setBackgroundResource(R.drawable.style_button)
                                }
                            }
                        }
                        setNegativeButton("No"){text, listner->
                            //do nothing
                        }
                    }
                dialog.create()
                dialog.show()

                }else{
                    CoroutineScope(Dispatchers.IO).launch{
                    holder.btnApply.text = "Remove"
                    addOrRemove(context!!, workshop, true)
                    workshop.applied = true
                    holder.btnApply.setBackgroundResource(R.drawable.style_button_remove)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return workshops.size
        }

    suspend fun addOrRemove(context: Context, workshop:Workshop, add: Boolean){
        val dao = Database.getDatabase(context, username).dao()
        if(add){
            workshop.applied = true
            dao.addWorkshop(workshop)
        }else{
            workshop.applied = false
            dao.addWorkshop(workshop)
        }

    }


}