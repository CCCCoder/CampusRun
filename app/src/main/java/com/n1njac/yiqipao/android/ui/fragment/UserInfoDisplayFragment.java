package com.n1njac.yiqipao.android.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.ui.widget.BezierView;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by N1njaC on 2017/9/8.
 */

public class UserInfoDisplayFragment extends Fragment {

    private static final String TAG = UserInfoDisplayFragment.class.getSimpleName();

    @BindView(R.id.bezier_bg)
    BezierView bezierBg;
    @BindView(R.id.user_icon_iv)
    CircleImageView userIconIv;
    @BindView(R.id.user_id_tv)
    TextView userIdTv;
    @BindView(R.id.user_signature_tv)
    TextView userSignatureTv;
    @BindView(R.id.user_run_count_tv)
    TextView userRunCountTv;
    @BindView(R.id.user_info_distance_tv)
    TextView userInfoDistanceTv;
    @BindView(R.id.edit_user_info_btn)
    Button editUserInfoBtn;
    Unbinder unbinder;
    @BindView(R.id.user_info_dis_linear)
    LinearLayout rootView;
    @BindView(R.id.user_info_setting_iv)
    ImageView userInfoSettingIv;

    private PopupWindow mPopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_info_frag, container, false);
        unbinder = ButterKnife.bind(this, view);
        getDataFromServer();

        UpdateRunDataReceiver receiver = new UpdateRunDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PersonalRunInfoFragment.UPDATE_RUN_DATA_ACTION);
        getActivity().registerReceiver(receiver, filter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.user_icon_iv, R.id.edit_user_info_btn, R.id.user_info_setting_iv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_icon_iv:

                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_icon_check, null, false);
                mPopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

                //设置动画必须在设置location之前
                mPopupWindow.setAnimationStyle(R.style.Popupwindow);
                mPopupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                setTvListener(popupView);

                backgroundAlpha(0.5f);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });

                break;
            case R.id.edit_user_info_btn:

                ToastUtil.shortToast(getActivity(), "编辑资料");
                break;

            case R.id.user_info_setting_iv:
                ToastUtil.shortToast(getActivity(), "更换背景");
                break;
        }
    }

    //更改背景透明度
    private void backgroundAlpha(float alpha) {

        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);

    }

    private void setTvListener(View popupView) {
        TextView checkBig = (TextView) popupView.findViewById(R.id.popup_check_big_icon_tv);
        TextView changeIcon = (TextView) popupView.findViewById(R.id.popup_change_icon_tv);
        TextView cancel = (TextView) popupView.findViewById(R.id.popup_cancel_tv);

        checkBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtil.shortToast(getActivity(), "查看大图");

                mPopupWindow.dismiss();
            }
        });

        changeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.shortToast(getActivity(), "更换头像");
                mPopupWindow.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
    }


    //查询跑步次数和总路程
    public void getDataFromServer() {
        String objectId = BmobUser.getCurrentUser(UserInfoBmob.class).getObjectId();

        Log.d(TAG, "objectId:" + objectId);
        BmobQuery<RunDataBmob> query = new BmobQuery<>();
        query.addWhereEqualTo("pUserObjectId", objectId);

        // TODO: 2017/10/11 这里数据量大的时候需要分页处理数据，bmob只支持最大500条的查询
        query.findObjects(new FindListener<RunDataBmob>() {
            @Override
            public void done(List<RunDataBmob> list, BmobException e) {


                if (e == null) {
                    int count = list.size();
                    userRunCountTv.setText(count + "");

                    DecimalFormat df = new DecimalFormat("#0.00");
                    double totalDistance = 0.0;
                    for (RunDataBmob r : list) {
                        if (!r.getRunDistance().equals("")) {
                            totalDistance += Double.parseDouble(r.getRunDistance().trim());
                        }
                    }

                    userInfoDistanceTv.setText(String.valueOf(df.format(totalDistance)));
                    Log.d(TAG, "total distance:" + totalDistance);

                } else {
                    userRunCountTv.setText("0");
                    userInfoDistanceTv.setText("0.00");
                    Log.d(TAG, "error code：" + e.getErrorCode() + " mgs:" + e.getLocalizedMessage());
                }
            }
        });
    }


    private class UpdateRunDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "------------>receive update");
            getDataFromServer();
        }
    }

}
