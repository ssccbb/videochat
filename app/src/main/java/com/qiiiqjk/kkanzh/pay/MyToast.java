package com.qiiiqjk.kkanzh.pay;

/**
 * Created by Byron on 2018/1/7.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qiiiqjk.kkanzh.R;


public class MyToast {

    public static void show(Context context, String text, boolean isLong) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View layout = inflater.inflate(R.layout.toast_layout, null);



//        ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
//
//        image.setImageResource(R.drawable.qr_code_file);



        TextView textV = (TextView) layout.findViewById(R.id.toast_text);

        textV.setText(text);



        Toast toast = new Toast(context);

        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setDuration((isLong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);

        toast.setView(layout);

        toast.show();

    }

}