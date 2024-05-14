package com.example.abcdefg

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Reply
import com.example.abcdefg.data.Topic
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentGroupTopicListBinding
import com.example.abcdefg.databinding.FragmentTopicCardBinding
import com.example.abcdefg.viewmodels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import java.text.SimpleDateFormat
import java.util.Date

class TopicListAdapter(private val data: ArrayList<DocumentSnapshot>, private val itemClickListener: TopicItemClickListener) : RecyclerView.Adapter<TopicListAdapter.ViewHolder>() {
    
    class ViewHolder(private val binding: FragmentTopicCardBinding): RecyclerView.ViewHolder(binding.root) {
        // le encapsulation
        @SuppressLint("SetTextI18n", "SimpleDateFormat") // annoying af
        fun bind(data: DocumentSnapshot, itemClickListener: TopicItemClickListener) {
            val dataTopic = data.toObject(Topic::class.java)!!
            binding.tvTopicTitle.text = dataTopic.title
            binding.tvTopicContent.text = dataTopic.content
            binding.tvDate.text = SimpleDateFormat("dd MMM yyyy").format(dataTopic.createdAt)
            // change this get from topicReplies collection
//            binding.tvReplyCount.text = "%d replies".format(dataTopic.replies.size)

            binding.root.setOnClickListener {
                itemClickListener.onTopicItemClicked(dataTopic)
            }
        }
    }

    fun interface TopicItemClickListener {
        abstract fun onTopicItemClicked(topicItem: Topic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentTopicCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], itemClickListener)
    }

}

class GroupTopicListFragment : Fragment() {

    private lateinit var binding: FragmentGroupTopicListBinding
    private val topicList: ArrayList<DocumentSnapshot> = arrayListOf()
    private val groupViewModel: GroupViewModel by activityViewModels()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // connect binding
        binding = FragmentGroupTopicListBinding.inflate(inflater, container, false)

        // attach the recycler view
        val topicListAdapter = TopicListAdapter(topicList) {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_fragmentGroupTopicList_to_fragmentGroupTopicContent)
            groupViewModel.navigateToNewFragment(GroupViewModel.Companion.GroupMainFragments.TOPIC_CONTENT)
        }
        binding.rvTopicList.adapter = topicListAdapter

        // Inflate the layout for this fragment
        return binding.root
    }
}