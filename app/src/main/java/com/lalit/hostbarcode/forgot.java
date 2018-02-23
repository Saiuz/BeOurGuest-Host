package com.lalit.hostbarcode;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class forgot extends AppCompatActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(!netcheck.isInternetAvailable(forgot.this)) //returns true if internet available
        {

            finish();
            Intent i = new Intent(forgot.this,nonet.class);
            startActivity(i);
        }

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(forgot.this,login.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError("Enter email address");
                    inputEmail.requestFocus();
                    /*Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;*/
                }

                else
                {progressBar.setVisibility(View.VISIBLE);
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AlertDialog.Builder ab = new AlertDialog.Builder(forgot.this);
                                        ab.setMessage("Please check your inbox, We have sent you instructions to reset your password!").setCancelable(false);
                                        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog2, int which) {
                                                        dialog2.cancel();
                                                        finish();
                                                        startActivity(new Intent(forgot.this,login.class));
                                                    }
                                                }
                                        );
                                        AlertDialog a = ab.create();
                                        a.show();
                                    } else {
                                        AlertDialog.Builder ab = new AlertDialog.Builder(forgot.this);
                                        ab.setMessage("Failed to send reset email !\nCheck your email id").setCancelable(false);
                                        ab.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog2, int which) {
                                                        dialog2.cancel();
                                                    }
                                                }
                                        );
                                        AlertDialog a = ab.create();
                                        a.show();
                                    }

                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }}
        });
    }
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(forgot.this,login.class);
        startActivity(i);

    }
}