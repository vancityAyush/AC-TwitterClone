package com.ak11.ac_twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUp extends AppCompatActivity implements View.OnClickListener{
    private EditText edtEmail, edtUsername, edtPassword;
    private Button btnSingUp, btnLogin;
    private  final String USERNAME_KEY="username", EMAIL_KEY="email", PASSWORD_KEY="password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSingUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        btnSingUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnSingUp);
                }
                return false;
            }
        });

        if(ParseUser.getCurrentUser()!=null){
            FancyToast.makeText(this,"Welcome "+ParseUser.getCurrentUser().getUsername(),
                    Toast.LENGTH_SHORT,FancyToast.DEFAULT,false).show();
            Intent intent = new Intent(SignUp.this,TwitterUsers.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnSignUp:
                if(edtEmail.getText().toString().isEmpty() || edtUsername.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                    FancyToast.makeText(this,"Email, Username, Password required",
                            FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
                }
                else{
                    ParseUser user = new ParseUser();
                    user.setEmail(edtEmail.getText().toString());
                    user.setUsername(edtUsername.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Signing Up "+edtUsername.getText());
                    progressDialog.show();
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(SignUp.this,
                                        edtUsername.getText() + " created successfully\nPlease verify Email to Login",
                                        FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                Intent intent = new Intent(SignUp.this, Login.class);
                                intent.putExtra("email", edtEmail.getText().toString());
                                intent.putExtra("password", edtPassword.getText().toString());
                                startActivity(intent);
                                ParseUser.logOut();

                            } else {
                                FancyToast.makeText(SignUp.this,
                                        e.getMessage(),
                                        FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                                e.printStackTrace();
                            }
                            progressDialog.dismiss();
                        }
                    });

                }

                break;
            case R.id.btnLogin:
                Intent intent = new Intent(SignUp.this,Login.class);
                startActivity(intent);
                break;
            case R.id.SignUpLayout:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                break;
        }
    }

}