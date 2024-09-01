package com.example.coursesbuyingapp1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.coursesbuyingapp1.databinding.ActivityCourseBuyBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class CourseBuyActivity : AppCompatActivity(),PaymentResultListener {
    private lateinit var binding: ActivityCourseBuyBinding
    private lateinit var firestore: FirebaseFirestore
    var price:String? = ""
    var coData:String? = ""
    var title:String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCourseBuyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = Firebase.firestore

        val img = intent.getStringExtra("img")
         title = intent.getStringExtra("title")
         price = intent.getStringExtra("price")
        val des = intent.getStringExtra("des")
        val isBuy = intent.getStringExtra("isBuy")
         coData = intent.getStringExtra("coData")

        Glide.with(application).load(img).into(binding.courseBuyImg)

        binding.courseBuyTitle.text = title.toString()

        binding.courseBuyPrice.text = "₹.$price"

        binding.CourseBuyDes.text = des.toString()

        if (isBuy.equals("no", ignoreCase = true)) {
            binding.buyNow.text = "Buy Now"
            binding.buyNow.setOnClickListener {
                startPayment()
            }

        } else {
            binding.buyNow.text = "View Course"
            binding.buyNow.setOnClickListener {
                val i = Intent(this,DataActivity::class.java)
                i.putExtra("data",coData)
                startActivity(i)
            }
        }
    }
    private fun startPayment() {

        val activity: Activity = this
        val co = Checkout()

        val amountInSubunits = price?.toInt()
        val resultAmount =amountInSubunits!!*100

        try {
            val options = JSONObject()
            options.put("name","Saneom Technologies")
            options.put("description",title)
            //You can omit the image option to fetch the image from the dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR");
            options.put("amount",resultAmount.toString())//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(p0: String?) {
        firestore = Firebase.firestore
        firestore.collection("Courses").document(title.toString()).update("buyCourse","Yes")
        Toast.makeText(this,"Payment Success",Toast.LENGTH_SHORT).show()
        val i = Intent(this,DataActivity::class.java)
        i.putExtra("data",coData)
        startActivity(i)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment UnSuccess",Toast.LENGTH_SHORT).show()
    }
}