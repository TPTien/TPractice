package com.tptien.tpractice.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.tptien.tpractice.Model.MultipleChoice;
import com.tptien.tpractice.Model.QuestionAndAnswer;
import com.tptien.tpractice.R;

import java.util.ArrayList;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder>{
    private ArrayList<MultipleChoice> listAnswer;
    private Context mContext;
    private ButtonClearOnClick mButtonClearOnClick;
    private MaterialRadioButton lastCheckedRB = null;
    private static int count =0;
    private  int pos =-1;
    private int select;
    private String correctAnswer;
    private String yourAnswer;
    public AnswerAdapter(ArrayList<MultipleChoice> listAnswer,Context context,int select,String correctAnswer,String yourAnswer){
        this.listAnswer=listAnswer;
        this.mContext=context;
        this.select =select;
        this.correctAnswer=correctAnswer;
        this.yourAnswer=yourAnswer;
    }
    public interface ButtonClearOnClick{
        void onButtonClearClick(int position);
        void onSelectAnswerClick(int position, String answer);
    }
    public void setOnClickClearOnClick(ButtonClearOnClick mButtonClearOnClick){
        this.mButtonClearOnClick =mButtonClearOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_multiple_choice,parent,false);
        ViewHolder viewHolder= new ViewHolder(v,mButtonClearOnClick);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultipleChoice currentAnswer=listAnswer.get(position);
        holder.edt_answer.setText(currentAnswer.getAnswer());
        holder.radioButton.setChecked(false);
        if(select==0){
//            holder.radioButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(count>0){
//                        count=-1;
//                        holder.radioButton.setChecked(true);
//                        holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.lowGreen));
//                        holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_done_24));
//                    }else {
//                        count=1;
//                        holder.radioButton.setChecked(false);
//
//                        holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
//                        holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
//                    }
//
//                }
//            });
            holder.radioButton.setClickable(false);
        }else if(select==1) {
            holder.edt_answer.setFocusable(false);
            holder.edt_answer.setClickable(false);
            holder.edt_answer.setFocusableInTouchMode(false);
            holder.edt_answer.setCursorVisible(false);

            holder.radioButton.setChecked(position == pos);
            holder.btn_clear.setVisibility(View.GONE);
//            if(position==pos){
//                //holder.radioButton.setChecked(true);
//                Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
//                holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.lowGreen));
//            }else {
//                holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
//            }
//            holder.radioButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(count>0){
//                        count=-1;
//                        holder.radioButton.setChecked(true);
//                        Toast.makeText(mContext,"Clicked",Toast.LENGTH_SHORT).show();
//                        holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.lowGreen));
//                        //holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_done_24));
//                    }else {
//                        count=1;
//                        holder.radioButton.setChecked(false);
//                        Toast.makeText(mContext," No Clicked",Toast.LENGTH_SHORT).show();
//                        holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
//                        //holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
//                    }
//
//                }
//            });

        } else if(select ==2){
            holder.radioButton.setClickable(false);
            holder.edt_answer.setFocusable(false);
            holder.edt_answer.setClickable(false);
            holder.edt_answer.setFocusableInTouchMode(false);
            holder.edt_answer.setCursorVisible(false);
            holder.btn_clear.setVisibility(View.GONE);

            if(currentAnswer.getAnswer().equals(correctAnswer)){
                holder.radioButton.setChecked(true);
                holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.lowGreen));
                holder.btn_clear.setVisibility(View.VISIBLE);
                holder.btn_clear.setClickable(false);
                holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_done_24));

            }
        }else {
            holder.radioButton.setClickable(false);
            holder.edt_answer.setFocusable(false);
            holder.edt_answer.setClickable(false);
            holder.edt_answer.setFocusableInTouchMode(false);
            holder.edt_answer.setCursorVisible(false);
            holder.btn_clear.setVisibility(View.GONE);
            if(currentAnswer.getAnswer().equals(correctAnswer)){
                holder.radioButton.setChecked(true);
                holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.lowGreen));
                holder.btn_clear.setVisibility(View.VISIBLE);
                holder.btn_clear.setClickable(false);
                holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_done_24));

            }
            if(currentAnswer.getAnswer().equals(yourAnswer)){
                holder.radioButton.setChecked(true);
                holder.mLayout.setCardBackgroundColor(mContext.getResources().getColor(R.color.wrongColor));
                holder.btn_clear.setVisibility(View.VISIBLE);
                holder.btn_clear.setClickable(false);
                holder.btn_clear.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_baseline_clear_24));
                holder.btn_clear.setBackgroundColor(Color.RED);
                holder.btn_clear.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.wrongColor));
            }
        }

    }

    @Override
    public int getItemCount() {
        return this.listAnswer.size();
    }
    public void setItems(ArrayList<MultipleChoice> mList) {
        listAnswer.clear();
        this.listAnswer.addAll(mList);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MaterialRadioButton radioButton;
        private ImageButton btn_clear;
        private ButtonClearOnClick mButtonClearOnClick;
        private EditText edt_answer;
        private CardView mLayout;
        public ViewHolder(@NonNull View itemView,final ButtonClearOnClick mButtonClearOnClick) {
            super(itemView);
            mLayout=(CardView) itemView.findViewById(R.id.container_answer);
            radioButton=(MaterialRadioButton)itemView.findViewById(R.id.rd_choice);
            btn_clear=(ImageButton)itemView.findViewById(R.id.btn_clear);
            edt_answer =(EditText)itemView.findViewById(R.id.edt_answer);
            this.mButtonClearOnClick =mButtonClearOnClick;

            btn_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position =getAdapterPosition();
                    mButtonClearOnClick.onButtonClearClick(position);
                }
            });
            edt_answer.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listAnswer.get(getAdapterPosition()).setAnswer(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pos=getAdapterPosition();
                    notifyDataSetChanged();
                    notifyItemChanged(pos);
                    mButtonClearOnClick.onSelectAnswerClick(pos,listAnswer.get(pos).getAnswer());
                }
            });
        }
    }
}
