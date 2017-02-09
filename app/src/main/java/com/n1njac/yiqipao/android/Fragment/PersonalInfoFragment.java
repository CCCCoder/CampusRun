package com.n1njac.yiqipao.android.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.personalinfo.PersonalInfoAdapter;
import com.n1njac.yiqipao.android.personalinfo.PersonalItemBean;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huanglei on 2017/2/2.
 */

public class PersonalInfoFragment extends Fragment {

    private static int COUNT = 0;
    private List<PersonalItemBean> mPersonalItemBeanList = new ArrayList<>();
    private PersonalInfoAdapter mAdapter;
    private ListView listView;
    private CircleImageView circleImageView;
    private TextView personalId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_frag, container, false);
        listView = (ListView) view.findViewById(R.id.personal_info_lv);
        circleImageView = (CircleImageView) view.findViewById(R.id.icon_personal_info);
        personalId = (TextView) view.findViewById(R.id.personal_info_id_tx);
        if (COUNT == 0) {
            initList();
        }
        mAdapter = new PersonalInfoAdapter(getActivity(), mPersonalItemBeanList);
        listView.setAdapter(mAdapter);
        return view;
    }

    private void initList() {

        String[] descriptions = new String[]{"昵称", "性别", "出生年月", "身高", "体重"};
        String[] content = new String[]{"N1njaC", "男", "1993年7月", "170厘米", "120.0斤"};
        PersonalItemBean itemBean;
        for (int i = 0; i < 5; i++) {
            itemBean = new PersonalItemBean(descriptions[i], content[i]);
            mPersonalItemBeanList.add(itemBean);
        }
        COUNT++;
    }
}
