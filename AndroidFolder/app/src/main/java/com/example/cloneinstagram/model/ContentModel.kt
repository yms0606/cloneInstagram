package com.example.cloneinstagram.model

import com.facebook.internal.Mutable

data class ContentModel (
    var explain : String? = null, //  사진 설명
    var imageurl : String? = null, // 사진 다운로드 주소
    var uid : String? = null, // Following, Follow
    var userId : String? = null, //  이메일
    var timestamp : Long? = null, // 올린 시간
    var favoriteCount : Int = 0, // 좋아요 수
    var favorites : MutableMap<String,Boolean> = HashMap() // 좋아요 누른 사람

)

