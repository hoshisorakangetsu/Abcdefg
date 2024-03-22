package com.example.abcdefg

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.ChatMessage
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentChatMessageBinding
import com.example.abcdefg.databinding.FragmentGroupChatBinding
import java.time.LocalDateTime
import java.util.Date

class ChatAdapter(private val dataSet: Array<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(private val cmBinding: FragmentChatMessageBinding) : RecyclerView.ViewHolder(cmBinding.root) {
        fun bind(chatMessage: ChatMessage) {
            // populate the chat message fragment here ***eNcApSuLaTiOn***
            cmBinding.tvUsername.text = chatMessage.sentBy.name
            cmBinding.tvMessageContent.text = chatMessage.content
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val cmBinding: FragmentChatMessageBinding = FragmentChatMessageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(cmBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}

class GroupChatFragment : Fragment() {

    private lateinit var binding: FragmentGroupChatBinding

    // temp TODO replace with firebase data
    private val chatMessages: Array<ChatMessage> = arrayOf(
        ChatMessage("0", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam tincidunt elit dolor. Integer varius pretium velit at fringilla. Aliquam neque orci, efficitur in est id.", User("0", "User 1"), Date()),
        ChatMessage("1", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam tincidunt elit dolor. Integer varius pretium velit at fringilla. Aliquam neque orci, efficitur in est id.", User("1", "User 2"), Date()),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)

        // enable smooth move up when bottom nav expands
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val chatMessageAdapter = ChatAdapter(chatMessages)
        binding.rvChatList.adapter = chatMessageAdapter

        return binding.root
    }
}