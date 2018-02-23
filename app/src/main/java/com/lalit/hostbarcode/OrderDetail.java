package com.lalit.hostbarcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class OrderDetail extends AppCompatActivity {
    SharedPreferences sp;
    String[] raw;
    String str3;
    TextView name_tv,email_tv,pno_tv,tno_tv,toto,date_tv,time_tv;
    Button conti,done_btn,edit;
    ListView akhrilist;
    List3Adapter ad;
    int n;
    String ars[];
    private String str2;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_detail);
        if(!netcheck.isInternetAvailable(OrderDetail.this)) //returns true if internet available
        {

            finish();
            Intent i = new Intent(OrderDetail.this,nonet.class);
            startActivity(i);
        }
//        Firebase.setAndroidContext(this);
        sp = getApplicationContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        date_tv = (TextView)findViewById(R.id.date_tv);
        time_tv = (TextView)findViewById(R.id.time_tv);
        name_tv=(TextView)findViewById(R.id.name_tv);
        pno_tv=(TextView)findViewById(R.id.pno_tv);
        email_tv=(TextView)findViewById(R.id.email_tv);
        tno_tv=(TextView)findViewById(R.id.tno_tv);
        toto=(TextView)findViewById(R.id.toto);
        done_btn =(Button)findViewById(R.id.done_btn);
        akhrilist=(ListView)findViewById(R.id.akhrilist);
        Intent in = getIntent();
        str3 = in.getStringExtra("detail");
        str2 = sp.getString("name"," ");
        ars = str3.split("_xzy@#@acb_");
        date_tv.setText(ars[0]);
        time_tv.setText(ars[1]);
        name_tv.setText(ars[2]);
        pno_tv.setText(ars[3]);
        email_tv.setText(ars[4]);
        tno_tv.setText(ars[5]);
        String str = ars[6];
        raw = str.split("_acb@#@xzy_");
        int n = ((raw.length)/3);
        ArrayList<String> titles = new ArrayList();
        ArrayList<String> no = new ArrayList();
        ArrayList<String> price = new ArrayList();
        for(int h = 0; h < n; h++)
        {
            if(!(raw[(h * 3) + 1]).trim().equals("0"))
            {
                titles.add(raw[(h * 3)]);
                no.add(raw[(h * 3) + 1]);
                price.add(raw[(h * 3)+2]);
            }

        }
        ad = new List3Adapter(OrderDetail.this,  titles.toArray(new String[0]),no.toArray(new String[0]), price.toArray(new String[0]));
        akhrilist.setAdapter(ad);
//Toast.makeText(sub_menu.this,value,Toast.LENGTH_LONG).show();
        String arr2[]=no.toArray(new String[0]);
        String arr[] = price.toArray(new String[0]);
        int g=0;
        for(int p=0;p<arr.length;p++)
        {
            g=g+( Integer.parseInt(arr[p]) * Integer.parseInt(arr2[p]) );
        }
        toto.setText("Total Payable : Rs."+String.valueOf(g));


        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            //

            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(OrderDetail.this);
                ab.setMessage("Are you sure you want to delete the order???").setCancelable(false);
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                    mref = new Firebase("https://be-our-guest-host.firebaseio.com/"+str2+"/orders/");
                        mref = database.getReference(str2+"/orders/");
                        mref.child(ars[0]+ars[1]+ars[5]).setValue(null);
                        finish();
                        Toast.makeText(OrderDetail.this,"Order has been deleted",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(OrderDetail.this,ViewOrder.class));
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                AlertDialog a = ab.create();
                a.show();
                //finish();
                //  Intent i = new Intent(loginlogup.this,Main2Activity.class);
                // startActivity(i);

            }


        });

    }

    public void onBackPressed()
    {

        Intent i = new Intent(OrderDetail.this,ViewOrder.class);
        startActivity(i);
        finish();
    }
}
