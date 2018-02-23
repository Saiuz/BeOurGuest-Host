package com.lalit.hostbarcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class full_edit extends AppCompatActivity {
TextView category;
    EditText name,price;
    Spinner spinner_type;
    SharedPreferences sp;
Button done,delete;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    String type,a,b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_full_edit);
        Intent i = getIntent();
        String details = i.getStringExtra("detail1");
        final String key = i.getStringExtra("detail2");
        String x[]=details.split("_acb@#@xzy_");
//        Firebase.setAndroidContext(this);
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String res_name = sp.getString("name"," ");
        String item_name=i.getStringExtra("category");
        name=(EditText)findViewById(R.id.name);
        price=(EditText)findViewById(R.id.price);
        name.setText(x[0]);
        price.setText(x[2]);
        spinner_type=(Spinner)findViewById(R.id.spinner_type);
        final String arr[] = {"Veg","Non-Veg","Contain Egg","Hot","Cold"};
        int hj=0;
        for(int j=0;j<5;j++)
        {
            if(arr[j].equalsIgnoreCase(x[1]))
            {
                hj = j;
                break;
            }
        }
        type = x[1];
        spinner_type.setSelection(hj);
        category = (TextView)findViewById(R.id.category);
        category.setText(item_name);
        done = (Button)findViewById(R.id.done);
        delete = (Button)findViewById(R.id.delete);
//        mref=new Firebase("https://be-our-guest-host.firebaseio.com/"+res_name+"/menu/"+item_name);
        mref = database.getReference(res_name+"/menu/"+item_name);

        spinner_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = arr[position];
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(full_edit.this);
                ab.setMessage("Are you sure you want to delete this item???\nItem will permanently removed from menu listing.").setCancelable(false);
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mref.child(key).setValue(null);
                        Toast.makeText(full_edit.this,"Item has been deleted",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(full_edit.this,main_menu.class));
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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a= name.getText().toString().trim();
                b = price.getText().toString().trim();

                AlertDialog.Builder ab = new AlertDialog.Builder(full_edit.this);
                ab.setMessage("Do want to update this item info???\nNew values are\nName : "+a+"\nType : "+type+"\nPrice : Rs."+b).setCancelable(false);
                ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mref.child(key).setValue(a+"_acb@#@xzy_"+type+"_acb@#@xzy_"+b);
                        Toast.makeText(full_edit.this,"Item has been updated",Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(new Intent(full_edit.this,main_menu.class));
                    }
                });
                ab.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }

                });

                AlertDialog ax = ab.create();
                ax.show();
                //finish();
                //  Intent i = new Intent(loginlogup.this,Main2Activity.class);
                // startActivity(i);


            }
        });
    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(full_edit.this,main_menu.class);
        startActivity(i);

    }

}
