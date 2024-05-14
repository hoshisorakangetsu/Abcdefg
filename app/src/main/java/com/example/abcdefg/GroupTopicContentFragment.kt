package com.example.abcdefg

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Reply
import com.example.abcdefg.data.Topic
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentGroupTopicContentBinding
import com.example.abcdefg.databinding.FragmentTopicReplyItemTimelineBinding
import com.example.abcdefg.viewmodels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date

class TopicReplyAdapter(var replies: ArrayList<DocumentSnapshot>) :
    RecyclerView.Adapter<TopicReplyAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_TOP = 0
        const val VIEW_TYPE_MIDDLE = 1
        const val VIEW_TYPE_BOTTOM = 2
    }

    class ViewHolder(private val binding: FragmentTopicReplyItemTimelineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(_data: DocumentSnapshot, viewType: Int) {
            val data = _data.toObject(Reply::class.java)!!
            when (viewType) {
                VIEW_TYPE_TOP -> {
                    val displayDensity = binding.root.resources.displayMetrics.density
                    val addPadding = (24 * displayDensity + 0.5f).toInt()
                    val llPadTop = binding.llContent.paddingTop
                    val llPadLeft = binding.llContent.paddingLeft
                    val llPadRight = binding.llContent.paddingRight
                    val llPadBot = binding.llContent.paddingBottom
                    binding.llContent.setPadding(llPadLeft, llPadTop + addPadding, llPadRight, llPadBot)

                    val vmlp = FrameLayout.LayoutParams(binding.circleIndicator.layoutParams)
                    val ciMarLeft = binding.circleIndicator.marginLeft
                    val ciMarRight = binding.circleIndicator.marginRight
                    val ciMarTop = binding.circleIndicator.marginTop
                    val ciMarBot = binding.circleIndicator.marginBottom
                    vmlp.setMargins(ciMarLeft, ciMarTop + addPadding, ciMarRight, ciMarBot)
                    binding.circleIndicator.layoutParams = vmlp
                }
            }

            val db = Firebase.firestore
            db.collection("users").whereEqualTo("uid", data.createdBy).get().addOnSuccessListener {
                binding.tvUsername.text = it.documents[0]?.get("name").toString()
            }
            binding.tvDateTime.text = SimpleDateFormat("dd MMM yyyy HH:MM").format((data.createdAt as Timestamp).toDate())
            binding.tvTopicReplyContent.text = data.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentTopicReplyItemTimelineBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return replies.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(replies[position], getItemViewType(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_TOP;
        } else if (position == (replies.size - 1)) {
            return VIEW_TYPE_BOTTOM;
        }
        return VIEW_TYPE_MIDDLE;
    }

}

class GroupTopicContentFragment : Fragment() {

    private lateinit var binding: FragmentGroupTopicContentBinding
    private val topicReply: ArrayList<DocumentSnapshot> = arrayListOf()
    private val groupViewModel: GroupViewModel by activityViewModels()
    private var replyListener: ListenerRegistration? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupTopicContentBinding.inflate(inflater, container, false)

        // setup recyclerview for the replies
        val topicReplyAdapter = TopicReplyAdapter(topicReply)
        binding.rvTopicReply.adapter = topicReplyAdapter

        populateTopicData(groupViewModel.activeTopicId.value!!)
        populateData(groupViewModel.activeTopicId.value!!, topicReplyAdapter)
        groupViewModel.activeTopicId.observe(viewLifecycleOwner) {
            replyListener?.remove()
            populateTopicData(groupViewModel.activeTopicId.value!!)
            populateData(it, topicReplyAdapter)
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    fun populateTopicData(topicId: String) {
        val db = Firebase.firestore
        db.collection("groupTopics").document(topicId).get().addOnSuccessListener {
            val data = it.toObject(Topic::class.java)!!
            db.collection("users").whereEqualTo("uid", data.createdBy).get().addOnSuccessListener { query ->
                // is always unique so at most will got 1
                binding.tvUsername.text = query.documents[0]?.get("name").toString()
            }
            binding.tvTopicTitle.text = data.title
            binding.tvTopicContent.text = data.content
            binding.tvDateTime.text = SimpleDateFormat("dd MMM yyyy HH:MM").format((data.createdAt as Timestamp).toDate())
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun populateData(topicId: String, adapter: TopicReplyAdapter) {
        val db = Firebase.firestore
        db.collection("topicReplies").whereEqualTo("topicId", topicId).get().addOnSuccessListener {
            topicReply.clear()
            it.documents.forEach { doc ->
                topicReply.add(doc)
            }
            adapter.replies = topicReply
            binding.tvReplyCount.text = "%d replies".format(topicReply.size)
            adapter.notifyDataSetChanged()
        }

        replyListener = db.collection("topicReplies").whereEqualTo("topicId", topicId).addSnapshotListener { values, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to chat message list change")
                return@addSnapshotListener
            }
            topicReply.clear()
            values!!.documents.forEach { doc ->
                topicReply.add(doc)
            }
            adapter.replies = topicReply
            binding.tvReplyCount.text = "%d replies".format(topicReply.size)
            adapter.notifyDataSetChanged()
        }
    }

}