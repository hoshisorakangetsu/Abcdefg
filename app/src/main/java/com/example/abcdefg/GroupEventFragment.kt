package com.example.abcdefg

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.abcdefg.data.Event
import com.example.abcdefg.data.User
import com.example.abcdefg.databinding.FragmentEventCardHeroBinding
import com.example.abcdefg.databinding.FragmentGroupEventBinding
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.abs

class GroupEventHeroAdapter(private val events: ArrayList<Event>): RecyclerView.Adapter<GroupEventHeroAdapter.ViewHolder>() {

    class ViewHolder(private val binding: FragmentEventCardHeroBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(data: Event) {
            binding.tvEventName.text = data.name
            binding.tvEventTime.text = SimpleDateFormat("HH:MM").format(data.eventDate)
            binding.tvJoinCount.text = "${data.joinedBy.size} joining"
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
        holder.bind(events[position])
    }

}

// create custom scroll method such that the children are scaled based on distance from center
class CenterScaleUpLayoutManager(
    context: Context,
    private val minScaleDistanceFactor: Float = 1.5f,
    private val scaleDownBy: Float = 0.4f
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

class GroupEventFragment : Fragment() {

    private lateinit var binding: FragmentGroupEventBinding
    private lateinit var eventHeroes: ArrayList<Event>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGroupEventBinding.inflate(inflater, container, false)

        eventHeroes = getData()
        val evHeroAdapter = GroupEventHeroAdapter(eventHeroes)
        binding.rvEventHeroes.adapter = evHeroAdapter
        // override the layout manager with custom scroll method
        binding.rvEventHeroes.layoutManager = CenterScaleUpLayoutManager(requireContext())

        // for snapping
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvEventHeroes)

        return binding.root
    }

    // TODO review this
    private fun getData(): ArrayList<Event> {
        val users = arrayOf(
            User("1", "Alice"),
            User("2", "Bob"),
            User("3", "Charlie")
        )

        val events = arrayListOf<Event>()

        for (i in 1..5) {
            val eventUsers = mutableListOf<User>()
            val numAttendees = (1..users.size).random() // Random number of attendees
            for (j in 1..numAttendees) {
                val user = users.random()
                eventUsers.add(user)
            }
            val event = Event(
                "Event $i",
                "Description of Event $i",
                Date(),
                eventUsers.toTypedArray()
            )
            events.add(event)
        }
        return events
    }
}