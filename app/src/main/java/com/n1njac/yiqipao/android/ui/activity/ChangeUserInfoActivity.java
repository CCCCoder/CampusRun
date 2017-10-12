package com.n1njac.yiqipao.android.ui.activity;
/*    
 *    Created by N1njaC on 2017/10/11.
 *    email:aiai173cc@gmail.com 
 */


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeUserInfoActivity extends BaseActivity {

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


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_info_activity);
        ButterKnife.bind(this);
        nickNameTv.setText("N1njaC");


    }

    @OnClick({R.id.nick_name_rl, R.id.sex_rl, R.id.height_rl, R.id.weight_rl, R.id.change_user_back_btn, R.id.change_user_save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nick_name_rl:

                setNickName();

                break;
            case R.id.sex_rl:

                setSex();

                break;
            case R.id.height_rl:
                setHeight();
                break;
            case R.id.weight_rl:

                setWeight();

                break;
            case R.id.change_user_back_btn:
                finish();
                break;
            case R.id.change_user_save_btn:
                break;
        }
    }

    //设置体重
    private void setWeight() {

    }

    //设置身高
    private void setHeight() {
    }

    //设置性别
    private void setSex() {

    }

    //设置昵称
    private void setNickName() {

        final EditText editText = new EditText(this);
        editText.setText(nickNameTv.getText());
        editText.selectAll();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("修改昵称");
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mNickName = editText.getText().toString();
                nickNameTv.setText(mNickName);
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
                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
            }
        });
        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
