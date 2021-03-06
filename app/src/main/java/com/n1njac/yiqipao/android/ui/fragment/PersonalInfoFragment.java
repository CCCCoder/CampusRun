package com.n1njac.yiqipao.android.ui.fragment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.PersonInfoBmob;
import com.n1njac.yiqipao.android.adapter.PersonalInfoAdapter;
import com.n1njac.yiqipao.android.bean.PersonalItemBean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by huanglei on 2017/2/2.
 */

public class PersonalInfoFragment extends Fragment {


    private List<PersonalItemBean> mPersonalItemBeanList = new ArrayList<>();
    private PersonalInfoAdapter mAdapter;
    private ListView listView;
    private CircleImageView circleImageView;
    private TextView personalId;

    PersonInfoBmob mPersonInfoBmob = new PersonInfoBmob();
    private String bmobObjectId = null;

    private SharedPreferences sf;
    private SharedPreferences.Editor editor;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();

        sf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

        int status = sf.getInt("isCreateTablePersonInfo", 0);
        Log.d("xyz", "isCreateTablePersonInfo:" + status);
        if (status != 1) {
            mPersonInfoBmob.setInitNum(1);
            mPersonInfoBmob.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    bmobObjectId = s;
                    editor.putString("bmobObjectId", bmobObjectId);
                    Log.d("xyz", "objectId:" + s);
                    editor.putInt("isCreateTablePersonInfo", 1);
                    editor.apply();
                }
            });
        }

