package com.example.abcdefg

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.FirestoreDateTimeFormatter
import com.example.abcdefg.databinding.FragmentEventDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat

// to help the read more functionality
class TruncateText(private val text: String) {
    val shortText: String
        get() = if (text.length > 250) {
            text.substring(0, 250) + "..."
        } else {
            text
        }
    val originalText: String
        get() = text

    val truncated: Boolean
        get() = text.length > 250
}

class EventDetailFragment(private val evId: String) : BottomSheetDialogFragment() {
    private var contentTruncated: Boolean = false
    private lateinit var binding: FragmentEventDetailBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false)

        val db = Firebase.firestore
        db.collection("events").document(evId).get().addOnSuccessListener {
            val ev = it.toObject(Event::class.java)!!
            binding.tvEventTitle.text = ev.name
            setEventDescription(ev.description)
            binding.tvEventDateTime.text = "${
                SimpleDateFormat("dd MMM yyyy").format(
                    FirestoreDateTimeFormatter.DateFormatter.parse(ev.eventDate)!!
                )
            } ${SimpleDateFormat("hh:mm a").format(FirestoreDateTimeFormatter.TimeFormatter.parse(ev.eventStartTime)!!)} - ${
                SimpleDateFormat(
                    "hh:mm a"
                ).format(FirestoreDateTimeFormatter.TimeFormatter.parse(ev.eventEndTime)!!)
            }"

            binding.btnJoinEvent.setOnClickListener { }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setEventDescription(text: String) {
        val truncText = TruncateText(text)
        contentTruncated = truncText.truncated

        if (truncText.truncated) {
            // show the readmore text view as well
            binding.btnDescReadMore.visibility = View.VISIBLE
            binding.tvDescContent.text = truncText.shortText
            binding.btnDescReadMore.setOnClickListener {
                if (contentTruncated) {
                    binding.tvDescContent.text = truncText.originalText
                    binding.btnDescReadMore.text = "Read Less"
                } else {
                    binding.tvDescContent.text = truncText.shortText
                    binding.btnDescReadMore.text = "Read More"
                }
                contentTruncated = !contentTruncated
            }
        } else {
            binding.btnDescReadMore.visibility = View.GONE
            binding.tvDescContent.text = truncText.originalText
        }
    }
}