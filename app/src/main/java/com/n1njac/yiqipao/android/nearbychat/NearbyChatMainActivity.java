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

import com.n1njac.yiqipao.android.R;


public class NearbyChatMainActivity extends AppCompatActivity {

    private Button mContact;
    SharedPreferences sf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_chat);

        // TODO: 2017/4/9 展示打招呼人的信息

        sf = PreferenceManager.getDefaultSharedPreferences(this);

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
        mContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

    }
}
