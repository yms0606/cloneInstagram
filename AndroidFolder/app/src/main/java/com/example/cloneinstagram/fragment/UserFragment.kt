package com.example.cloneinstagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.databinding.FragmentDetailViewBinding
import com.example.cloneinstagram.databinding.FragmentUserBinding
import com.example.cloneinstagram.databinding.ItemImageviewBinding
import com.example.cloneinstagram.model.ContentModel
import com.google.common.collect.Table
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class UserFragment : Fragment() {
    lateinit var binding : FragmentUserBinding
    var dUid : String? = null
    val firestore : FirebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false)
        dUid = FirebaseAuth.getInstance().uid  // 나의 uid
        binding.accountRecyclerview.adapter = UserFragmentRecyclerviewAdapter()
        binding.accountRecyclerview.layoutManager = GridLayoutManager(activity,3)
        return binding.root
    }
    // recyclerview를 만들 때, 뷰홀더에 각 리스트 xml을 넣어 어댑터에서 반환
    // 즉 뷰홀더는 커스텀 페이지 뷰를 넣는다고 생각하면 됨
    // 그 후 recyclerview에 어댑터와 레이아웃 매니저 설정
    inner class CellImageViewHolder(val binding: ItemImageviewBinding) : RecyclerView.ViewHolder(binding.root)
    inner class UserFragmentRecyclerviewAdapter : RecyclerView.Adapter<CellImageViewHolder>() {

        var contentModels : ArrayList<ContentModel> = arrayListOf()

        init {
            firestore.collection("images").whereEqualTo("uid",dUid).addSnapshotListener { value, error ->
                // collection : firebase 데이터 베이스에 접근
                for(item in value!!.documents){
                    contentModels.add(item.toObject(ContentModel::class.java)!!)
                }
                binding.accountPostTextview.text = contentModels.size.toString()
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellImageViewHolder {
            var width = resources.displayMetrics.widthPixels/3
            var view = ItemImageviewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            view.cellImageview.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
            return CellImageViewHolder(view)
        }

        override fun onBindViewHolder(holder: CellImageViewHolder, position: Int) {
            var contentModel = contentModels[position]
            Glide.with(holder.itemView.context).load(contentModel.imageurl).into(holder.binding.cellImageview)
        }

        override fun getItemCount(): Int {
            return  contentModels.size
        }

    }

}