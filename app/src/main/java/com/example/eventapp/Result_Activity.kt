package com.example.eventapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Result_Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvFullName = findViewById<TextView>(R.id.tvFullName)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val tvEventType = findViewById<TextView>(R.id.tvEventType)
        val tvEventDate = findViewById<TextView>(R.id.tvEventDate)
        val tvGender = findViewById<TextView>(R.id.tvGender)
        val tvImage = findViewById<ImageView>(R.id.tvImage)
        val btnHome = findViewById<Button>(R.id.btnHome)

        tvFullName.text = intent.getStringExtra("FULL_NAME") ?: ""
        tvPhone.text = intent.getStringExtra("PHONE") ?: ""
        tvEmail.text = intent.getStringExtra("EMAIL") ?: ""
        tvEventType.text = intent.getStringExtra("EVENT_TYPE") ?: ""
        tvEventDate.text = intent.getStringExtra("EVENT_DATE") ?: ""
        tvGender.text = intent.getStringExtra("GENDER") ?: ""

        val imageUriStr = intent.getStringExtra("IMAGE_URI")
        if (!imageUriStr.isNullOrEmpty()) {
            tvImage.setImageURI(Uri.parse(imageUriStr))
        }

        btnHome.setOnClickListener {
            val intent = Intent(this, Home_Activity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }
}
