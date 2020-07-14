package com.tptien.tpractice.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tptien.tpractice.Activity.MoreActivity;
import com.tptien.tpractice.Activity.TestDetailActivity;
import com.tptien.tpractice.Adapter.TestAdapter;
import com.tptien.tpractice.Model.Test;
import com.tptien.tpractice.R;
import com.tptien.tpractice.Service.APIService;
import com.tptien.tpractice.Service.DataService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewestFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<Test> testList =new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String idHost=null;
    private TestAdapter mTestAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_more;
    private List<Test> mListFavorite =new ArrayList<>();
    private DataFavoriteListener mDataFavoriteListener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_newest, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharedPreferences= getContext().getSharedPreferences("loginAccount", Context.MODE_PRIVATE);
        idHost= mSharedPreferences.getString("idUser",null);
        Log.d("idUserInFragment1",idHost);
        mRecyclerView=view.findViewById(R.id.recyclerView_newest);
        mSwipeRefreshLayout= view.findViewById(R.id.swipe_refresh_new);
        tv_more= view.findViewById(R.id.btn_more);
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getContext(), MoreActivity.class);
                intent.putExtra("option","Newest");
                startActivity(intent);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewestTestData(idHost);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getNewestTestData(idHost);
        //


    }
    private void getNewestTestData(String idHost){
        DataService dataService= APIService.getService();
        Observable<List<Test>> observable = dataService.getNewestTest(idHost,"0","");
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Test>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Test> tests) {
                        testList =tests;
                        mTestAdapter =new TestAdapter(testList,getContext());
                        mRecyclerView.setAdapter(mTestAdapter);
                        mTestAdapter.setOnClickListener(new TestAdapter.CardTestListener() {
                            @Override
                            public void onCardClick(int position) {
                                Intent intent =new Intent(getContext(), TestDetailActivity.class);
                                intent.putExtra("idTest",testList.get(position).getIdTest());
                                startActivity(intent);
                                //getActivity().finish();
                            }

                            @Override
                            public void onFavoriteClick(int pos) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.addToFavoriteList)
                                        .setMessage("Bạn có muốn thêm "+tests.get(pos).getName()+" vào danh sách yêu thích?")
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                mListFavorite.add(testList.get(pos));
                                                insertToListFavorite(idHost,testList.get(pos).getIdTest());
                                                Log.d("favoriteList", String.valueOf(mListFavorite.size()));
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
                                //
//                                Bundle bundle =new Bundle();
//                                //bundle.putParcelable("favorite", (Parcelable) mListFavorite);
//                                bundle.putString("id","abc");
//                                FavoriteFragment favoriteFragment =new FavoriteFragment();
//                                favoriteFragment.setArguments(bundle);

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
                            Toast.makeText(getContext(),R.string.addFavoriteSuccess,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"Đã tồn tại!",Toast.LENGTH_SHORT).show();
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



//    @Override
//    public void onAttach(@NonNull Activity activity) {
//        super.onAttach(activity);
//        try {
//            mDataFavoriteListener= (DataFavoriteListener) activity;
//        }catch (ClassCastException e){
//            e.printStackTrace();
//        }
//    }

    public interface DataFavoriteListener{
        void getListFavorite(List<Test> testList);
    }
}