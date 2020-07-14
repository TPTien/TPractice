package com.tptien.tpractice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.tptien.tpractice.Adapter.DoTestAdapter;
import com.tptien.tpractice.Model.QuestionAndAnswer;
import com.tptien.tpractice.Model.Test;
import com.tptien.tpractice.R;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private PieChart mPieChart;
    private Test test;
    private  List<QuestionAndAnswer> questionAndAnswerList =new ArrayList<>();
    private ArrayList<String>yourAnswer= new ArrayList<>();
    private  int[]color =new int[]{Color.GREEN,Color.BLUE};
    private TextView tv_nameTest,tv_score;
    private RecyclerView mRecyclerView;
    private int timeDone,numRightQuestion,numWrongQuestion,score= 0;
    private float percentDone =0;
    private DoTestAdapter mTestAdapter;
    private Button btn_detailResult;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);


        Bundle bundle =getIntent().getExtras();
        questionAndAnswerList = bundle.getParcelableArrayList("listQuestion");
        Log.d("listSizeInResult", String.valueOf(questionAndAnswerList.size()));
        test =bundle.getParcelable("testInfor");
        Log.d("testInResult",test.getName());
        yourAnswer= bundle.getStringArrayList("listYourAnswer");
        Log.d("yourAnswerInResult", String.valueOf(yourAnswer.size()));
        timeDone =bundle.getInt("timeDone");
        numWrongQuestion=bundle.getInt("numWrong");
        numRightQuestion=bundle.getInt("numRight");
        percentDone = bundle.getFloat("percent");
        score = bundle.getInt("score");

        mPieChart =(PieChart)findViewById(R.id.pieChart);
        ArrayList<PieEntry> data=new ArrayList<>();
        data.add(new PieEntry((float)numRightQuestion/yourAnswer.size(),"Đúng"));
        data.add(new PieEntry((float)numWrongQuestion/yourAnswer.size(),"Sai"));
        PieDataSet pieDataSet =new PieDataSet(data,"");
        pieDataSet.setColors(color);
        PieData pieData =new PieData(pieDataSet);
        pieData.setValueTextSize(20);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(Color.WHITE);
        mPieChart.setData(pieData);
        mPieChart.setCenterText("Thành tích");
        mPieChart.setCenterTextRadiusPercent(50);
        mPieChart.setHoleRadius(25);
        mPieChart.setUsePercentValues(true);
        mPieChart.setTransparentCircleRadius(35);
        mPieChart.setTransparentCircleColor(Color.RED);
        mPieChart.setTransparentCircleAlpha(50);
        mPieChart.animateXY(1400,1400);
        mPieChart.getDescription().setText("");
        mPieChart.getDescription().setTextSize(20);
        mPieChart.invalidate();

        tv_nameTest =findViewById(R.id.nameTest);
        tv_score=(TextView)findViewById(R.id.tv_score);
        btn_detailResult=(Button)findViewById(R.id.btn_detail);
        tv_nameTest.setText(test.getName());
        tv_score.setText("Điểm: "+numRightQuestion+"/"+yourAnswer.size());
        btn_detailResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if(count%2==0){
                    mRecyclerView.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
        });


        mRecyclerView =(RecyclerView)findViewById(R.id.recyclerView_result);
        mTestAdapter =new DoTestAdapter(questionAndAnswerList,this,1,yourAnswer);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mTestAdapter);


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}