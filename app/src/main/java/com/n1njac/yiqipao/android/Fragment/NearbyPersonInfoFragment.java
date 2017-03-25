package com.n1njac.yiqipao.android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.nearybyview.NearbyMainActivity;

/**
 * Created by huanglei on 2017/1/12.
 */

public class NearbyPersonInfoFragment extends Fragment {

    private Button mStart;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearby_frag,container,false);
        mStart = (Button) view.findViewById(R.id.start_nearby_btn);
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NearbyMainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
