package com.tptien.tpractice.Fragment;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tptien.tpractice.Activity.TestDetailActivity;
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


public class FavoriteFragment extends Fragment implements NewestFragment.DataFavoriteListener{
    private List<Test> listFavorite =new ArrayList<>();
    private RecyclerView mRecyclerView;
    private List<Test> testList =new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String idHost=null;
    private TestAdapter mTestAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
//    public static FavoriteFragment newInstance(String a){
//        FavoriteFragment fragment =new FavoriteFragment();
//        Bundle bundle =new Bundle();
//        bundle.putString("id",a);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedPreferences= getContext().getSharedPreferences("loginAccount", Context.MODE_PRIVATE);
        idHost= mSharedPreferences.getString("idUser",null);
        Log.d("idUserInFragment1",idHost);
        mRecyclerView=view.findViewById(R.id.recyclerView_favorite);
        mSwipeRefreshLayout= view.findViewById(R.id.swipe_refresh_favor);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getYourListFavorite(idHost);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getYourListFavorite(idHost);
    }

    @Override
    public void getListFavorite(List<Test> testList) {
        listFavorite =testList;
        Log.d("listFavorite", String.valueOf(listFavorite.size()));
    }
    private void getYourListFavorite(String idUser){
        DataService  dataService = APIService.getService();
        Observable<List<Test>>observable =dataService.getNewestTest(idUser,"3","");
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Test>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Test> testList) {
                        listFavorite =testList;
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
                                        .setMessage("Bạn muốn xóa "+listFavorite.get(pos).getName()+" khỏi danh sách yêu thích?")
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(listFavorite.contains(testList.get(pos))){
                                                    insertToListFavorite(idHost,testList.get(pos).getIdTest());
                                                    listFavorite.remove(testList.get(pos));
                                                    mTestAdapter.notifyItemRemoved(pos);
                                                    mTestAdapter.notifyDataSetChanged();

                                                }


                                                Log.d("favoriteList", String.valueOf(listFavorite.size()));
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void insertToListFavorite(String idUser,String idTest){
        DataService dataService =APIService.getService();
        Observable<String> observable =dataService.insertOrDelete(idUser,"1",idTest);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        if(s.equals("done")){
                            Toast.makeText(getContext(),R.string.deleteSuccess,Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(), R.string.notExist,Toast.LENGTH_SHORT).show();
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
}