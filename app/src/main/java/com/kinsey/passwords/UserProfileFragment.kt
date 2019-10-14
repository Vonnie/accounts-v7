package com.kinsey.passwords

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kinsey.passwords.provider.UserProfileViewModel

class UserProfileFragment : androidx.fragment.app.Fragment() {

    // To use the viewModels() extension function, include
    // "androidx.fragment:fragment-ktx:latest-version" in your app
    // module's build.gradle file.
    private val viewModel: UserProfileViewModel? = null // by viewModels()


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.user.observe(viewLifecycleOwner) {
//            // update UI
//        }
    }
}
