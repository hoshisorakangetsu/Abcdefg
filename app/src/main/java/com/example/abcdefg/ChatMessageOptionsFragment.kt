package com.example.abcdefg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abcdefg.data.ChatMessage
import com.example.abcdefg.databinding.FragmentChatMessageOptionsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun interface ChatMessageActionClickListener {
    fun onChatMessageActionDelete(chatMessageId: String)
}

class ChatMessageOptionsFragment(private val msgId: String, private val actionClickListener: ChatMessageActionClickListener) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentChatMessageOptionsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatMessageOptionsBinding.inflate(inflater, container, false)
        binding.deleteMsg.setOnClickListener {
            actionClickListener.onChatMessageActionDelete(msgId)
        }
        return inflater.inflate(R.layout.fragment_chat_message_options, container, false)
    }
}