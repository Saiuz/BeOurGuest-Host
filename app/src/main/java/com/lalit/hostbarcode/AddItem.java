package com.lalit.hostbarcode;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddItem extends AppCompatActivity {
EditText name,category,price;
    Button done;
    Spinner spinner_type,spinner_category;
    LinearLayout llb;
    ImageView bc;
    TextView category_tv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    SharedPreferences sp;
    String cat,type;
    String hj= " ";
    String g[];
    int aj = 0;
    int d =0;
    int y= 0;
    String arr[] = {"Veg","Non-Veg","Contain Egg","Hot (for Beverages)","Cold (for Beverages)"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_iem);
        if(!netcheck.isInternetAvailable(AddItem.this)) //returns true if internet available
        {
            finish();
            Intent i = new Intent(AddItem.this,nonet.class);
            startActivity(i);
        }
        bc =(ImageView)findViewById(R.id.bc);
        category_tv = (TextView)findViewById(R.id.category_tv);
        llb = (LinearLayout)findViewById(R.id.llb);
        done = (Button)findViewById(R.id.done);
        name = (EditText) findViewById(R.id.name);
        price = (EditText) findViewById(R.id.price);
        category = (EditText)findViewById(R.id.category);
        spinner_category = (Spinner)findViewById(R.id.spinner_category);
        spinner_type = (Spinner)findViewById(R.id.spinner_type);
//        Firebase.setAndroidContext(this);
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        final String str=sp.getString("name"," ");
//        mref = new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/major_categories");
        mref = database.getReference(str+"/menu/major_categories");
        final ProgressDialog progressDialog = new ProgressDialog(AddItem.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                String str2[] = value.split("_acb@#@xzy_");
                d=1;
                hj = value;
                ArrayAdapter<String>ad = new ArrayAdapter<String>(AddItem.this,android.R.layout.simple_dropdown_item_1line,str2);
                spinner_category.setAdapter(ad);
                g=str2;
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
                    y=1;
                    progressDialog.dismiss();
                    aj=2;
                    bc.setVisibility(View.VISIBLE);
                    llb.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                    category.setVisibility(View.VISIBLE);
                    category.requestFocus();
                }
            }
        }, 8500);


        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat = g[position];
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        findViewById(R.id.existing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aj =1;
                llb.setVisibility(View.GONE);
                category_tv.setText("Category  (Tap on below field to select)");
                spinner_category.setVisibility(View.VISIBLE);
                done.setVisibility(View.VISIBLE);

            }

        });
        findViewById(R.id.new_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aj=2;
                bc.setVisibility(View.VISIBLE);
                llb.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
                category.setVisibility(View.VISIBLE);
                category.requestFocus();
            }});
        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position<3)
                {
                    type = arr[position];
                }
                else
                    if(position==3)
                    {
                        type = "Hot";
                    }
                    else
                        if(position==4)
                        {
                            type = "Cold";
                        }
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
done.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String na = name.getText().toString().trim();
        String pr = price.getText().toString().trim();
        if (TextUtils.isEmpty(na)) {
            name.setError("Enter Item Name");
            name.requestFocus();
                    /*Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();*/
            return;
        }
        if (TextUtils.isEmpty(pr)) {
            price.setError("Enter Price");
            price.requestFocus();
                    /*Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();*/
            return;
        }

        if(aj==2 )
        {
            cat = category.getText().toString().trim();
            if (TextUtils.isEmpty(cat)) {
                category.setError("Enter Category Name");
                category.requestFocus();
                    /*Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();*/
                return;
            }
            if(y==0) {
                int r = 0;
                for (int i = 0; i < g.length; i++) {
                    if (cat.equalsIgnoreCase(g[i])) {
                        category.setError("Cagtegory already exists");
                        category.setText("");
                        category.requestFocus();
                        r = 1;
                        break;

                    }
                }
                if (r == 1) {
                    return;
                }
            }
//                Firebase mref2 = new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/major_categories/");
            DatabaseReference mref2 = database.getReference(str+"/menu/major_categories/");
            if(hj.equals(" "))
            {
                mref2.child("mj_cat").setValue(cat);
            }
            else
                {
            mref2.child("mj_cat").setValue(hj+"_acb@#@xzy_"+cat);
            }
        }
//        Firebase mref3 = new Firebase("https://be-our-guest-host.firebaseio.com/"+str+"/menu/");
        DatabaseReference mref3 = database.getReference(str+"/menu/");
        mref3.child(cat).push().setValue(na+"_acb@#@xzy_"+type+"_acb@#@xzy_"+pr);
        finish();
        Intent i = new Intent(AddItem.this, MainActivity.class);
        startActivity(i);

    }
});
            }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(AddItem.this,MainActivity.class);
        startActivity(i);

    }

}
