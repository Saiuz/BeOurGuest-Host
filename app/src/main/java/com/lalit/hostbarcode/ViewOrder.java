package com.lalit.hostbarcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class ViewOrder extends AppCompatActivity {
SharedPreferences sp;
    ListView lv;
    String str;
    String data1[],data2[],data3[],data4[],data5[],data6[],data7[];
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> table_no = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> order = new ArrayList<>();
    ArrayList<String> pno = new ArrayList<>();
    ArrayList<String> email = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    int d = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_order);
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        str = sp.getString("name"," ");
        if(!netcheck.isInternetAvailable(ViewOrder.this)) //returns true if internet available
        {

            finish();
            Intent j = new Intent(ViewOrder.this,nonet.class);
            startActivity(j);
        }
//        Firebase.setAndroidContext(this);
        lv = findViewById(R.id.lv);
        final ProgressDialog progressDialog = new ProgressDialog(ViewOrder.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//        mref=new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/orders/");
        mref = database.getReference(str+"/orders/");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String arr[] = value.split("_xzy@#@acb_");
                date.add(arr[0]);
                time.add(arr[1]);
                name.add(arr[2]);
                pno.add(arr[3]);
                email.add(arr[4]);
                table_no.add(arr[5]);
                order.add(arr[6]);

//Toast.makeText(sub_menu.this,value,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                d = 1;
/*                final ProgressDialog progressDialog2 = new ProgressDialog(ViewOrder.this);
                progressDialog2.setTitle("Loading");
                progressDialog2.setMessage("Please Wait...");
                progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog2.show();
*/
                Collections.reverse(date);
                Collections.reverse(time);
                Collections.reverse(name);
                Collections.reverse(pno);
                Collections.reverse(email);
                Collections.reverse(table_no);
                Collections.reverse(order);
                data1 = date.toArray(new String[date.size()]);
                data2 = time.toArray(new String[time.size()]);
                data3 = name.toArray(new String[name.size()]);
                data4 = pno.toArray(new String[pno.size()]);
                data5 = email.toArray(new String[email.size()]);
                data6 = table_no.toArray(new String[table_no.size()]);
                data7 = order.toArray(new String[order.size()]);
                order_lv_adapter ad = new order_lv_adapter(ViewOrder.this, data6, data1, data2, data3);
                lv.setAdapter(ad);
                //              progressDialog2.dismiss();
                lv.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                finish();
                                String e = data1[position] + "_xzy@#@acb_" + data2[position] + "_xzy@#@acb_" + data3[position] + "_xzy@#@acb_" + data4[position] + "_xzy@#@acb_" + data5[position] + "_xzy@#@acb_" + data6[position] + "_xzy@#@acb_" + data7[position];
                                Intent i = new Intent(ViewOrder.this, OrderDetail.class);
                                i.putExtra("detail", e);
                                startActivity(i);
                            }
                        });
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(d==0)
                {
                    progressDialog.dismiss();
                    lv.setVisibility(View.GONE);
                    findViewById(R.id.tv).setVisibility(View.VISIBLE);
                }
            }
        }, 8500);

    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(ViewOrder.this,MainActivity.class);
        startActivity(i);
        finish();

    }
}
