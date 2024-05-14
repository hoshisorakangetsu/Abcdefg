package com.example.abcdefg

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.abcdefg.databinding.FragmentEditUserProfileBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class EditUserProfileFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        db = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibEditImage.setOnClickListener {
            // Handle image edit
        }

        binding.btnUpload.setOnClickListener {
            updateUserProfile()
        }
    }

    private fun updateUserProfile() {
        val userId = auth.uid
        val newDisplayName = binding.tidtDisplayName.text.toString()
        if (userId == null) {
            binding.btnUpload.text = "idnull"
            return
        }
        binding.btnUpload.text = "btnwork"

        db.collection("users").whereEqualTo("uid", userId).get()
            .addOnSuccessListener { query ->
                if (!query.isEmpty) {
                    for (document in query) {
                        val docId = document.id
                        db.collection("users").document(docId)
                            .update("name", newDisplayName)
                            .addOnSuccessListener {
                                binding.btnUpload.text = "Success"
                            }
                            .addOnFailureListener {
                                binding.btnUpload.text = "Failed"

                            }
                    }
                } else {
                    // Handle no user found case
                    binding.btnUpload.text = "No User Found"
                }
            }
            .addOnFailureListener {
                binding.btnUpload.text = "Failed to Query"

            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }
}
