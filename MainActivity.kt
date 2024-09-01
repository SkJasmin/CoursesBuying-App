package com.example.coursesbuyingapp1

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coursesbuyingapp1.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class MainActivity : AppCompatActivity(), onClick {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CoursesAdapter
    private lateinit var coursesArrayList: ArrayList<Courses>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var filteredList: ArrayList<Courses>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.coursesListView
        recyclerView.layoutManager = LinearLayoutManager(this)
        coursesArrayList = arrayListOf()

        filteredList = arrayListOf()
        adapter = CoursesAdapter(filteredList, this, this)

        recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val filterData = if (!TextUtils.isEmpty(newText)) {
                    coursesArrayList.filter {
                        it.title!!.contains(newText!!, ignoreCase = true)
                    }
                } else {
                    coursesArrayList
                }
                filteredList.clear()
                filteredList.addAll(filterData)
                adapter.notifyDataSetChanged()
                return true
            }
        })
        firestore = Firebase.firestore

        firestore.collection("Courses").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        coursesArrayList.add(dc.document.toObject(Courses::class.java))
                        filteredList.add(dc.document.toObject(Courses::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.INVISIBLE
            }
        })
    }

    override fun onClick(courses: Courses) {
        val i = Intent(this, CourseBuyActivity::class.java)
        i.putExtra("title", courses.title)
        i.putExtra("coData", courses.coData)
        i.putExtra("price", courses.price)
        i.putExtra("img", courses.img)
        i.putExtra("des", courses.des)
        i.putExtra("isBuy", courses.buyCourse)
        startActivity(i)
    }

}