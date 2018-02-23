package com.lalit.hostbarcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class sub_menu extends AppCompatActivity {
TextView tv,bc;
    Button btn;
    ListView lv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    ArrayList<String> mnotes = new ArrayList<>();
    ArrayList<String> mnotes2 = new ArrayList<>();
private View v;
    int d=0;
    String f=" ";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String item_name;
    String data1[],data2[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sub_menu);
        tv= (TextView)findViewById(R.id.res_name);
        bc = (TextView)findViewById(R.id.bc);
        lv = (ListView)findViewById(R.id.lv_items);
        btn = (Button)findViewById(R.id.btn_cart);
        Intent i = getIntent();
        item_name=i.getStringExtra("category");
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        if(!netcheck.isInternetAvailable(sub_menu.this)) //returns true if internet available
        {

            finish();
            Intent j = new Intent(sub_menu.this,nonet.class);
            startActivity(j);
        }
//        Firebase.setAndroidContext(this);
        Typeface t = Typeface.createFromAsset(getAssets(),"Pacifico-Regular.ttf");
        tv.setTypeface(t);
        String str = sp.getString("name"," ").trim();
        tv.setText(item_name);
        final ProgressDialog progressDialog = new ProgressDialog(sub_menu.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
//        mref=new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/"+item_name+"/");
        mref = database.getReference(str+"/menu/"+item_name+"/");
        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mnotes.add(value);
                data1 = mnotes.toArray(new String[mnotes.size()]);
                String value2 = dataSnapshot.getKey();
                mnotes2.add(value2);
                data2 = mnotes2.toArray(new String[mnotes2.size()]);
                ListAdapter ad = new ListAdapter(sub_menu.this, data1);
                lv.setAdapter(ad);
                d=1;
//Toast.makeText(sub_menu.this,value,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                lv.setOnItemClickListener(
                        new AdapterView.OnItemClickListener(){
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                finish();
                                Intent i = new Intent(sub_menu.this,full_edit.class);
                                i.putExtra("detail1",data1[position]);
                                i.putExtra("detail2",data2[position]);
                                i.putExtra("category",item_name);
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
                 bc.setVisibility(View.VISIBLE);
                }
            }
        }, 8500);




btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
        Intent i = new Intent(sub_menu.this,AddItem.class);
        startActivity(i);

    }
});
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(sub_menu.this,main_menu.class);
        startActivity(i);
        finish();

    }

}
