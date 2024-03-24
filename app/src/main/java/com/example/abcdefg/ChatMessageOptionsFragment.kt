package com.example.abcdefg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abcdefg.data.ChatMessage
import com.example.abcdefg.databinding.FragmentChatMessageOptionsBinding
import com.example.abcdefg.databinding.FragmentGroupCardBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

enum class ChatMessageAction { EDIT, DELETE }
fun interface ChatMessageActionClickListener {
    fun onChatMessageActionClicked(chatMessage: ChatMessage, action: ChatMessageAction)
}

class ChatMessageOptionsFragment(private val actionClickListener: ChatMessageActionClickListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChatMessageOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatMessageOptionsBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_chat_message_options, container, false)
    }
}