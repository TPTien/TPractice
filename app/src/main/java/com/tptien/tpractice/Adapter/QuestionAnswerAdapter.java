package com.tptien.tpractice.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tptien.tpractice.Model.MultipleChoice;
import com.tptien.tpractice.Model.QuestionAndAnswer;
import com.tptien.tpractice.R;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder>{
    private ArrayList<QuestionAndAnswer> mListQuestion;
    private Context mContext;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<MultipleChoice> listAnswer;
    private QuestionCardListener mCardListener;
    public   AnswerAdapter answerAdapter;
    private char temp ='B';
    private List<String> answer;

    public interface QuestionCardListener{
        void onButtonDeleteClick(int position);
        void onSelectImageClick(int position);
        void onAddAnswerClick(int position);
    }
    public void setOnClickListener(QuestionCardListener mCardListener){
        this.mCardListener=mCardListener;
    }

    public QuestionAnswerAdapter(Context mContext,ArrayList<QuestionAndAnswer> mListQuestion) {
        this.mListQuestion =mListQuestion;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(mContext).inflate(R.layout.cardview_question_answers,parent,false);
        ViewHolder viewHolder =new ViewHolder(v,mCardListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionAndAnswer currentQuestion=mListQuestion.get(position);
        holder.edt_question.setText(currentQuestion.getQuestion());
        holder.edt_explain.setText(currentQuestion.getExplain());

        if(currentQuestion.getBitmap() !=null){
            holder.imgQuestion.setImageBitmap(currentQuestion.getBitmap());
            holder.imgQuestion.setVisibility(View.VISIBLE);
        }


        //
        listAnswer=new ArrayList<>();

        listAnswer =currentQuestion.getListAnswer();
        linearLayoutManager=new LinearLayoutManager(mContext);
        holder.recyclerViewQuestion.setLayoutManager(linearLayoutManager);
        answerAdapter =new AnswerAdapter(listAnswer,mContext,0,null,null);
        answerAdapter.setOnClickClearOnClick(new AnswerAdapter.ButtonClearOnClick() {
            @Override
            public void onButtonClearClick(int position1) {
                mListQuestion.get(position).getListAnswer().remove(position1);
                Log.d("position clear", String.valueOf(position1));
                answerAdapter.notifyItemRemoved(position1);
                answerAdapter.notifyDataSetChanged();
                notifyDataSetChanged();


            }

            @Override
            public void onSelectAnswerClick(int position, String answer) {

            }
        });

        holder.recyclerViewQuestion.setAdapter(answerAdapter);

        holder.btn_addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListQuestion.get(position).getListAnswer().add(new MultipleChoice(String.valueOf(temp) +"."+" "));

                Log.d("add_size", String.valueOf(mListQuestion.get(position).getListAnswer().size()));
                //answerAdapter.notifyItemInserted(position);
                answerAdapter.notifyDataSetChanged();

//                answerAdapter.notifyDataSetChanged();
                notifyDataSetChanged();
                temp++;
            }
        });
        //get hint answers

        answer =new ArrayList<>();
        for (int i= 0;i<mListQuestion.get(position).getListAnswer().size();i++){
            answer.add(mListQuestion.get(position).getListAnswer().get(i).getAnswer());
        }
        Log.d("listSize", String.valueOf(answer.size()));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,answer);
        holder.edt_correctAnswer.setAdapter(adapter);
        holder.edt_correctAnswer.setText(currentQuestion.getCorrectAnswer());

    }
    private void getAllAnswer(int pos){

    }
    public void removeItem(int position){
        mListQuestion.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(QuestionAndAnswer questionAndAnswer, int position){
        mListQuestion.add(position,questionAndAnswer);
        notifyItemInserted(position);
    }
    public ArrayList<QuestionAndAnswer>getData(){
        return mListQuestion;
    }

    @Override
    public int getItemCount() {
        return this.mListQuestion.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText edt_question,edt_explain,edt_scoreQuestion;
        private AutoCompleteTextView edt_correctAnswer;
        private ImageButton btn_selectImage,btn_deleteQuestion,btn_addAnswer;
        private ImageView imgQuestion;
        private RecyclerView recyclerViewQuestion;
        private QuestionCardListener mCardListener;
        public ViewHolder(@NonNull View itemView, final QuestionCardListener mCardListener) {
            super(itemView);
            edt_correctAnswer =(AutoCompleteTextView) itemView.findViewById(R.id.edt_correctAnswer);
            edt_scoreQuestion =(EditText)itemView.findViewById(R.id.edt_scoreQuestion);
            btn_deleteQuestion=(ImageButton)itemView.findViewById(R.id.btn_clearQuestion);
            edt_question=(EditText)itemView.findViewById(R.id.edt_question);
            edt_explain=(EditText)itemView.findViewById(R.id.edt_response);
            btn_selectImage=(ImageButton)itemView.findViewById(R.id.btn_selectPicture);
            imgQuestion=(ImageView)itemView.findViewById(R.id.img_question);
            btn_addAnswer=(ImageButton)itemView.findViewById(R.id.btn_addAnswer);
            recyclerViewQuestion=(RecyclerView)itemView.findViewById(R.id.recyclerView_multiple_choice);
            //
            this.mCardListener =mCardListener;

            btn_selectImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    mCardListener.onSelectImageClick(position);
                }
            });
            btn_deleteQuestion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    mCardListener.onButtonDeleteClick(position);
                }
            });
            //add answer button
            btn_addAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =getAdapterPosition();
                    mCardListener.onAddAnswerClick(position);
                }
            });
            //score
            edt_scoreQuestion.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mListQuestion.get(getAdapterPosition()).setScore(charSequence.toString());
                }
                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            //question
            edt_question.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mListQuestion.get(getAdapterPosition()).setQuestion(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            //explain
            edt_explain.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mListQuestion.get(getAdapterPosition()).setExplain(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            edt_correctAnswer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mListQuestion.get(getAdapterPosition()).setCorrectAnswer(charSequence.toString());
                    //notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }
}
