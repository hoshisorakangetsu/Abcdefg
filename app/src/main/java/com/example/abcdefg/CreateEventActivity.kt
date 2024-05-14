package com.example.abcdefg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.FirestoreDateTimeFormatter
import com.example.abcdefg.databinding.ActivityCreateEventBinding
import com.example.abcdefg.utils.transformIntoDatePicker
import com.example.abcdefg.utils.transformIntoTimePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.UUID

@Suppress("DEPRECATION")
class CreateEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateEventBinding
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private val REQUEST_STORAGE_PERMISSION = 2
    private var imageUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val groupId = intent.getStringExtra("groupId")

        auth = Firebase.auth
        val db = Firebase.firestore

        binding.etSelectDate.transformIntoDatePicker("dd MMM yyyy")
        binding.etStartTime.transformIntoTimePicker()
        binding.etEndTime.transformIntoTimePicker()

        @Suppress("SimpleDateFormat")
        binding.btnCreate.setOnClickListener {
            if (binding.inputEventTitle.text.isEmpty() || binding.inputEventDescr.text.isEmpty()){
                if (binding.inputEventTitle.text.isEmpty()){
                    binding.inputEventTitle.setError("Input required.")
                }
                if (binding.inputEventDescr.text.isEmpty()){
                    binding.inputEventDescr.setError("Input required.")
                }
                return@setOnClickListener
            }
            val e = Event(
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
                groupId = groupId!!,
                createdBy = auth.uid!!,
                imgPath = imageUrl
            )
            db.collection("events").add(e).addOnSuccessListener {
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