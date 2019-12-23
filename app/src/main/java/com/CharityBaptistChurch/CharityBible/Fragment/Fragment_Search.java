package com.CharityBaptistChurch.CharityBible.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.CharityBaptistChurch.CharityBible.R;

public class Fragment_Search extends Fragment {

    View m_View;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_View = inflater.inflate(R.layout.fragment_search, container, false);
     //   ListView listview = m_View.findViewById(R.id.list);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return m_View;
    }
}
