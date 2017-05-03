package com.kinsey.passwords.provider;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kinsey.passwords.items.MyDataObject;

/**
 * Created by Yvonne on 5/2/2017.
 */

public class RetainedFragment extends Fragment {

    // data object we want to retain
    private MyDataObject data;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(MyDataObject data) {
        this.data = data;
    }

    public MyDataObject getData() {
        return data;
    }


}


