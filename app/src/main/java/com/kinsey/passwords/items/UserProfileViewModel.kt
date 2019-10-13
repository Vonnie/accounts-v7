package com.kinsey.passwords.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel


class UserProfileViewModel(
//        private val githubApi: GithubApi,
        private val savedStateHandle: SavedStateHandle
) : ViewModel() {
//        , Parcelable {
    val userId : String = this.savedStateHandle["uid"] ?:
        throw IllegalArgumentException("missing user id")

    val user : LiveData<User> = TODO()
//        get() = TODO()

    fun loadData() {
        val id = this.savedStateHandle["id"] ?: "default"
    }
}

//// UserProfileFragment
//private val viewModel: UserProfileViewModel by viewModels(
//        factoryProducer = { SavedStateVMFactory(this) }
//        ...
//)