package com.tptien.tpractice.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class DoTestAdapter extends RecyclerView.Adapter<DoTestAdapter.ViewHolder> {
    private List<QuestionAndAnswer> mListTest;
    private Context mContext;
    private AnswerAdapter mAnswerAdapter;
    private List<MultipleChoice> choiceList;
    private LinearLayoutManager linearLayoutManager;
    private CardDoTestListener cardDoTestListener;
    private int prePosition =-1;
    private List<String> yourAnswer =new ArrayList<>();
    private List<String>yourAnswerResult =new ArrayList<>();
    private boolean isHave = false;
    private int choice =0;
    //private List<MultipleChoice>multipleChoiceList;
    public interface CardDoTestListener{
        void onBackClick(int pos);
        void onNextClick(int pos);
    }
    public DoTestAdapter(List<QuestionAndAnswer> mListTest1, Context mContext,int choice,List<String>yourAnswer) {
        this.mListTest = mListTest1;
        this.mContext = mContext;
        this.choice =choice;
        this.yourAnswerResult =yourAnswer;
    }
    public void setOnClickListener(CardDoTestListener clickListener){
        this.cardDoTestListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cardview_test_detail,parent,false);
        return new ViewHolder(view,cardDoTestListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionAndAnswer currentItem =mListTest.get(position);
        //set null answer
        String pos = String.valueOf(position+1);
        holder.tv_question.setText(pos+". "+currentItem.getQuestion());
//        if(currentItem.getImgUrlQuestion()==null){
//            holder.img_question.setVisibility(View.GONE);
//        }
        if(currentItem.getImgUrlQuestion()!=null){
            holder.img_question.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(currentItem.getImgUrlQuestion()).into(holder.img_question);
        }else {
            holder.img_question.setVisibility(View.GONE);
        }

        if(choice ==0){
            holder.tv_explain.setVisibility(View.GONE);
            choiceList=new ArrayList<>();
            choiceList =currentItem.getListAnswer();
            linearLayoutManager=new LinearLayoutManager(mContext);
            holder.mRecyclerView.setLayoutManager(linearLayoutManager);
            mAnswerAdapter =new AnswerAdapter(currentItem.getListAnswer(),mContext,1,null,null);
            mAnswerAdapter.setOnClickClearOnClick(new AnswerAdapter.ButtonClearOnClick() {
                @Override
                public void onButtonClearClick(int position1) {
//                mListQuestion.get(position).getListAnswer().remove(position1);
//                Log.d("position clear", String.valueOf(position1));
//                answerAdapter.notifyItemRemoved(position1);
//                answerAdapter.notifyDataSetChanged();
//                notifyDataSetChanged();
                }
                @Override
                public void onSelectAnswerClick(int position1, String answer) {
                    if(!yourAnswer.contains(answer)){
                        yourAnswer.add(position,answer);
                    }

                    for(int i=0;i<mListTest.get(position).getListAnswer().size();i++){
                        if(mListTest.get(position).getListAnswer().get(i).getAnswer().equals(answer)){
                            isHave =true;
                            break;
                        }
                    }
                    if(isHave){
                        yourAnswer.set(position,answer);
                    }else {
                        yourAnswer.add(position,answer);
                    }

                    Log.d("yourAnswerInAdapter",yourAnswer.get(position) + yourAnswer.size());

                }
            });
            holder.mRecyclerView.setAdapter(mAnswerAdapter);
            if(position==0){
                holder.btn_back.setVisibility(View.GONE);
            }else if(position >0) {
                holder.btn_next.setVisibility(View.VISIBLE);
                if (position ==mListTest.size()-1){
                    holder.btn_next.setVisibility(View.GONE);
                }
                else {
                    holder.btn_next.setVisibility(View.VISIBLE);
                }
            }
        }else {

            if(currentItem.getExplain()!=null){
                holder.tv_explain.setVisibility(View.VISIBLE);
                holder.tv_explain.setText(currentItem.getExplain());
            }
            holder.img_trueOrWrong.setVisibility(View.VISIBLE);
            holder.btn_next.setVisibility(View.GONE);
            holder.btn_back.setVisibility(View.GONE);
            holder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            if(yourAnswerResult.get(position).equals(mListTest.get(position).getCorrectAnswer())){
                holder.img_trueOrWrong.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_done_24));
                Log.d("thisIsTrue",yourAnswerResult.get(position));
                mAnswerAdapter =new AnswerAdapter(currentItem.getListAnswer(),mContext,2,currentItem.getCorrectAnswer(),null);

            }else {
                holder.img_trueOrWrong.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_wrong_24));
                mAnswerAdapter =new AnswerAdapter(currentItem.getListAnswer(),mContext,3,currentItem.getCorrectAnswer(),yourAnswerResult.get(position));
            }
            holder.mRecyclerView.setAdapter(mAnswerAdapter);
        }



    }
    public List<QuestionAndAnswer>getData(){
        return this.mListTest;
    }
    public List<String> getYourAnswer(){
        return this.yourAnswer;
    }

    @Override
    public int getItemCount() {
        return mListTest.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_question,tv_score,tv_explain;
        private ImageView img_trueOrWrong,img_question;
        private Button btn_back,btn_next;
        private RecyclerView mRecyclerView;
        private CardDoTestListener cardDoTestListener;
        public ViewHolder(@NonNull View itemView, final CardDoTestListener cardDoTestListener) {
            super(itemView);
            mRecyclerView =(RecyclerView)itemView.findViewById(R.id.recyclerView_answer);
            tv_question=(TextView)itemView.findViewById(R.id.tv_question);
            tv_score=(TextView)itemView.findViewById(R.id.tv_score);
            tv_explain=(TextView)itemView.findViewById(R.id.tv_explain);
            img_question=(ImageView)itemView.findViewById(R.id.img_questionDetail);
            img_trueOrWrong=(ImageView)itemView.findViewById(R.id.img_trueOrWrong);
            btn_back=(Button)itemView.findViewById(R.id.btn_back);
            btn_next=(Button)itemView.findViewById(R.id.btn_next);
            this.cardDoTestListener =cardDoTestListener;

//            for(int i= 0;i <yourAnswer.size();i++){
//                yourAnswer.get(i).setCorrectAnswer("");
//            }
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    cardDoTestListener.onBackClick(pos);
                }
            });
            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    cardDoTestListener.onNextClick(pos);
                }
            });


        }
    }
}
