package com.yan.tiaoshizhushou.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yan.tiaoshizhushou.R;

/**
 * Created by a7501 on 2016/1/26.
 */
public class NotesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_fragment,container,false);

        return view;
    }
}
