package com.kinsey.passwords.items

import android.arch.lifecycle.ViewModel


// UserProfileViewModel
class UserProfileViewModel(
        savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userId : String = savedStateHandle["uid"] ?:
    throw IllegalArgumentException("missing user id")
    val user : User = TODO()
}

// UserProfileFragment
private val viewModel: UserProfileViewModel by viewModels(
        factoryProducer = { SavedStateVMFactory(this) }
        ...
)