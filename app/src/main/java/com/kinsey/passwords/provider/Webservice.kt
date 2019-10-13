package com.kinsey.passwords.provider

import android.telecom.Call

import com.kinsey.passwords.items.User

interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
//    @GET("/users/{user}")
//    fun getUser(@Path("user") userId: String): "vonnie" // Call<User>

    fun getUser(userId: String) {
//        return "vonnie"
    }

}

