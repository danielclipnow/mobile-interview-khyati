package com.example.interviewtemplateupload

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform