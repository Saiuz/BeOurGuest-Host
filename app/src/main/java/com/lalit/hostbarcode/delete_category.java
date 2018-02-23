package com.lalit.hostbarcode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class delete_category extends AppCompatActivity {
    private TextView name;
    SharedPreferences sp;
    int d=0;
    String final_str=" ";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref,mref2;
    String strr[];
    String title;
    SharedPreferences.Editor editor;
    GridView gvCategory;
    int[] imgs = {R.drawable.food};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_delete_category);
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = sp.edit();
        if(!netcheck.isInternetAvailable(delete_category.this)) //returns true if internet available
        {

            finish();
            Intent i = new Intent(delete_category.this,nonet.class);
            startActivity(i);
        }
//        Firebase.setAndroidContext(this);
        name = (TextView)findViewById(R.id.namet);
        Typeface t = Typeface.createFromAsset(getAssets(),"Pacifico-Regular.ttf");
        name.setTypeface(t);
        String str = sp.getString("name"," ");
        name.setText(str);
//        mref = new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/major_categories");
//        mref2 = new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/");
        mref = database.getReference(str+"/menu/major_categories");
        mref2 = database.getReference(str+"/menu/");
        gvCategory = (GridView) findViewById(R.id.gv_category);
        final ProgressDialog progressDialog = new ProgressDialog(delete_category.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                strr = value.split("_acb@#@xzy_");
                GridAdapter ad = new GridAdapter(delete_category.this, imgs, strr);
                gvCategory.setAdapter(ad);
                d=1;
                progressDialog.dismiss();
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
                    gvCategory.setVisibility(View.GONE);
                    findViewById(R.id.bc).setVisibility(View.VISIBLE);
                }
            }
        }, 8500);



        gvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                title = view.getTag().toString();
                final_str=" ";
                for(int t=0;t<strr.length;t++)
                {
                    if(!strr[t].equals(title))
                    {
                        if(final_str.equals(" "))
                        {
                            final_str = strr[t];
                        }
                        else
                            final_str = final_str+"_acb@#@xzy_"+strr[t];
                    }
                }
                AlertDialog.Builder ab = new AlertDialog.Builder(delete_category.this);
                ab.setMessage("Are you sure you want to\ndelete this section of your menu???\n\nItems within this section\nwill also be removed").setCancelable(false);
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(final_str.equals(" "))
                        {
                            mref.child("mj_cat").setValue(null);
                        }
                        else
                        {
                            mref.child("mj_cat").setValue(final_str);
                        }
                        mref2.child(title).setValue(null);
                        Toast.makeText(delete_category.this,"Section has been deleted",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(delete_category.this,MainActivity.class));
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

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(delete_category.this,MainActivity.class);
        startActivity(i);

    }

}
