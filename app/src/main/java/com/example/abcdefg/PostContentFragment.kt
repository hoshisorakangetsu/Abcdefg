package com.example.abcdefg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abcdefg.databinding.FragmentPostContentBinding

class PostContentFragment : Fragment() {
    private lateinit var binding: FragmentPostContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostContentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}