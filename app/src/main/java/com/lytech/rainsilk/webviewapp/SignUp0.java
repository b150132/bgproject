package com.lytech.rainsilk.webviewapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by rainsilk on 2015/12/23.
 */
public class SignUp0 extends Activity {
//    private int my_ids[] = {R.id.imgbtn01, R.id.imgbtn02, R.id.imgbtn03, R.id.imgbtn04, R.id.imgbtn05
//            , R.id.imgbtn06};
    private  boolean my_ass[] = {false, false,false, false, false, false,false,false,false};
    private int assCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup0);

//        Button b = null;
//        for( int i=0 ; i< 6 ; ++i )
//            if( ( b = (Button)findViewById( my_ids[i]) ) != null )
//                b.setOnClickListener((View.OnClickListener) this);
    }
//    public void onClick(View v) {
//        switch( v.getId() ) {
//            case R.id.imgbtn01:
//                assoiation[assCount]=1;
//                assCount++;
//                break;
//            case R.id.imgbtn02:
//                assoiation[assCount]=2;
//                assCount++;
//                break;
//            case R.id.imgbtn03:
//                assoiation[assCount]=3;
//                assCount++;
//                break;
//            case R.id.imgbtn04:
//                assoiation[assCount]=4;
//                assCount++;
//                break;
//            case R.id.imgbtn05:
//                assoiation[assCount]=5;
//                assCount++;
//                break;
//            case R.id.imgbtn06:
//                assoiation[assCount]=6;
//                assCount++;
//                break;
//
//        }
//    }

    public void onButtonClick(View v)
    {
        if(v.getId()==R.id.checkedTextView)
        {
            Intent i = new Intent(SignUp0.this,Policy.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
        if(v.getId()==R.id.btnLogin)
        {
            Intent i = new Intent(SignUp0.this,MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        if(v.getId()==R.id.Bsignup)
        {
            int [] assoiation = new int[assCount];
            int index=0;
            String string_assoia= "";

            for(int i = 0; i < my_ass.length; i++) {
                if (my_ass[i] == true) {
                    assoiation[index] = i;
                    index++;
                }
            }
            for(int j = 0; j < assoiation.length; j++) {
                string_assoia=string_assoia+String.valueOf(assoiation[j]);
            }

            Intent i = new Intent(SignUp0.this,SignUp.class);
            i.putExtra("ass", string_assoia);
            startActivity(i);
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }

        switch (v.getId()) {
//            case R.id.imgbtn01:
//                my_ass[1]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.imgbtn02:
//                my_ass[2]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.imgbtn03:
//                my_ass[3]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.imgbtn04:
//                my_ass[4]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;
            case R.id.imgbtn05:
                my_ass[5]=true;
                assCount++;
                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.imgbtn06:
                my_ass[6]=true;
                assCount++;
                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
                break;
//            case R.id.imgbtn07:
//                my_ass[7]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.imgbtn08:
//                my_ass[8]=true;
//                assCount++;
//                Toast.makeText(SignUp0.this, "已選擇"+(assCount)+"協會" , Toast.LENGTH_SHORT).show();
//                break;

            default:
                break;

        }


    }
}
