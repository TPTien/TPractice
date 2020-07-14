package com.tptien.tpractice.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.tptien.tpractice.Adapter.TestAdapter;
import com.tptien.tpractice.Model.Test;
import com.tptien.tpractice.R;
import com.tptien.tpractice.Service.APIService;
import com.tptien.tpractice.Service.DataService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MoreActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Test> testList =new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String idHost=null;
    private TestAdapter mTestAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        mSharedPreferences= getSharedPreferences("loginAccount", Context.MODE_PRIVATE);
        idHost= mSharedPreferences.getString("idUser",null);
        Log.d("idUserInMore",idHost);
        mRecyclerView=findViewById(R.id.recyclerView_more);
        mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_more);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewestTestData(idHost);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getNewestTestData(idHost);
        mEditText =(EditText)findViewById(R.id.edit_search);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTestAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getNewestTestData(String idHost) {
        DataService dataService= APIService.getService();
        Observable<List<Test>> observable = dataService.getNewestTest(idHost,"1","");
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Test>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Test> tests) {
                        testList =tests;
                        mTestAdapter =new TestAdapter(testList,MoreActivity.this);
                        mRecyclerView.setAdapter(mTestAdapter);
                        mTestAdapter.setOnClickListener(new TestAdapter.CardTestListener() {
                            @Override
                            public void onCardClick(int position) {
                                Intent intent =new Intent(MoreActivity.this, TestDetailActivity.class);
                                intent.putExtra("idTest",testList.get(position).getIdTest());
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFavoriteClick(int pos) {
                                new AlertDialog.Builder(MoreActivity.this)
                                        .setTitle(R.string.addToFavoriteList)
                                        .setMessage("Bạn có muốn thêm " +tests.get(pos).getName()+" vào danh sách yêu thích?")
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //mListFavorite.add(testList.get(pos));
                                                insertToListFavorite(idHost,testList.get(pos).getIdTest());
                                                //Log.d("favoriteList", String.valueOf(mListFavorite.size()));
                                                //Toast.makeText(getContext(),R.string.addFavoriteSuccess,Toast.LENGTH_SHORT).show();
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        })
                                        .setIcon(R.drawable.ic_baseline_favorite_24)
                                        .show();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void insertToListFavorite(String idUser,String idTest){
        DataService dataService =APIService.getService();
        Observable<String> observable =dataService.insertOrDelete(idUser,"0",idTest);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if(s.equals("done")){
                            Toast.makeText(MoreActivity.this,R.string.addFavoriteSuccess,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MoreActivity.this,"Đã tồn tại!",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


}