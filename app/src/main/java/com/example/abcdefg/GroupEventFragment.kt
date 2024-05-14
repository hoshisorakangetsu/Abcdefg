package com.example.abcdefg

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.FirestoreDateTimeFormatter
import com.example.abcdefg.databinding.FragmentEventCardHeroBinding
import com.example.abcdefg.databinding.FragmentGroupEventBinding
import com.example.abcdefg.databinding.FragmentMoreEventCardBinding
import com.example.abcdefg.viewmodels.GroupViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import kotlin.math.abs

class GroupEventHeroAdapter(var events: ArrayList<DocumentSnapshot>, private val onMoreDetailsClickedListener: MoreDetailsClickedListener): RecyclerView.Adapter<GroupEventHeroAdapter.ViewHolder>() {

    fun interface MoreDetailsClickedListener {
        fun onMoreDetailsClicked(data: DocumentSnapshot)
    }

    class ViewHolder(private val binding: FragmentEventCardHeroBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(_data: DocumentSnapshot, onMoreDetailsClickListener: MoreDetailsClickedListener) {
            Log.d("GroupEventFragment", _data.toString())
            _data.toObject(Event::class.java).let { data ->
                Log.d("GroupEventFragment", data!!.toString())
                binding.tvEventName.text = data.name
                binding.tvEventTime.text = SimpleDateFormat("dd MMM yyyy").format(FirestoreDateTimeFormatter.DateFormatter.parse(data.eventDate)!!)
                binding.tvJoinCount.text = "${data.joinedBy.size} joining"

                binding.btnViewEventDetails.setOnClickListener {
                    onMoreDetailsClickListener.onMoreDetailsClicked(_data)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentEventCardHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position], onMoreDetailsClickedListener)
    }

}

// create custom scroll method such that the children are scaled based on distance from center
class CenterScaleUpLayoutManager(
    context: Context,
    private val minScaleDistanceFactor: Float = 1.5f,
    private val scaleDownBy: Float = 0.3f
) : LinearLayoutManager(context, HORIZONTAL, false) {

    override fun onLayoutCompleted(state: RecyclerView.State?) =
        super.onLayoutCompleted(state).also { scaleChildren() }

    override fun scrollHorizontallyBy(
        dx: Int,
        recycler: RecyclerView.Recycler,
        state: RecyclerView.State
    ) = super.scrollHorizontallyBy(dx, recycler, state).also {
        if (orientation == HORIZONTAL) scaleChildren()
    }

    // magic formula to calculate scale of children as they move across
    private fun scaleChildren() {
        val containerCenter = width / 2f

        val scaleDistanceThreshold = minScaleDistanceFactor * containerCenter

        for (i in 0 until childCount) {
            val child = getChildAt(i)!!

            val childCenter = (child.left + child.right) / 2f
            val distanceToCenter = abs(childCenter - containerCenter)

            val scaleDownAmount = (distanceToCenter / scaleDistanceThreshold).coerceAtMost(1f)
            val scale = 1f - scaleDownBy * scaleDownAmount

            // change scale based on distance from center via magic formula
            child.scaleX = scale
            child.scaleY = scale

            // retain the original spacing (spacing is scaled together as well, translate back)
            val translationDirection = if (childCenter > containerCenter) -1 else 1
            val translationXFromScale = translationDirection * child.width * (1 - scale) / 2f
            child.translationX = translationXFromScale
        }
    }
}

class GroupEventCardAdapter(var events: ArrayList<DocumentSnapshot>, private val onJointEventCardClickedListener: OnJointEventCardClickedListener): RecyclerView.Adapter<GroupEventCardAdapter.ViewHolder>() {

    fun interface OnJointEventCardClickedListener {
        fun onJointEventCardClicked(event: DocumentSnapshot)
    }

    class ViewHolder(private val binding: FragmentMoreEventCardBinding, private val onJointEventCardClickedListener: OnJointEventCardClickedListener): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(_data: DocumentSnapshot) {
            val data = _data.toObject(Event::class.java)!!
            binding.tvEventName.text = data.name
            binding.tvEventTime.text = SimpleDateFormat("dd MMM yyyy").format(FirestoreDateTimeFormatter.DateFormatter.parse(data.eventDate)!!)

