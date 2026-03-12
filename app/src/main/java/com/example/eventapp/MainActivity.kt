package com.example.eventapp

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fullName = findViewById<EditText>(R.id.fullName)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val email = findViewById<EditText>(R.id.email)
        val eventType = findViewById<Spinner>(R.id.eventType)
        val eventDate = findViewById<EditText>(R.id.eventDate)
        val genderGroup = findViewById<RadioGroup>(R.id.genderGroup)
        val btnChooseImage = findViewById<Button>(R.id.btnChooseImage)
        val termsCheckBox = findViewById<CheckBox>(R.id.termsCheckBox)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        // Spinner setup
        val eventTypes = arrayOf("Seminar", "Workshop", "Conference", "Webinar", "Cultural Event")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        eventType.adapter = adapter

        // DatePicker
        eventDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
                eventDate.setText("${d}/${m + 1}/${y}")
            }, year, month, day)
            dpd.show()
        }

        // Image Picker
        btnChooseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnSubmit.setOnClickListener {
            // Validation
            val name = fullName.text.toString().trim()
            val phone = phoneNumber.text.toString().trim()
            val mail = email.text.toString().trim()
            val event = eventType.selectedItem.toString()
            val date = eventDate.text.toString().trim()
            val genderId = genderGroup.checkedRadioButtonId
            val termsAccepted = termsCheckBox.isChecked

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (phone.isEmpty() || phone.length < 10) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (date.isEmpty()) {
                Toast.makeText(this, "Please select event date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (genderId == -1) {
                Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!termsAccepted) {
                Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = findViewById<RadioButton>(genderId).text.toString()

            // Confirmation Dialog
            AlertDialog.Builder(this)
                .setTitle("Confirm Registration")
                .setMessage("Do you want to submit your registration?")
                .setPositiveButton("Yes") { _, _ ->
                    // Go to confirmation screen
                    val intent = Intent(this, Result_Activity::class.java)
                    intent.putExtra("FULL_NAME", name)
                    intent.putExtra("PHONE", phone)
                    intent.putExtra("EMAIL", mail)
                    intent.putExtra("EVENT_TYPE", event)
                    intent.putExtra("EVENT_DATE", date)
                    intent.putExtra("GENDER", gender)
                    intent.putExtra("IMAGE_URI", selectedImageUri?.toString())
                    startActivity(intent)
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            if (imageUri != null) {
                selectedImageUri = imageUri
                val profileImage = findViewById<ImageView>(R.id.profileImage)
                profileImage.setImageURI(imageUri)
            }
        }
    }
}
