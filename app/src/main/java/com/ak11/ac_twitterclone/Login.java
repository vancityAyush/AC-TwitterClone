package com.ak11.ac_twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsernameEmail, edtPassword;
    private Button btnLogin, btnSignUp;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        edtUsernameEmail = findViewById(R.id.edtUsernameEmail);
        edtPassword = findViewById(R.id.edtLoginPassword);

        btnLogin = findViewById(R.id.btnLoginLogin);
        btnSignUp = findViewById(R.id.btnLoginSingup);
        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        if(getIntent().getExtras()!=null){
            edtUsernameEmail.setText(getIntent().getStringExtra("email"));
            edtPassword.setText(getIntent().getStringExtra("password"));
        }

        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
                    onClick(btnLogin);
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLoginLogin:
                username = edtUsernameEmail.getText().toString();
                if (username.indexOf("@")!=-1) {
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email",username);
                    query.getFirstInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser object, ParseException e) {
                            if(e==null && object!=null){
                                Login.this.username=object.getUsername();
                            }
                            else {
                                FancyToast.makeText(Login.this,e.getMessage(), Toast.LENGTH_SHORT,
                                        FancyToast.ERROR,false).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }
                ParseUser.logInInBackground(username, edtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null && e==null){
                                FancyToast.makeText(Login.this,user.getUsername()+" Logged In successfully",
                                        FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();
                            Intent intent = new Intent(Login.this,TwitterUsers.class);
                            startActivity(intent);
                            finish();
                            }
                        else {
                            FancyToast.makeText(Login.this,e.getMessage(), Toast.LENGTH_SHORT,
                                    FancyToast.ERROR,false).show();
                            e.printStackTrace();

                        }
                    }
                });
                break;
            case R.id.btnLoginSingup:
                finish();
                break;
            case R.id.LoginLayout:
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                break;
        }
    }
}