            binding.root.setOnClickListener {
                onJointEventCardClickedListener.onJointEventCardClicked(_data)
            }
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentMoreEventCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onJointEventCardClickedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position])
    }

}

class GroupEventFragment : Fragment() {

    private lateinit var binding: FragmentGroupEventBinding
    private lateinit var auth: FirebaseAuth
    private val eventHeroes: ArrayList<DocumentSnapshot> = arrayListOf()
    private val jointEvents: ArrayList<DocumentSnapshot> = arrayListOf()
    private val groupViewModel: GroupViewModel by activityViewModels()
    private val snapshotListeners: ArrayList<ListenerRegistration> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupEventBinding.inflate(inflater, container, false)

        val evHeroAdapter = GroupEventHeroAdapter(eventHeroes) {
            openBottomSheetWithData(it)
        }
        populateEventData(groupViewModel.activeGroupId.value!!, evHeroAdapter)
        binding.rvEventHeroes.adapter = evHeroAdapter
        // override the layout manager with custom scroll method
        binding.rvEventHeroes.layoutManager = CenterScaleUpLayoutManager(requireContext())

        // for snapping
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvEventHeroes)

        val eventCardAdapter = GroupEventCardAdapter(jointEvents) {
            openBottomSheetWithData(it)
        }
        populateJointEventData(groupViewModel.activeGroupId.value!!, eventCardAdapter)
        groupViewModel.activeGroupId.observe(viewLifecycleOwner) {
            snapshotListeners.forEach { lr ->
                lr.remove()
            }
            snapshotListeners.clear()
            populateEventData(it, evHeroAdapter)
            populateJointEventData(it, eventCardAdapter)
        }
        binding.rvMoreEvents.adapter = eventCardAdapter

        return binding.root
    }

    private fun openBottomSheetWithData(data: DocumentSnapshot) {
        EventDetailFragment(data.id).show(parentFragmentManager, "Event Detail")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateEventData(groupId: String, adapter: GroupEventHeroAdapter) {
        val db = Firebase.firestore
        db.collection("events").whereEqualTo("groupId", groupId).get().addOnSuccessListener {
            eventHeroes.clear()
            it.documents.forEach { doc ->
                eventHeroes.add(doc)
            }
            binding.txtNoEvents.visibility = if (eventHeroes.size <= 0) View.VISIBLE else View.GONE
            adapter.events = eventHeroes
            adapter.notifyDataSetChanged()
        }

        snapshotListeners.add(db.collection("events").whereEqualTo("groupId", groupId).addSnapshotListener { values, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to event list change")
                return@addSnapshotListener
            }
            eventHeroes.clear()
            values!!.documents.forEach { doc ->
                eventHeroes.add(doc)
            }
            binding.txtNoEvents.visibility = if (eventHeroes.size <= 0) View.VISIBLE else View.GONE
            adapter.events = eventHeroes
            adapter.notifyDataSetChanged()
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateJointEventData(groupId: String, adapter: GroupEventCardAdapter) {
        val db = Firebase.firestore
        db.collection("events").where(Filter.and(
            Filter.equalTo("groupId", groupId),
            Filter.arrayContains("joinedBy", auth.uid!!)
        )).get().addOnSuccessListener {
            jointEvents.clear()
            it.documents.forEach { doc ->
                jointEvents.add(doc)
            }
            binding.txtNoJointEvents.visibility = if (jointEvents.size <= 0) View.VISIBLE else View.GONE
            adapter.events = jointEvents
            adapter.notifyDataSetChanged()
        }

        snapshotListeners.add(db.collection("events").where(Filter.and(
            Filter.equalTo("groupId", groupId),
            Filter.arrayContains("joinedBy", auth.uid!!)
        )).addSnapshotListener { values, error ->
            if (error != null) {
                Log.d("E", "Failed to listen to joint event list change")
                return@addSnapshotListener
            }
            jointEvents.clear()
            values!!.documents.forEach { doc ->
                jointEvents.add(doc)
            }
            binding.txtNoJointEvents.visibility = if (jointEvents.size <= 0) View.VISIBLE else View.GONE
            adapter.events = jointEvents
            adapter.notifyDataSetChanged()
        })
    }
}