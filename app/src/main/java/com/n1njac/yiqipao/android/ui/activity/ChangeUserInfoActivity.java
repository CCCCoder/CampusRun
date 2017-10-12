package com.n1njac.yiqipao.android.ui.activity;
/*    
 *    Created by N1njaC on 2017/10/11.
 *    email:aiai173cc@gmail.com 
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.NewUserInfoBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class ChangeUserInfoActivity extends BaseActivity {

    private static final String TAG = ChangeUserInfoActivity.class.getSimpleName();

    @BindView(R.id.nick_name_tv)
    TextView nickNameTv;
    @BindView(R.id.nick_name_rl)
    RelativeLayout nickNameRl;
    @BindView(R.id.sex_tv)
    TextView sexTv;
    @BindView(R.id.sex_rl)
    RelativeLayout sexRl;
    @BindView(R.id.height_tv)
    TextView heightTv;
    @BindView(R.id.height_rl)
    RelativeLayout heightRl;
    @BindView(R.id.weight_tv)
    TextView weightTv;
    @BindView(R.id.weight_rl)
    RelativeLayout weightRl;
    @BindView(R.id.change_user_back_btn)
    Button mBackBtn;
    @BindView(R.id.change_user_header_rl)
    RelativeLayout changeUserHeaderRl;
    @BindView(R.id.change_user_save_btn)
    Button mSaveChangeBtn;


    private String mNickName;
    //0:未设置 1:男 2:女
    private int mSex = 0;

    //=0的时候，文本设为未设置。处于未设置情况下选择器设置中间的数。
    private int mHeight = 0;

    private int mWeight = 0;

    private String[] sexs = new String[]{"未设置", "男", "女"};

    private ProgressDialog mProgressDialog;

    private String mUserInfoItemObjectId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_info_activity);
        ButterKnife.bind(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("加载中...");
        updateInfoFromServer();

    }

    //从服务器更新数据
    private void updateInfoFromServer() {
        mProgressDialog.show();

        BmobQuery<NewUserInfoBmob> query = new BmobQuery<>();
        String objectId = BmobUser.getCurrentUser(UserInfoBmob.class).getObjectId();
        query.addWhereEqualTo("pUserObjectId", objectId);
        query.findObjects(new FindListener<NewUserInfoBmob>() {
            @Override
            public void done(List<NewUserInfoBmob> list, BmobException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    Log.d(TAG, "info size=============>:" + list.size());
                    NewUserInfoBmob userInfo = list.get(0);
                    if (userInfo != null) {
                        mNickName = userInfo.getpNickName();
                        mSex = userInfo.getpSex();
                        mHeight = userInfo.getpHeight();
                        mWeight = userInfo.getpWeight();

                        Log.d(TAG, "sex:" + mSex);
                        Log.d(TAG, "mHeight:" + mHeight);
                        Log.d(TAG, "mWeight:" + mWeight);
                        mUserInfoItemObjectId = userInfo.getObjectId();

                        nickNameTv.setText(mNickName);
                        if (mHeight == 0) {
                            heightTv.setText("未设置");
                        } else {
                            heightTv.setText(String.valueOf(mHeight));
                        }
                        if (mWeight == 0) {
                            weightTv.setText("未设置");
                        } else {
                            weightTv.setText(String.valueOf(mWeight));
                        }
                        sexTv.setText(sexs[mSex]);
                    } else {
                        ToastUtil.shortToast(ChangeUserInfoActivity.this, "个人信息列表为空！");
                    }

                } else {
                    ToastUtil.shortToast(ChangeUserInfoActivity.this, e.getLocalizedMessage());
                    Log.d(TAG, "error code:" + e.getErrorCode() + " msg:" + e.getLocalizedMessage());
                }

            }
        });

    }

    @OnClick({R.id.nick_name_rl, R.id.sex_rl, R.id.height_rl, R.id.weight_rl, R.id.change_user_back_btn, R.id.change_user_save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nick_name_rl:

                setNickName();

                break;
            case R.id.sex_rl:

                setSex(mSex);

                break;
            case R.id.height_rl:
                setHeight(mHeight);
                break;
            case R.id.weight_rl:

                setWeight(mWeight);

                break;
            case R.id.change_user_back_btn:
                finish();
                break;
            case R.id.change_user_save_btn:

                saveUserInfo();

                break;
        }
    }

    //保存用户信息到服务器
    private void saveUserInfo() {
        mProgressDialog.show();
        mProgressDialog.setMessage("正在保存...");
        NewUserInfoBmob userInfoBmob = new NewUserInfoBmob();
        userInfoBmob.setpNickName(mNickName);
        userInfoBmob.setpHeight(mHeight);
        userInfoBmob.setpSex(mSex);
        userInfoBmob.setpWeight(mWeight);
        userInfoBmob.update(mUserInfoItemObjectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                mProgressDialog.dismiss();
                if (e == null) {
                    ToastUtil.shortToast(ChangeUserInfoActivity.this, "保存成功");
                    finish();
                } else {
                    ToastUtil.shortToast(ChangeUserInfoActivity.this, "保存失败:" + e.getLocalizedMessage());
                }

            }
        });
    }

    //设置体重
    private void setWeight(int currentWeight) {

        RelativeLayout mPickerView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.picker_layout, null, false);
        NumberPicker mWeightPicker = (NumberPicker) mPickerView.findViewById(R.id.picker_np);

        mWeightPicker.setMaxValue(200);
        mWeightPicker.setMinValue(20);
        mWeightPicker.setSelected(false);
        if (currentWeight == 0) {
            mWeightPicker.setValue(50);
        } else {
            mWeightPicker.setValue(currentWeight);
        }


        mWeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "oldVal:" + oldVal + " newVal:" + newVal);
                mWeight = newVal;

            }
        });

        new AlertDialog.Builder(this)
                .setTitle("选择体重(KG)")
                .setView(mPickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //当用户的体重刚好是选择器上面的数字时，用户无需滑动，此时value change监听器没有调用
                        if (mWeight == 0) {
                            weightTv.setText(String.valueOf(50));
                        } else {
                            weightTv.setText(String.valueOf(mWeight));
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    //设置身高
    private void setHeight(int currentHeight) {
        RelativeLayout mPickerView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.picker_layout, null, false);
        final NumberPicker mHeightPicker = (NumberPicker) mPickerView.findViewById(R.id.picker_np);

        mHeightPicker.setMaxValue(250);
        mHeightPicker.setMinValue(60);
        mHeightPicker.setSelected(false);

        if (currentHeight == 0) {
            mHeightPicker.setValue(170);
        } else {
            mHeightPicker.setValue(currentHeight);
        }

        mHeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d(TAG, "oldVal:" + oldVal + " newVal:" + newVal);
                mHeight = newVal;
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("选择身高(CM)")
                .setView(mPickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (mHeight == 0) {
                            heightTv.setText(String.valueOf(170));
                        } else {
                            heightTv.setText(String.valueOf(mHeight));
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();

    }

    //设置性别,从服务器拿到当前的性别，从而设置当前状态
    private void setSex(int currentSex) {
        RelativeLayout mPickerView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.picker_layout, null, false);
        NumberPicker mSexPicker = (NumberPicker) mPickerView.findViewById(R.id.picker_np);
        mSexPicker.setDisplayedValues(sexs);
        mSexPicker.setMinValue(0);
        mSexPicker.setMaxValue(sexs.length - 1);
        mSexPicker.setValue(currentSex);
        mSexPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                Log.d(TAG, "oldVal:" + oldVal + " newVal:" + newVal);
                mSex = newVal;
            }
        });
        new AlertDialog.Builder(this)
                .setTitle("性别")
                .setView(mPickerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sexTv.setText(sexs[mSex]);
                    }
                })
                .setNegativeButton("取消", null)
                .show();


    }

    //设置昵称
    private void setNickName() {

        final EditText editText = new EditText(this);
        editText.setText(nickNameTv.getText());
        editText.selectAll();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("修改昵称");
        builder.setView(editText);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String content = editText.getText().toString();
                if (!content.equals("")) {
                    mNickName = content;
                    nickNameTv.setText(mNickName);
                } else {
                    ToastUtil.shortToast(ChangeUserInfoActivity.this, "昵称不允许为空，修改失败!");
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        //弹出软键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
