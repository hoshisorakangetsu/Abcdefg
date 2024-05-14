package com.example.abcdefg

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.abcdefg.databinding.ActivityCreateTopicBinding
import com.example.abcdefg.databinding.FragmentSetUsernameDialogBinding

class SetUsernameDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentSetUsernameDialogBinding
    private lateinit var listener: SetUsernameListener

    public fun interface SetUsernameListener {
        fun onUsernameSet(username: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = context as SetUsernameListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface. Throw exception.
            throw ClassCastException((context.toString() +
                    " must implement SetUsernameListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            // Use the Builder class for convenient dialog construction.
            val builder = AlertDialog.Builder(it)
            builder.setTitle("What should others call you?")

                .setPositiveButton("Call Me This") { dialog, id ->
                    // SET USERNAME!
                    listener.onUsernameSet(binding.etSetUsername.text.toString())
                }

            binding = FragmentSetUsernameDialogBinding.inflate(requireActivity().layoutInflater)
            builder.setView(binding.root)

            // Create the AlertDialog object and return it.
            val dialog = builder.create()
            dialog.setOnShowListener { _dialog ->
                (_dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = false
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}