//        mPushBmob.setInitNum(1);
//        mPushBmob.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                pushBmobObjectId = s;
//                Log.d("xyz", "pushBmobObjectId:" + s);
//            }
//        });

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_frag, container, false);
        listView = (ListView) view.findViewById(R.id.personal_info_lv);
        circleImageView = (CircleImageView) view.findViewById(R.id.icon_personal_info);
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String iconUri = spf.getString("iconUri", null);

        if (iconUri != null) {
            Uri uri = Uri.parse(iconUri);
            Glide.with(getActivity()).load(uri).centerCrop().into(circleImageView);
        }
        personalId = (TextView) view.findViewById(R.id.personal_info_id_tx);

        mAdapter = new PersonalInfoAdapter(getActivity(), mPersonalItemBeanList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final TextView personalContent = (TextView) view.findViewById(R.id.content_tx);
                final SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getActivity());
                final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                switch (position) {

                    case 0:
                        final EditText et = new EditText(getActivity());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("修改昵称")
                                .setView(et)
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String input = et.getText().toString();
                                        if (input.equals("")) {
                                            Toast.makeText(getActivity(), "内容不能为空哦：）", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //需要将信息保存到sf中
                                            personalContent.setText(input);
                                            editor.putString("user_nickname", input);
                                            editor.apply();
                                            String obId = spf.getString("bmobObjectId",null);
                                            //将数据保存到服务器数据库。
                                            mPersonInfoBmob.setNickName(input);
                                            mPersonInfoBmob.update(obId, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("取消", null)
                                .show();

                        break;
                    case 1:
                        final CharSequence[] charSequences = new CharSequence[]{"男", "女"};
                        new AlertDialog.Builder(getActivity())
                                .setTitle("性别")
                                .setSingleChoiceItems(charSequences, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                personalContent.setText(charSequences[0]);
                                                editor.putString("sex", charSequences[0] + "");
                                                editor.apply();
                                                mPersonInfoBmob.setSex("男");
                                                dialog.dismiss();
                                                break;
                                            case 1:
                                                personalContent.setText(charSequences[1]);
                                                editor.putString("sex", charSequences[1] + "");
                                                editor.apply();
                                                mPersonInfoBmob.setSex("女");
                                                dialog.dismiss();
                                                break;
                                            default:
                                                break;

                                        }

                                        String obId = spf.getString("bmobObjectId",null);
                                        mPersonInfoBmob.update(obId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setCancelable(true)
                                .show();

                        break;
                    case 2:
                        View view2 = View.inflate(getActivity(), R.layout.dialog_datepicker, null);
                        final DatePicker datePicker = (DatePicker) view2.findViewById(R.id.date_picker);
                        new AlertDialog.Builder(getActivity())
                                .setView(view2)
                                .setTitle("出生年月")
                                .setNegativeButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int month = datePicker.getMonth() + 1;
                                        int year = datePicker.getYear();
                                        String birth = year + "年" + month + "月";
                                        personalContent.setText(birth);
                                        editor.putString("birth", birth);
                                        editor.apply();

                                        mPersonInfoBmob.setBirth(birth);
                                        String obId = spf.getString("bmobObjectId",null);
                                        mPersonInfoBmob.update(obId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setCancelable(true)
                                .show();
                        break;
                    case 3:

                        View view1 = View.inflate(getActivity(), R.layout.dialog_numberpicker, null);
                        final NumberPicker numberPicker = (NumberPicker) view1.findViewById(R.id.number_picker);
                        final String[] lengths = new String[]{"150", "151", "152", "153", "154", "155", "156", "157", "158", "159",
                                "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173",
                                "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187",
                                "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200"};
                        numberPicker.setDisplayedValues(lengths);
                        numberPicker.setMaxValue(lengths.length - 1);
                        numberPicker.setMinValue(0);
                        new AlertDialog.Builder(getActivity())
                                .setTitle("身高（厘米）")
                                .setView(view1)
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String height = lengths[numberPicker.getValue()] + "厘米";
                                        personalContent.setText(height);
                                        editor.putString("height", height);
                                        editor.apply();

                                        String obId = spf.getString("bmobObjectId",null);
                                        mPersonInfoBmob.setHeight(height);
                                        mPersonInfoBmob.update(obId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                })
                                .setNegativeButton("取消", null)
                                .setCancelable(false)
                                .show();

                        break;
                    case 4:
                        final EditText et2 = new EditText(getActivity());
                        et2.setInputType(InputType.TYPE_CLASS_NUMBER);
                        new AlertDialog.Builder(getActivity())
                                .setTitle("体重（kg）")
                                .setView(et2)
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String input = et2.getText().toString();
                                        if (input.equals("")) {
                                            Toast.makeText(getActivity(), "内容不能为空哦：）", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //需要将信息保存到sf中
                                            String weight = input + "kg";
                                            personalContent.setText(weight);
                                            editor.putString("weight", weight);
                                            editor.apply();

                                            String obId = spf.getString("bmobObjectId",null);
                                            mPersonInfoBmob.setWeight(weight);
                                            mPersonInfoBmob.update(obId, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("取消", null)
                                .show();
                        break;
                    case 5:
                        View view3 = View.inflate(getActivity(), R.layout.personal_info_hobby, null);
                        final EditText hobbyEt = (EditText) view3.findViewById(R.id.hobby_et);
                        hobbyEt.setText(spf.getString("hobbyContent", null));
                        hobbyEt.setSelection(hobbyEt.getText().length());
                        new AlertDialog.Builder(getActivity())
                                .setView(view3)
                                .setTitle("兴趣爱好")
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String hobbyContent = hobbyEt.getText().toString();
                                        editor.putString("hobbyContent", hobbyContent);
                                        editor.apply();

                                        String obId = spf.getString("bmobObjectId",null);
                                        mPersonInfoBmob.setHobby(hobbyContent);
                                        mPersonInfoBmob.update(obId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .setCancelable(false)
                                .show();
                        break;

                    case 6:

                        View view4 = View.inflate(getActivity(), R.layout.personal_info_phone, null);
                        final EditText phoneEt = (EditText) view4.findViewById(R.id.phone_et);
                        phoneEt.setText(spf.getString("phone", null));
                        phoneEt.setSelection(phoneEt.getText().length());

                        new AlertDialog.Builder(getActivity())
                                .setTitle("手机号码")
                                .setView(view4)
                                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String input = phoneEt.getText().toString();
                                        if (input.equals("")) {
                                            Toast.makeText(getActivity(), "内容不能为空哦：）", Toast.LENGTH_SHORT).show();

                                        } else {
                                            //需要将信息保存到sf中
                                            String phoneNum = input;

                                            Log.d("xyz","phoneNum"+phoneNum);
                                            personalContent.setText(phoneNum);
                                            editor.putString("phone", phoneNum);
                                            editor.apply();

                                            String installationId = BmobInstallation.getInstallationId(getContext());
                                            mPersonInfoBmob.setInstallationId(installationId);
                                            mPersonInfoBmob.setPhone(phoneNum);
                                            mPersonInfoBmob.setDistance(0.28);
                                            String obId = spf.getString("bmobObjectId",null);
                                            mPersonInfoBmob.update(obId, new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(getActivity(), "更新成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.d("xyz", "phone:" + e.getMessage());
                                                        Toast.makeText(getActivity(), "连接服务器失败！请检查网络连接：）", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("取消", null)
                                .show();

                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

    private void initList() {

        String[] descriptions = new String[]{"昵称", "性别", "出生年月", "身高", "体重", "兴趣爱好", "手机号码"};
        String[] content = new String[]{"N1njaC", "男", "1993年7月", "170厘米", "62kg", "点击修改", "点击修改"};
        PersonalItemBean itemBean;
        for (int i = 0; i < 7; i++) {
            itemBean = new PersonalItemBean(descriptions[i], content[i]);
            mPersonalItemBeanList.add(itemBean);
        }

    }
}
