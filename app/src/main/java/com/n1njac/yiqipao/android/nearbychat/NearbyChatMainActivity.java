package com.n1njac.yiqipao.android.nearbychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.n1njac.yiqipao.android.R;
import com.n1njac.yiqipao.android.bmobObject.PersonInfoBmob;
import com.n1njac.yiqipao.android.nearybyview.NearbyMainActivity;

import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.QueryListener;


public class NearbyChatMainActivity extends AppCompatActivity {

    private Button mContact, mRefuse;
    SharedPreferences sf;

    private TextView name, sex, birth, height, weight, interest;

    //    和你打招呼人的objectId
    String objectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_chat);
        initView();
        sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        getDataFromBmob();

        // TODO: 2017/4/9 展示打招呼人的信息

        String phone = sf.getString("phone", null);
        Log.d("xyz", "phone--nearbychat:" + phone);

        String callPhone = null;
        // TODO: 2017/4/10 号码修改
        String phone1 = "15505514373";
        String phone2 = "18119635150";
        if (phone.equals(phone1)) {
            callPhone = phone2;
        } else {
            callPhone = phone1;
        }

        // TODO: 2017/4/9 号码处理
        final Uri uri = Uri.parse("tel:" + callPhone);
        final Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        mContact = (Button) findViewById(R.id.contact_btn);
        mRefuse = (Button) findViewById(R.id.refuse_btn);
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/4/12 发送一条推送告诉对方被拒绝
                //对方的installationId

                //15505514373
                String installationId1 = "8AC0A4524336DA27E2861D9071B03244";
                //18119635150
                String installationId2 = "1C04DA1CD835A7837A70E608036A9AA2";

                String phone = sf.getString("phone", null);
                Log.d("xyz", "phone--nearbychat:" + phone);

                String installationId = null;
                // TODO: 2017/4/10 号码修改
                String phone1 = "15505514373";
                String phone2 = "18119635150";
                if (phone.equals(phone1)) {
                    installationId = installationId2;
                } else {
                    installationId = installationId1;
                }

                BmobPushManager bmobPush = new BmobPushManager();
                BmobQuery<BmobInstallation> query = BmobInstallation.getQuery();
                query.addWhereEqualTo("installationId", installationId);
                bmobPush.setQuery(query);
                bmobPush.pushMessage("refuse", new PushListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(NearbyChatMainActivity.this, "已拒绝", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(NearbyChatMainActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                finish();
            }
        });

    }

    //    从服务器拿到对方的资料
// TODO: 2017/4/12 可以把对方的objectId取出来放到本地数据库，然后可以在推送中把这个id取出来，让其他人拿到
//    然后访问bmob数据库
    private void getDataFromBmob() {


        //15505514373
        String objectId1 = "edfe7e04f8";
        //18119635150
        String objectId2 = "c6ddea6a46";

        String phone = sf.getString("phone", null);
        Log.d("xyz", "phone--nearbychat:" + phone);

        String objectId = null;
        // TODO: 2017/4/10 号码修改
        String phone1 = "15505514373";
        String phone2 = "18119635150";
        if (phone.equals(phone1)) {
            objectId = objectId2;
        } else {
            objectId = objectId1;
        }

        BmobQuery<PersonInfoBmob> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<PersonInfoBmob>() {
            @Override
            public void done(PersonInfoBmob personInfoBmob, BmobException e) {
                if (e == null) {
                    name.setText(personInfoBmob.getNickName());
                    sex.setText(personInfoBmob.getSex());
                    birth.setText(personInfoBmob.getBirth());
                    height.setText(personInfoBmob.getHeight());
                    weight.setText(personInfoBmob.getWeight());
                    interest.setText(personInfoBmob.getHobby());
                } else {
                    Log.i("xyz", "NearbyChatMainActivity-访问数据库失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name_tx);
        sex = (TextView) findViewById(R.id.sex_tx);
        birth = (TextView) findViewById(R.id.birth_tx);
        height = (TextView) findViewById(R.id.height_tx);
        weight = (TextView) findViewById(R.id.weight_tx);
        interest = (TextView) findViewById(R.id.interest_tx);
    }
}
