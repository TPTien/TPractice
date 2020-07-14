package com.tptien.tpractice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tptien.tpractice.Adapter.DoTestAdapter;
import com.tptien.tpractice.Adapter.QuestionAnswerAdapter;
import com.tptien.tpractice.Model.MultipleChoice;
import com.tptien.tpractice.Model.QuestionAndAnswer;
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

public class TestDetailActivity extends AppCompatActivity {
    private String idTest=null;
    private ArrayList<QuestionAndAnswer>mListQuestionAndAnswers =new ArrayList<>();
    private List<MultipleChoice> multipleChoiceList =new ArrayList<>();
    private RecyclerView mRecyclerView;
    private DoTestAdapter mDoTestAdapter;
    ArrayList<MultipleChoice>listAnswer =new ArrayList<>();
    private int min ,tempMin=0;
    private int sec =60;
    Handler handler;
    private Test testInfor;
    private TextView tv_numQuestion,tv_timer,tv_score;
    private ImageButton btn_done;
    private boolean isDone =false;
    private SharedPreferences mSharedPreferences;
    private String idUser;
    private List<String> yourAnswer =new ArrayList<>();
    private List<QuestionAndAnswer> fullList =new ArrayList<>();
    private int numWrongQuestion,numRightQuestion =0 ;
    private float percentDone =0;
    private int score =0;
    private int timeDone =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detail);
        //
        mSharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        idUser =mSharedPreferences.getString("idUser",null);
        Intent intent =getIntent();
        idTest= intent.getStringExtra("idTest");
        Log.d("idTestInDetail",idTest);
        //
        tv_numQuestion =(TextView)findViewById(R.id.tv_numberQues);
        tv_timer=(TextView)findViewById(R.id.tv_clock);
        tv_score=(TextView)findViewById(R.id.tv_mark);
        btn_done = (ImageButton)findViewById(R.id.btn_done);
        // check your answer is completely done

        getDataTest(idTest);
        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                getListQuestion(idTest);
            }
        };

        handler.postDelayed(r, 2000);
        getListAnswer(idTest);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView_testDetail);
