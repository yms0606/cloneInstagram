package com.example.cloneinstagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.R
import com.example.cloneinstagram.databinding.FragmentDetailViewBinding


class DetailViewFragment : Fragment() {
    lateinit var binding : FragmentDetailViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_view,container,false)
        return binding.root
    }


}