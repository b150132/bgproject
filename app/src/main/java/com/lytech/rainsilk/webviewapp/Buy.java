package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by rainsilk on 2016/1/12.
 */
public class Buy extends Activity {
    private Spinner mSpinBuyType;
    private TextView mTxtB;
    private ImageView imgBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy);
        mTxtB = (TextView) findViewById(R.id.textViewBuyType);
        mSpinBuyType = (Spinner) findViewById(R.id.spinnerJobtype);
        mSpinBuyType.setOnItemSelectedListener(spnBuyTypeItemSelect);
        imgBuy=(ImageView)findViewById(R.id.imageViewBuyThings);

        Bitmap mBitmap2 = getLocalBitmap(Buy.this,R.drawable.shop);
        imgBuy.setImageBitmap(mBitmap2);
    }

    public Bitmap getLocalBitmap(Context con, int resourceId){
        InputStream inputStream = con.getResources().openRawResource(resourceId);
        return BitmapFactory.decodeStream(inputStream, null, getBitmapOptions(2));
    }
    public BitmapFactory.Options getBitmapOptions(int scale){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inSampleSize = scale;
        return options;
    }

    public void onButtonClick(View v) {
       if (v.getId() == R.id.imageViewBuyThings) { //Homepage
            Intent i = new Intent(Buy.this, Buy_view_main.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
        }

        if (v.getId() == R.id.imgUndo) { //change baby photo
            finish();
        }
    }
    private AdapterView.OnItemSelectedListener spnBuyTypeItemSelect = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id){
            mTxtB.setText(getString(R.string.buytype_select)+parent.getSelectedItem().toString());
            Object item = parent.getItemAtPosition(position);
            String chooseItem = item.toString();

            String str1= "輔食";
            String str2= "配方奶粉";
            String str3= "營養品";


            if(str1.equals(chooseItem)==true)
               imgBuy.setImageResource(R.drawable.shop);

            if(str2.equals(chooseItem)==true)
                imgBuy.setImageResource(R.drawable.buy_2);

            if(str3.equals(chooseItem)==true)
                imgBuy.setImageResource(R.drawable.buy_3);

            switch(parent.getSelectedItem().toString())
            {
                case "":
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?>  arg0){

        }
    };
}
