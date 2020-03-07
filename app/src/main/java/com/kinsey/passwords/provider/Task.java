package com.kinsey.passwords.provider;

import com.kinsey.passwords.items.Profile;

public interface Task {
    default void processInsert(Profile profile) {

    }
    default void processInsertComplete(Profile profile) {

    }

}
