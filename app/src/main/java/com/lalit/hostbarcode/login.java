package com.lalit.hostbarcode;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String email,password;
TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        if(!netcheck.isInternetAvailable(login.this)) //returns true if internet available
        {
            finish();
            Intent i = new Intent(login.this,nonet.class);
            startActivity(i);
        }
//        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        editor.putInt("status",0);
        editor.commit();
        tv = (TextView)findViewById(R.id.textView2);
        Typeface t = Typeface.createFromAsset(getAssets(),"Pacifico-Regular.ttf");
        tv.setTypeface(t);

        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(login.this, forgot.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter email address");
                    inputEmail.requestFocus();
                    /*Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();*/
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    inputPassword.setError("Enter Password");
                    inputPassword.requestFocus();
                    /*Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    */return;
                }


                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    inputEmail.setText("");
                                    inputPassword.setText("");
                                    AlertDialog.Builder ab = new AlertDialog.Builder(login.this);
                                    ab.setMessage("Authentication failed, check your email-id and password").setCancelable(false);
                                    ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog2, int which) {
                                                    dialog2.cancel();

                                                }
                                            }
                                    );
                                    AlertDialog a = ab.create();
                                    a.setTitle("Error !!!");
                                    a.show();
                                } else {
                                    editor.putString("user",email);
                                    editor.putInt("status",1);
                                    editor.commit();
                                    Toast.makeText(login.this,"Sign in process completed successfully",Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);


                                }
                            }
                        });

            }
        });
    }
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    public void onBackPressed()
    {
        AlertDialog.Builder ab = new AlertDialog.Builder(login.this);
        ab.setMessage("Are you sure you want to exit ???").setCancelable(false);
        ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
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



