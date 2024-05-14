package com.example.abcdefg

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.ChatMessage
import com.example.abcdefg.databinding.FragmentChatMessageBinding
import com.example.abcdefg.databinding.FragmentGroupChatBinding
import com.example.abcdefg.viewmodels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore

class ChatAdapter(
    var dataSet: ArrayList<DocumentSnapshot>,
    private val authId: String,
    private val messageLongPressListener: MessageLongPressListener
) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    fun interface MessageLongPressListener {
        fun onMessageLongPress(data: DocumentSnapshot)
    }

    class ViewHolder(private val cmBinding: FragmentChatMessageBinding) :
        RecyclerView.ViewHolder(cmBinding.root) {

        fun bind(
            data: DocumentSnapshot,
            authId: String,
            messageLongPressListener: MessageLongPressListener
        ) {
            val db = Firebase.firestore
            val chatMessage = data.toObject(ChatMessage::class.java)!!
            Log.d("ChatFragment", data.toString())
            db.collection("users").whereEqualTo("uid", chatMessage.sentBy).get()
                .addOnSuccessListener {
                    it.documents.forEach { doc ->
                        cmBinding.tvUsername.text = doc.get("name").toString()
                    }
                }
            cmBinding.tvMessageContent.text = chatMessage.content
            if (chatMessage.sentBy == authId) {
                cmBinding.cvChatMessage.setOnLongClickListener {
                    messageLongPressListener.onMessageLongPress(data)
                    // dont let other events consume this
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val cmBinding: FragmentChatMessageBinding = FragmentChatMessageBinding.inflate(
            LayoutInflater.from(viewGroup.context),
            viewGroup,
            false
        )
        return ViewHolder(cmBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], authId, messageLongPressListener)
    }

    override fun getItemCount() = dataSet.size

}

class GroupChatFragment : Fragment() {

    private lateinit var binding: FragmentGroupChatBinding
    private lateinit var auth: FirebaseAuth
    private val groupViewModel: GroupViewModel by activityViewModels()

    private val chatMessages: ArrayList<DocumentSnapshot> = arrayListOf()
    private var currentSnapshotListener: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupChatBinding.inflate(inflater, container, false)

        // enable smooth move up when bottom nav expands
        binding.root.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        val db = Firebase.firestore

        val chatMessageAdapter = ChatAdapter(chatMessages, auth.uid!!) {
            ChatMessageOptionsFragment(it.id) { msgId ->
                db.collection("chatMessages").document(msgId)
                    .delete()
                    .addOnSuccessListener {
                        Toast.makeText(
                            activity,
                            "Successfully deleted message",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }.show(parentFragmentManager, "chatMessageOptionBottomSheet")
        }

        populateChat(groupViewModel.activeGroupId.value!!, chatMessageAdapter)
        groupViewModel.activeGroupId.observe(viewLifecycleOwner) {
            currentSnapshotListener?.remove()
            populateChat(it, chatMessageAdapter)
        }

        binding.rvChatList.adapter = chatMessageAdapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateChat(activeGroupId: String, chatMessageAdapter: ChatAdapter) {
        val db = Firebase.firestore
        // fetch data for the first time
        Log.d("ChatFragment", groupViewModel.activeGroupId.value.toString())
        db.collection("chatMessages").whereEqualTo("groupId", activeGroupId)
            .orderBy("sentAt").get().addOnSuccessListener {
                chatMessages.clear()
                it.documents.forEach { doc ->
                    chatMessages.add(doc)
                }
                chatMessageAdapter.dataSet = chatMessages
                chatMessageAdapter.notifyDataSetChanged()
                binding.rvChatList.smoothScrollToPosition(if (chatMessages.size - 1 <= 0) 0 else chatMessages.size - 1)
            }.addOnFailureListener {
                Log.e("ChatFragment", it.toString())
            }
        // attach listener so it is updated everytime new chat message is created
        currentSnapshotListener = db
            .collection("chatMessages").whereEqualTo("groupId", groupViewModel.activeGroupId.value)
            .orderBy("sentAt").addSnapshotListener { value, error ->
                if (error != null) {
                    Log.d("E", "Failed to listen to chat message list change")
                    return@addSnapshotListener
                }
                chatMessages.clear()
                value!!.documents.forEach { doc ->
                    chatMessages.add(doc)
                }
                chatMessageAdapter.dataSet = chatMessages
                chatMessageAdapter.notifyDataSetChanged()
                binding.rvChatList.smoothScrollToPosition(if (chatMessages.size - 1 <= 0) 0 else chatMessages.size - 1)
            }
    }
}