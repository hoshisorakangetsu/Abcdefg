package com.example.abcdefg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.FirestoreDateTimeFormatter
import com.example.abcdefg.databinding.ActivityEditEventBinding
import com.example.abcdefg.databinding.FragmentSavedBlogsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.UUID

class EditEventActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityEditEventBinding
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_STORAGE_PERMISSION = 2
    private lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=Firebase.auth
        val eventId = intent.getStringExtra("eventId")!!
        fetchEventData(eventId)
        binding.btnCreate.setOnClickListener {
            val db = Firebase.firestore
            val eventId = "YOUR_EVENT_ID_HERE"

            val updatedEvent = Event(
                name = binding.inputEventTitle.text.toString(),
                description = binding.inputEventDescr.text.toString(),
                eventDate = FirestoreDateTimeFormatter.DateFormatter.format(
                    SimpleDateFormat("dd MMM yyyy").parse(
                        binding.etSelectDate.text.toString()
                    )!!
                ),
                eventStartTime = FirestoreDateTimeFormatter.TimeFormatter.format(
                    SimpleDateFormat("KK:mm a").parse(
                        binding.etStartTime.text.toString()
                    )!!
                ),
                eventEndTime = FirestoreDateTimeFormatter.TimeFormatter.format(
                    SimpleDateFormat("KK:mm a").parse(
                        binding.etEndTime.text.toString()
                    )!!
                ),
                imgPath = imageUrl
            )

            db.collection("events").document(eventId).set(updatedEvent).addOnSuccessListener {
                finish()
            }
        }
        binding.btnAddImage.setOnClickListener{
            if (checkStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun fetchEventData(eventId:String){
        val db=Firebase.firestore
        db.collection("events").document(eventId).get()
            .addOnSuccessListener {document->
                if(document!=null){
                    val title=document.getString("name")
                    val description=document.getString("description")
                    val eventDate=document.getString("eventDate")
                    val eventStartTime=document.getString("eventStartTime")
                    val eventEndTime=document.getString("eventEndTime")
                    binding.inputEventTitle.setText(title)
                    binding.inputEventDescr.setText(description)
                    binding.etSelectDate.setText(eventDate)
                    binding.etStartTime.setText(eventStartTime)
                    binding.etEndTime.setText(eventEndTime)


                }

            }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            selectedImageUri?.let {
                uploadImageToFirebase(it)
            }
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri) {
        val storageReference = com.google.firebase.ktx.Firebase.storage.reference
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val fileReference = storageReference.child("images/events/$fileName")

        fileReference.putFile(fileUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString()
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkStoragePermission(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
            Toast.makeText(this, "Storage permission is needed to select an image", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_STORAGE_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                openGallery()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}