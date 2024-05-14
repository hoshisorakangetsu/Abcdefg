package com.example.abcdefg

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.abcdefg.databinding.FragmentEventDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

// to help the read more functionality
class TruncateText(private val text: String) {
    val shortText: String
        get() = if (text.length > 250) {text.substring(0, 250) + "..."} else {text}
    val originalText: String
        get() = text

    val truncated: Boolean
        get() = text.length > 35
}

class EventDetailFragment(private val evId: String) : BottomSheetDialogFragment() {
    private var contentTruncated: Boolean = false
    private lateinit var binding: FragmentEventDetailBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    // make bottom sheet always expanded on open
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailBinding.inflate(inflater, container, false)

        val db = Firebase.firestore
        db.collection("events").document(evId).get().addOnSuccessListener {

            binding.btnJoinEvent.setOnClickListener {  }
        }

        // testing purposes
        val test = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut volutpat ipsum quis mi luctus, id feugiat ante sollicitudin. Proin quis dolor justo. Nullam id magna ut arcu vestibulum dictum. Duis pharetra, leo a vulputate pulvinar, turpis nisl condimentum urna, id posuere purus justo in dolor. Vestibulum diam dui, feugiat sed sollicitudin sit amet, tristique quis mi. Aenean quis accumsan augue. Etiam nisi turpis, euismod non leo ac, condimentum mattis sapien. Proin vel porttitor lectus. Vestibulum semper ante viverra ex elementum, ac efficitur sapien pharetra. In nibh odio, hendrerit ut purus at, laoreet eleifend felis. Cras vestibulum ipsum sit amet urna condimentum imperdiet. Donec imperdiet sem non eros aliquet, ac porttitor purus posuere. Vivamus sollicitudin feugiat metus ut varius. Donec eget condimentum tellus. Nulla urna risus, blandit sed dolor a, semper rhoncus justo. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut volutpat ipsum quis mi luctus, id feugiat ante sollicitudin. Proin quis dolor justo. Nullam id magna ut arcu vestibulum dictum. Duis pharetra, leo a vulputate pulvinar, turpis nisl condimentum urna, id posuere purus justo in dolor. Vestibulum diam dui, feugiat sed sollicitudin sit amet, tristique quis mi. Aenean quis accumsan augue. Etiam nisi turpis, euismod non leo ac, condimentum mattis sapien. Proin vel porttitor lectus. Vestibulum semper ante viverra ex elementum, ac efficitur sapien pharetra. In nibh odio, hendrerit ut purus at, laoreet eleifend felis. Cras vestibulum ipsum sit amet urna condimentum imperdiet. Donec imperdiet sem non eros aliquet, ac porttitor purus posuere. Vivamus sollicitudin feugiat metus ut varius. Donec eget condimentum tellus. Nulla urna risus, blandit sed dolor a, semper rhoncus justo."

        setEventDescription(test)

        return binding.root
    }

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
        }
    }
}