package com.example.coursesbuyingapp1

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coursesbuyingapp1.databinding.CoursesLayoutBinding
import java.util.ArrayList

class CoursesAdapter(private val coursesArrayList: ArrayList<Courses>, private val context: Context, private val onClick: onClick) :
    RecyclerView.Adapter<CoursesAdapter.ViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CoursesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPos = coursesArrayList[position]
        Glide.with(context).load(currentPos.img).into(holder.img)
        holder.title.text = currentPos.title.toString()

        holder.itemView.setOnClickListener {
            onClick.onClick(currentPos)
        }
    }
    override fun getItemCount(): Int {
        return coursesArrayList.size
    }
    inner class ViewHolder(binding: CoursesLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        val img = binding.courseImg
        val title = binding.courseTitle
    }
}
interface  onClick {
    fun onClick(courses: Courses)
}