package com.akash.social.model

 class UserModel{
     private var uid : String=""
     private var username : String =""
     private var bio : String = ""
     private var imageUrl : String =""

     constructor()
     constructor(uid: String, username: String, bio: String, imageUrl: String) {
         this.uid = uid
         this.username = username
         this.bio = bio
         this.imageUrl = imageUrl
     }

     fun getUid():String{
         return uid
     }
     fun getUsername():String{
         return username
     }
     fun getBio():String{
         return bio
     }
     fun getImageUrl():String{
         return imageUrl
     }

 }

