package com.example.cloneinstagram.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cloneinstagram.R
import com.example.cloneinstagram.databinding.FragmentDetailViewBinding
import com.example.cloneinstagram.databinding.ItemDetailBinding
import com.example.cloneinstagram.model.ContentModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DetailViewFragment : Fragment() {
    lateinit var binding : FragmentDetailViewBinding
    val firestore : FirebaseFirestore by lazy{
        FirebaseFirestore.getInstance()
    }
    lateinit var uid : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail_view,container,false)
        binding.detailviewRecyclerview.adapter = DetailviewRecyclerviewAdapter()
        binding.detailviewRecyclerview.layoutManager = LinearLayoutManager(activity)
        //firestore = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().uid!!
        return binding.root
    }
    inner class DetailViewHolder(var binding : ItemDetailBinding) : RecyclerView.ViewHolder(binding.root)
    // 커스텀 뷰 홀더 제작
    inner class DetailviewRecyclerviewAdapter() : RecyclerView.Adapter<DetailViewHolder>() {

        var contentModels = arrayListOf<ContentModel>()
        var contentUids = arrayListOf<String>()

        init{
            //데이터 불러오는 코드
            //데이터를 계속 지켜보는 것, snapshot -> 리소스를 많이 사용 -> 서버비용 증가
            //한 번만 데이터를 읽어드리는 것, get
            firestore.collection("images").addSnapshotListener { value, error ->
                contentModels.clear()
                contentUids.clear()
                for(item in value!!.documents){
                    var contentModel = item.toObject(ContentModel::class.java)
                    contentModels.add(contentModel!!)
                    contentUids.add(item.id)
                }
                notifyDataSetChanged()
            }
            
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
            // 행 하나에 어떤 디자인의 xml을 넣을 지 설정하는 코드
            var view = ItemDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return DetailViewHolder(view)
        }

        override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
            // 데이터를 바인딩
            // viewholder에 커스텀 리스트 속 속성을 채우는 함수
            var contentModel = contentModels.get(position)
            var viewHolder = holder.binding
            viewHolder.profileTextview.text = contentModel.userId
            viewHolder.likeTextview.text = "Likes " + contentModel.favoriteCount
            viewHolder.displayTextview.text = contentModel.explain
            viewHolder.favoriteImageview.setOnClickListener {
                eventFavorite(position)
            }
            if(contentModel.favorites.containsKey(uid)){
                viewHolder.favoriteImageview.setImageResource(R.drawable.ic_favorite)
            }
            else{
                viewHolder.favoriteImageview.setImageResource(R.drawable.ic_favorite_border)
            }
            Glide.with(holder.itemView.context).load(contentModel.imageurl).into(viewHolder.contentImageview)

        }

        override fun getItemCount(): Int {
            // 행이 몇 개인지 관리하는 함수
            return contentModels.size
        }

        fun eventFavorite(position : Int){
            var docId = contentUids[position] // = .get(position)
            var tsDoc = firestore.collection("images").document(docId)
            firestore.runTransaction{
                transition->
                var contentDTO = transition.get(tsDoc).toObject(ContentModel :: class.java)
                if(contentDTO!!.favorites.containsKey(uid)){
                    //좋아요 누른 상태
                    contentDTO.favorites.remove(uid)
                    contentDTO.favoriteCount --
                }
                else{
                    contentDTO.favoriteCount ++
                    contentDTO.favorites[uid] = true

                }
                transition.set(tsDoc,contentDTO)
            }

        }

    }

}