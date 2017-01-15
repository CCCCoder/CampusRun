package com.n1njac.yiqipao.android.personalInfo;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.n1njac.yiqipao.android.Fragment.PersonalInfoFragment;
import com.n1njac.yiqipao.android.MainActivity;
import com.n1njac.yiqipao.android.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by huanglei on 2017/1/15.
 */

public class ExecPlanActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText aimDistance;
    private ImageView runImage;
    private CheckBox alarmCheckBox;
    private TextView alarmTime;
    private Button save, returnBtn;

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exec_plan_activity);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        initView();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        if (!"".equals(prefs.getString("distance", null))) {
            aimDistance.setText(prefs.getString("distance", null));
        } else {
            aimDistance.setText("100");
        }


        if ("1".equals(prefs.getString("checkbox", null))) {
            alarmCheckBox.setChecked(true);
        } else {
            alarmCheckBox.setChecked(false);
        }

        if (prefs.getString("time", null) != null) {
            alarmTime.setText(prefs.getString("time", null));
        } else {
            alarmTime.setText("09:30");
        }

    }

    private void initView() {
        aimDistance = (EditText) findViewById(R.id.exec_et);
        runImage = (ImageView) findViewById(R.id.run_image);
        alarmCheckBox = (CheckBox) findViewById(R.id.alarm_cb);
        alarmTime = (TextView) findViewById(R.id.alarmtime_tx);
        save = (Button) findViewById(R.id.submit_btn);
        returnBtn = (Button) findViewById(R.id.return_btn);
        save = (Button) findViewById(R.id.submit_btn);
        aimDistance.setSelection(aimDistance.getText().length());
        returnBtn.setOnClickListener(this);
        runImage.setOnClickListener(this);
        alarmTime.setOnClickListener(this);
        save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_btn:

                Intent intent = new Intent();
                intent.putExtra("distance",aimDistance.getText().toString());
                Log.i("xyz","ExecPlanActivity-distance:"+aimDistance.getText().toString());
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.run_image:
                Toast.makeText(this, "加油，干巴爹！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.submit_btn:
                saveRunnerData();
                break;
            case R.id.alarmtime_tx:
                showTimePickerDialog();
                break;

        }
    }

    private void showTimePickerDialog() {

        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        DateFormat format = new SimpleDateFormat("HH:mm");
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                editor.putString("time", time);
                editor.apply();
                alarmTime.setText(time);
            }
        }, hours, minute, true).show();

    }

    //保存用户的修改的数据
    private void saveRunnerData() {

        String distance = aimDistance.getText().toString();
        editor.putString("distance", distance);

        if (alarmCheckBox.isChecked()) {
            editor.putString("checkbox", "1");

        } else {
            editor.putString("checkbox", "0");
        }
        editor.apply();

        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("保存成功！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }
}
