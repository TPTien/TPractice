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
import android.widget.Toast;

import com.tptien.tpractice.Service.APIService;
import com.tptien.tpractice.Service.DataService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WelcomeActivity extends AppCompatActivity {
    private Handler handler =new Handler();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor mEditor;
    private boolean isSaved =false;
    private String username;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        isSaved =sharedPreferences.getBoolean("saveAccount",false);
        if(isSaved) {
            username =sharedPreferences.getString("username",null);
            password =sharedPreferences.getString("password",null);
            Login(username,password);
        }

        else {
                    handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                // do something
                Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }, 1500);

        }
    }
    private void Login(String username,String password){
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
                            Intent intent =new Intent(WelcomeActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {

                            }

                        }

                    @Override
                    public void onError(Throwable e) {
                        Intent intent =new Intent(WelcomeActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }


                });


    }
}