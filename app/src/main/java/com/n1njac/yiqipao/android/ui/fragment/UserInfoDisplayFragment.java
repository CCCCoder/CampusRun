package com.n1njac.yiqipao.android.ui.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.Fade;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.ui.widget.BezierView;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by N1njaC on 2017/9/8.
 */

public class UserInfoDisplayFragment extends Fragment {

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

    private PopupWindow mPopupWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_info_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.user_icon_iv, R.id.edit_user_info_btn})
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
                // TODO: 2017/9/14 跳转到个人信息编辑界面
                ToastUtil.shortToast(getActivity(), "编辑资料");
                break;
        }
    }

    private void backgroundAlpha(float alpha){

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
}
