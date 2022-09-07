package com.example.cloneinstagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.cloneinstagram.R
import com.example.cloneinstagram.databinding.FragmentAlarmBinding
import com.example.cloneinstagram.databinding.FragmentDetailViewBinding


class AlarmFragment : Fragment() {
    lateinit var binding : FragmentAlarmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_alarm,container,false)
        return binding.root
    }


}