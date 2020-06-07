package com.example.videorecordingapplication.data.localdatasource

import com.example.videorecordingapplication.data.entity.*

object DataSource {

    val LOG_TAG = "JujuPlusTag"

    fun getVideoList() = ArrayList<VideoEntity>().apply {
        add(VideoEntity(5,
            "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            0,"","active",0,"By Blender Foundation",
            " https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/BigBuckBunny.jpg",
        "Big Buck Bunny",1))

        add(VideoEntity(8,"Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            0, "","active",0,"By Blender Foundation",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ElephantsDream.jpg",
            "Elephant Dream",2))


        add(VideoEntity(6,"Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            0,"","active",0,"By Blender Foundation",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerBlazes.jpg",
            "For Bigger Blazes",3))


        add(VideoEntity(7,"Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
            0,"","active",0,"By Blender Foundation",
            " https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerFun.jpg",
            "For Bigger Escape",4))

        add(VideoEntity(9,"Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.\\n\\nLicensed under the Creative Commons Attribution license\\nhttp://www.bigbuckbunny.org",
            "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            0,"","active",0,"By Blender Foundation",
           "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/images/ForBiggerJoyrides.jpg",
            "For Bigger Fun",5))
    }

    fun getVideoEntityList() = ArrayList<VideoListEntity>().apply {
        add(VideoListEntity(1, "Art", getVideoList()))
        add(VideoListEntity(2, "Math", getVideoList()))
    }

    fun getSchoolList() = ArrayList<SchoolEntity>().apply {
        add(SchoolEntity("", "School1", 1 ))
        add(SchoolEntity("", "School2", 2 ))
        add(SchoolEntity("", "School3", 3 ))
        add(SchoolEntity("", "School4", 4 ))
        add(SchoolEntity("", "School5", 5 ))
        add(SchoolEntity("", "School6", 6 ))
        add(SchoolEntity("", "School7", 7 ))
        add(SchoolEntity("", "School8", 8 ))
    }

    fun getSchoolNameArray() = arrayOf("DAV School", "DPS", "Convent School", "Army Public School")

    fun getFriendList() = ArrayList<SchoolEntity>().apply {
        add(SchoolEntity("", "Neha", 1 ))
        add(SchoolEntity("", "Aditya", 2 ))
        add(SchoolEntity("", "Sumit", 3 ))
        add(SchoolEntity("", "Priya", 4 ))
        add(SchoolEntity("", "Sneha", 5 ))
    }
}