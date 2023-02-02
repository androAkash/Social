package com.akash.social.model

class Post {
    private var postId: String =""
    private var postImage: String =""
    private var publisher: String =""
    private var description: String =""
    private var currentDate: String =""
    private var currentTime: String =""

    constructor()
    constructor(postId: String, postImage: String, publisher: String, description: String, date : String, time:String) {
        this.postId = postId
        this.postImage = postImage
        this.publisher = publisher
        this.description = description
        this.currentDate = date
        this.currentTime = time
    }

    fun getPostId():String{
        return postId
    }
    fun getPostImage():String{
        return postImage
    }
    fun getPublisher():String{
        return publisher
    }
    fun getDescription():String{
        return description
    }

    fun getDate():String{
        return currentDate
    }
    fun getTime():String{
        return currentTime
    }

    fun setPostId(postId:String){
        this.postId = postId
    }
    fun setPostImage(postImage:String){
        this.postImage = postImage
    }
    fun setPostDescription(postDesc:String){
        this.description = postDesc
    }
    fun setPublisher(publisher:String){
        this.publisher = publisher
    }

    fun setCurrentTime(currentTime: String){
        this.currentTime = currentTime
    }
    fun setCurrentDate(currentDate: String){
        this.currentDate = currentDate
    }
}