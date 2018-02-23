package com.lalit.hostbarcode;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
private SharedPreferences sp;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mref;
    private SharedPreferences.Editor editor;
    String tempp,name;
    String email;
    TextView nametv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor=sp.edit();
        email = sp.getString("user"," ");
        if(email.length()>20)
        {
            tempp = email.substring(0, 20).replace('.', '-').replace('#', '-')
                    .replace('$', '-').replace('[', '-').replace(']', '-');
        }
        else
        {
            tempp=email.replace('.','-').replace('#','-').replace('$','-').replace('[','-').replace(']','-');
        }
        name=sp.getString("name"," ");
        nametv = (TextView)findViewById(R.id.namet);
        Typeface t = Typeface.createFromAsset(getAssets(),"Pacifico-Regular.ttf");
        nametv.setTypeface(t);
        if(name.equals(" ")) {
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();

//            mref = new Firebase("https://be-our-guest-host.firebaseio.com/details/" + tempp);
            mref = database.getReference("details/" + tempp);
            mref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    String str[] = value.split("_acb@#@xzy_");
                    progressDialog.dismiss();
                    if (str[0].equals(email)) {
                        editor.putString("name", str[1]);
                        editor.putString("pno", str[2]);
                        editor.putString("address", str[3]);
                        editor.commit();
                    }
                    nametv.setText(str[1]);
                    Intent p = new Intent(MainActivity.this,MyService.class);
                    startService(p);
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
        }
        else {
            String hotel_name = sp.getString("name", " ");
            nametv.setText(hotel_name);

        }
        findViewById(R.id.view_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, ViewOrder.class));
            }
        });

        findViewById(R.id.edit_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,main_menu.class));
                finish();
            }
        });

        findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddItem.class));
                finish();
            }
        });

        findViewById(R.id.remove_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,delete_category.class));
                finish();
            }
        });
        findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         /*       Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND).setType("text/*").putExtra(Intent.EXTRA_EMAIL,"datastore.beourguest@gmail.com").putExtra(Intent.EXTRA_SUBJECT,"BeOurGuest-Android-Services");
                startActivity(i);*/
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"datastore.beourguest@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "BeOurGuest-Android-Services");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                           }
        });

    }
    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        ab.setMessage("Are you sure you want to exit ???").setCancelable(false);
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                finish();
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

}
