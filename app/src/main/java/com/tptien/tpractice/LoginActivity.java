package com.tptien.tpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tptien.tpractice.Service.APIService;
import com.tptien.tpractice.Service.DataService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_userName,edt_password;
    private Button btn_login,btn_signUp;
    private CheckBox cb_saveAccount;
    private String username,password;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Boolean isSaveAccount =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_userName=(EditText)findViewById(R.id.edt_username);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        btn_signUp=(Button)findViewById(R.id.btn_signup);
        cb_saveAccount=(CheckBox)findViewById(R.id.checkbox_login);
        progressBar=(ProgressBar)findViewById(R.id.pbHeaderProgress);

        progressBar.setVisibility(View.GONE);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=edt_userName.getText().toString();
                password=edt_password.getText().toString();
                validateYourAccount(username,password);

            }
        });
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);
            }
        });
        //
        sharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        editor =sharedPreferences.edit();
        isSaveAccount=sharedPreferences.getBoolean("saveAccount",true);
        if(isSaveAccount){
            edt_userName.setText(sharedPreferences.getString("username",null));
            edt_password.setText(sharedPreferences.getString("password",null));
            cb_saveAccount.setChecked(true);
            //Log.d("save_account",sharedPreferences.getString("username",null));
        }
    }
    private void validateYourAccount(String username,String password){
        if(username.isEmpty()|| password.isEmpty()){
            Toast.makeText(LoginActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }else {
            btn_login.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            DataService dataService= APIService.getService();
            Observable<User> observable=dataService.loginAccount(username,password);
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(User user) {


                            if(user.getResponse().equals("ok")){
                                String nameDisplay=user.getDisplayName();
                                String idUser =user.getIdUser();
                                String username=user.getUserName();
                                String password=user.getPassword();
                                String role=user.getRole();
                                Log.d("username",nameDisplay);
                                if(cb_saveAccount.isChecked()){
                                    editor.putBoolean("saveAccount",true);
                                    editor.putString("userNameDisplay",nameDisplay);
                                    editor.putString("idUser",idUser);
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.putString("role",role);
                                    editor.commit();

                                }
                                else {
                                    editor.putBoolean("saveAccount",false);
                                    editor.putString("userNameDisplay",nameDisplay);
                                    editor.putString("idUser",idUser);
                                    editor.putString("username",username);
                                    editor.putString("password",password);
                                    editor.putString("role",role);
                                    editor.apply();
                                }
                                Toast.makeText(LoginActivity.this,"Đăng nhập thành công.",Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            if(user.getResponse().equals("failed")){
                                Toast.makeText(LoginActivity.this,"Thông tin tài khoản không chính xác!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onComplete() {
                            btn_login.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    });


        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}