//        //for(int i=0;i<5;i++){
//
//            listAnswer.add(new MultipleChoice("A"));
//            listAnswer.add(new MultipleChoice("B"));
//            listAnswer.add(new MultipleChoice("C"));
//            listAnswer.add(new MultipleChoice("D"));
////            mListQuestionAndAnswers.add(new QuestionAndAnswer("dsadasdasdáddddddddddddddddddddddddddddddddddddddddddddddđ","dsadasd","dsadsad",null,listAnswer));
//
//        //}




    }
    private void finishTest(String idUser,String idTest,int timeDone,String percentDone,int numWrongQuestion,int numRightQuestion){
        DataService dataService=APIService.getService();
        Observable<String>observable =dataService.insertOrUpdateTester(idUser,idTest,timeDone,percentDone,numWrongQuestion,numRightQuestion);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d("UploadTester",s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void setTimer(int timer){

        min=timer*60*1000;
        tempMin =timer;
        new CountDownTimer(min,1000){

            @Override
            public void onTick(long l) {
                sec--;
                if(sec ==0){
                    sec=59;
                    tempMin--;
                }
                tv_timer.setText(tempMin-1+":"+sec);
            }

            @Override
            public void onFinish() {
                tv_timer.setText("00:00");

                Toast.makeText(TestDetailActivity.this,"Hết giờ! Bạn đã không hoàn thành bài test.",Toast.LENGTH_SHORT).show();
                finish();
                //

            }
        }.start();
    }
    private void getListAnswer(String idTest){

        DataService dataService = APIService.getService();
        Observable<List<MultipleChoice>>observable =dataService.getListAnswer(idTest);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MultipleChoice>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MultipleChoice> multipleChoices) {
                        multipleChoiceList =multipleChoices;
                        Log.d("answerListSize", String.valueOf(multipleChoiceList.size()));


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
    private void getListQuestion(String idTest){
        DataService dataService = APIService.getService();
        Observable<List<QuestionAndAnswer>>observable =dataService.getListQuestion(idTest);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<QuestionAndAnswer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<QuestionAndAnswer>questionAndAnswers) {
                        mListQuestionAndAnswers = (ArrayList<QuestionAndAnswer>) questionAndAnswers;
                        Log.d("questionListSize", String.valueOf(mListQuestionAndAnswers.size()));


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        getFullListQuestionAnswer(mListQuestionAndAnswers,multipleChoiceList);
                    }
                });
    }
    private void getFullListQuestionAnswer(List<QuestionAndAnswer> listQuestion,List<MultipleChoice>choiceList){

        for(int i=0;i<listQuestion.size();i++){
            ArrayList<MultipleChoice> listTemp= new ArrayList<>();
            for(int j=0;j<choiceList.size();j++){
                if(choiceList.get(j).getIdQuestion().equals(listQuestion.get(i).getIdQuestion())){
                    listTemp.add(choiceList.get(j));
                    Log.d("answer11111", choiceList.get(j).getAnswer());
                    //choiceList.remove(j);
                }
            }
            listQuestion.get(i).setListAnswer(listTemp);
            Log.d("list answer size", String.valueOf(listQuestion.get(i).getListAnswer().size()));


        }
        //getFullListQuestionAnswer(mListQuestionAndAnswers,multipleChoiceList);
        mDoTestAdapter=new DoTestAdapter( listQuestion,TestDetailActivity.this,0,null);
        mDoTestAdapter.setOnClickListener(new DoTestAdapter.CardDoTestListener() {
            @Override
            public void onBackClick(int pos) {
                if(pos>0){
                    mRecyclerView.smoothScrollToPosition(pos-1);
                }

            }

            @Override
            public void onNextClick(int pos) {
                mRecyclerView.smoothScrollToPosition(pos+1);

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(TestDetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(mDoTestAdapter);


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yourAnswer = mDoTestAdapter.getYourAnswer();
                Log.d("yourAnswerSize", String.valueOf(yourAnswer.size()));
                fullList = (ArrayList<QuestionAndAnswer>) mDoTestAdapter.getData();
                if(yourAnswer.size()<listQuestion.size()){
                    Toast.makeText(TestDetailActivity.this, "Vui lòng hoàn thành bài test trước khi nộp!", Toast.LENGTH_SHORT).show();
                }else {
//                    if(!isDone){
//                        for(int i =0;i<yourAnswer.size();i++){
//                            if(yourAnswer.get(i).equals("")) {
//                                Toast.makeText(TestDetailActivity.this, "Vui lòng hoàn thành bài test trước khi nộp!", Toast.LENGTH_SHORT).show();
//                                mRecyclerView.smoothScrollToPosition(i);
//                                break;
//                            }else {
//                                isDone =true;
//
//
//                            }
//                        }
//
//                    }else {
                        for (int i= 0;i<yourAnswer.size();i++){
                            String correct =listQuestion.get(i).getCorrectAnswer();
                            if(yourAnswer.get(i).equals(correct)){
                                numRightQuestion ++ ;
                                score+=Integer.parseInt(listQuestion.get(i).getScore());
                                Log.d("numRight", String.valueOf(numRightQuestion));
                            }else {
                                numWrongQuestion ++;
                                Log.d("numWrong", String.valueOf(numWrongQuestion));
                            }
                            Log.d("yourAnswerInDetail",yourAnswer.get(i));
                            Log.d("correctAnswer",listQuestion.get(i).getCorrectAnswer() +listQuestion.size());
                        }
                        percentDone =(float) (numRightQuestion*100.0/listQuestion.size());
                        Log.d("percentDone", String.valueOf(percentDone));
                        Log.d("score", String.valueOf(score));
                        String[] time=tv_timer.getText().toString().split(":");
                        timeDone =Integer.parseInt(testInfor.getTime()) - Integer.parseInt(time[0]);
                        Log.d("timeDone", String.valueOf(timeDone));
                        Toast.makeText(TestDetailActivity.this,"Nộp thành công",Toast.LENGTH_SHORT).show();

                        //
                        finishTest(idUser,idTest,timeDone,String.valueOf(percentDone),numWrongQuestion,numRightQuestion);
                        Intent intent =new Intent(TestDetailActivity.this,ResultActivity.class);
                        Bundle bundle =new Bundle();
                        bundle.putParcelableArrayList("listQuestion", (ArrayList<? extends Parcelable>) listQuestion);
                        bundle.putParcelable("testInfor",testInfor);
                        bundle.putStringArrayList("listYourAnswer", (ArrayList<String>) yourAnswer);
                        bundle.putInt("timeDone",timeDone);
                        bundle.putInt("numWrong",numWrongQuestion);
                        bundle.putInt("numRight",numRightQuestion);
                        bundle.putFloat("percent",percentDone);
                        bundle.putInt("score",score);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();


                    }
                }
//                Log.d("yourAnswer",yourAnswer.get(0));
//                Log.d("correctAnswer",listQuestion.get(0).getCorrectAnswer() +fullList.size());




        });

    }
    private void getDataTest(String idTest){
        DataService dataService = APIService.getService();
        Observable<Test>observable =dataService.getDataTest(idTest);
        observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Test>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Test test) {
                        testInfor =test;
                        Log.d("testInfor", testInfor.getName());
                        setTimer(Integer.parseInt(testInfor.getTime()));
                        tv_numQuestion.setText(testInfor.getNumQuestion()+" Câu");


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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}