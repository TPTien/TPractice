package com.tptien.tpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class RegistrationActivity extends AppCompatActivity {
    private EditText edt_nameDisplay,edt_userName,edt_password;
    private Button btn_register,btn_login;
    private String nameDisplay,userName,password;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edt_nameDisplay=(EditText)findViewById(R.id.edt_nameDisplay);
        edt_userName=(EditText)findViewById(R.id.edt_username);
        edt_password=(EditText)findViewById(R.id.edt_password);
        btn_register=(Button)findViewById(R.id.btn_register);
        btn_login=(Button)findViewById(R.id.btn_login1);
        mProgressBar=(ProgressBar)findViewById(R.id.pbRegisterLoading);
        mProgressBar.setVisibility(View.GONE);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                nameDisplay=edt_nameDisplay.getText().toString();
                userName=edt_userName.getText().toString();
                password=edt_password.getText().toString();
                registerYourAccount(nameDisplay,userName,password);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }
    private void registerYourAccount(String nameDisplay,String userName,String password){
        if(nameDisplay.isEmpty() || userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_SHORT).show();
        }else {
            btn_register.setClickable(false);
            mProgressBar.setVisibility(View.VISIBLE);
            DataService dataService = APIService.getService();
            Observable<User>observable =dataService.registerAccount(userName,password,nameDisplay);
            observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<User>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(User user) {

                            Log.d("response",user.getResponse());
                            if(user.getResponse().equals("ok")){
                                Toast.makeText(RegistrationActivity.this,"Tạo tài khoản thành công!",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegistrationActivity.this,"Tài khoản đã tồn tại!",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            btn_register.setClickable(true);
                            mProgressBar.setVisibility(View.GONE);
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