package com.n1njac.yiqipao.android.ui.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.RunDataBmob;
import com.n1njac.yiqipao.android.bmobObject.UserInfoBmob;
import com.n1njac.yiqipao.android.ui.activity.ChangeUserInfoActivity;
import com.n1njac.yiqipao.android.ui.activity.MainActivity;
import com.n1njac.yiqipao.android.ui.widget.BezierView;
import com.n1njac.yiqipao.android.utils.ToastUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadFileListener;
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
    private UpdateRunDataReceiver mReceiver;

    private Uri mPhotoUri;
    private String mAvatarUrl;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSf;

    private static final int CHOOSE_ALBUM = 0x777;
    private static final int TAKE_PHOTO = 0x778;
    private static final int CROP_PIC = 0x779;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.user_info_frag, container, false);
        unbinder = ButterKnife.bind(this, view);

        mEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        mSf = PreferenceManager.getDefaultSharedPreferences(getActivity());

        getDataFromServer();

        mReceiver = new UpdateRunDataReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(PersonalRunInfoFragment.UPDATE_RUN_DATA_ACTION);
        getActivity().registerReceiver(mReceiver, filter);

        mAvatarUrl = mSf.getString("avatar_user", null);
        //设置头像
        Glide.with(getActivity()).load(mAvatarUrl).error(R.drawable.boy).into(userIconIv);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
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

                startActivity(new Intent(getActivity(), ChangeUserInfoActivity.class));

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
        TextView fromAlbum = (TextView) popupView.findViewById(R.id.popup_from_album_tv);
        TextView takePhoto = (TextView) popupView.findViewById(R.id.popup_take_photo_tv);
        TextView cancel = (TextView) popupView.findViewById(R.id.popup_cancel_tv);

        fromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_ALBUM);

                mPopupWindow.dismiss();
            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPhotoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp_image.jpg"));
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);

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

    //裁剪图片

    private void cutImage(Uri uri) {
        if (uri == null) {
            Log.d(TAG, "uri not exist");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪时是否保留图片的比例，这里的比例是1:1
        intent.putExtra("scale", true);
        //是否将数据保留在Bitmap中返回
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PIC);

    }

    //查询跑步次数和总路程
    public void getDataFromServer() {
        String objectId = BmobUser.getCurrentUser(UserInfoBmob.class).getObjectId();

        Log.d(TAG, "objectId:" + objectId);
        BmobQuery<RunDataBmob> query = new BmobQuery<>();
        query.addWhereEqualTo("pUserObjectId", objectId);

        // TODO: 2017/10/11 这里数据量大的时候需要分页处理数据，bmob只支持最大500条的查询
        query.findObjects(new FindListener<RunDataBmob>() {
            @SuppressLint("SetTextI18n")
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
//                    userRunCountTv.setText("0");
//                    userInfoDistanceTv.setText("0.00");
                    Log.d(TAG, "error code：" + e.getErrorCode() + " msg:" + e.getLocalizedMessage());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:

                Log.d(TAG, "TAKE_PHOTO");
                cutImage(mPhotoUri);

                break;
            case CHOOSE_ALBUM:
                Log.d(TAG, "CHOOSE_ALBUM");
                if (data != null) {
                    cutImage(data.getData());
                }

                break;
            case CROP_PIC:
                Log.d(TAG, "CROP_PIC");
                if (data != null) {
                    handleAvatar(data);
                }

                break;
            default:
                break;
        }
    }

    //设置头像并上传服务器
    private void handleAvatar(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                byte[] bytes = baos.toByteArray();
                Glide.with(getActivity()).load(bytes).into(userIconIv);

                //bitmap转file
                File parentFile = new File(Environment.getExternalStorageDirectory() + "/yiqipao");
                if (!parentFile.exists()) {
                    parentFile.mkdir();
                }
                File file = new File(parentFile, "Avatar_temp.jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    bos.flush();
                    bos.close();
                    //上传服务器
                    final BmobFile bmobFile = new BmobFile(file);
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {

                            if (e == null) {
                                mAvatarUrl = bmobFile.getFileUrl();
                                mEditor.putString("avatar_user", mAvatarUrl);
                                mEditor.commit();

                                //通知MainActivity更改头像

                                getActivity().sendBroadcast(new Intent(MainActivity.UPDATE_ICON));

                                Log.d(TAG, "url:" + mAvatarUrl);
                            } else {
                                Log.d(TAG, "error code:" + e.getErrorCode() + " msg:" + e.getLocalizedMessage());

                            }
                        }

                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
