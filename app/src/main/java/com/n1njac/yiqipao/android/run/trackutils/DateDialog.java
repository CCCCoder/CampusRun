package com.n1njac.yiqipao.android.run.trackutils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import com.n1njac.yiqipao.android.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DateDialog extends Dialog implements
        View.OnClickListener {

    private CallBack back;
    private PriorityListener lis;
    private boolean scrolling = false;
    @SuppressWarnings("unused")
    private Context context = null;
    public Button softInfo = null;
    public Button softInfoButton = null;
    private NumericWheelAdapter yearAdapter = null;
    private NumericWheelAdapter monthAdapter = null;
    private NumericWheelAdapter dayAdapter = null;
    private Button btnSure = null;
    private Button btnCancel = null;
    private int curYear = 0;
    private int curMonth = 0;
    private int curDay = 0;
    private WheelView monthview = null;
    private WheelView yearview = null;
    private WheelView dayview = null;
    @SuppressLint("InlinedApi")
    private static int theme = android.R.style.Theme_Holo_Light_Dialog; // 主题
    // private static int theme = android.R.style.Theme_Black_NoTitleBar; // 主题
    private LinearLayout dateLayout;
    private int width; // 对话框宽度
    private int height; // 对话框高度
    private TextView titleTv;
    private String title;
    private int flag = 0;

    public interface PriorityListener {
        public void refreshPriorityUI(String year, String month, String day,
                                      CallBack back);
    }

    public interface CallBack {
        public void execute();
    }

    public DateDialog(final Context context, final PriorityListener listener,
                      CallBack callback, int currentyear, int currentmonth,
                      int currentday, int width, int height, String title, int flag) {
        super(context, theme);
        this.context = context;
        lis = listener;
        back = callback;
        this.curYear = currentyear;
        this.curMonth = currentmonth;
        this.curDay = currentday;
        this.width = width;
        this.title = title;
        this.height = height;
        this.flag = flag;
    }

    public DateDialog(Context context, PriorityListener listener) {
        super(context, theme);
        this.context = context;
    }

    public DateDialog(Context context, String birthDate) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_select_wheel);
        btnSure = (Button) findViewById(R.id.confirm_btn);
        btnSure.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.cancel_btn);
        btnCancel.setOnClickListener(this);
        dateLayout = (LinearLayout) findViewById(R.id.date_select_layout);
        LayoutParams lparamsHours = new LayoutParams(width, height / 3 + 10);
        dateLayout.setLayoutParams(lparamsHours);
        titleTv = (TextView) findViewById(R.id.dialog_title_tv);
        titleTv.setText(title);
        yearview = (WheelView) findViewById(R.id.year);
        monthview = (WheelView) findViewById(R.id.month);
        dayview = (WheelView) findViewById(R.id.day);
        if (flag != 1) {
            btnSure.setText("下一步");
        } else {
            btnSure.setText("确定");
        }

        OnWheelChangedListener listener = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateDays(yearview, monthview, dayview);
                }
            }
        };
        monthview.addChangingListener(listener);
        yearview.addChangingListener(listener);
        Calendar calendar = Calendar.getInstance();
        if (this.curYear == 0 || this.curMonth == 0) {
            curYear = calendar.get(Calendar.YEAR);
            curMonth = calendar.get(Calendar.MONTH) + 1;
            curDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        yearAdapter = new NumericWheelAdapter(2001, 2100);
        yearview.setAdapter(yearAdapter);
        int cc = curYear - 2001;
        yearview.setCurrentItem(cc);
        yearview.setVisibleItems(5);
        monthAdapter = new NumericWheelAdapter(1, 12, "%02d");
        monthview.setAdapter(monthAdapter);
        monthview.setCurrentItem(curMonth - 1);
        monthview.setCyclic(false);
        monthview.setVisibleItems(5);
        updateDays(yearview, monthview, dayview);
        dayview.setCyclic(false);
        dayview.setVisibleItems(5);
    }

    /**
     * 根据年份和月份来更新日期
     */
    private void updateDays(WheelView year, WheelView month, WheelView day) {
        String[] monthsBig = {"1", "3", "5", "7", "8", "10", "12"};
        String[] monthsLittle = {"4", "6", "9", "11"};

        final List<String> listBig = Arrays.asList(monthsBig);
        final List<String> listLittle = Arrays.asList(monthsLittle);
        int yearNum = year.getCurrentItem() + 1900;
        if (listBig.contains(String.valueOf(month.getCurrentItem() + 1))) {
            dayAdapter = new NumericWheelAdapter(1, 31, "%02d");
        } else if (listLittle
                .contains(String.valueOf(month.getCurrentItem() + 1))) {
            dayAdapter = new NumericWheelAdapter(1, 30, "%02d");
        } else {
            if ((yearNum % 4 == 0 && yearNum % 100 != 0)
                    || yearNum % 400 == 0) {
                dayAdapter = new NumericWheelAdapter(1, 29, "%02d");
            } else {
                dayAdapter = new NumericWheelAdapter(1, 28, "%02d");
            }
        }
        dayview.setAdapter(dayAdapter);
        dayview.setCurrentItem(curDay - 1);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_btn:
                lis.refreshPriorityUI(yearAdapter.getValues(),
                        monthAdapter.getValues(), dayAdapter.getValues(), back);

                this.dismiss();
                // if (flag != 1) {
                // getTime();
                // }
                break;
            case R.id.cancel_btn:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
