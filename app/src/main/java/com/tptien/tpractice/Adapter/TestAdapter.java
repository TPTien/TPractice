package com.tptien.tpractice.Adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tptien.tpractice.Fragment.NewestFragment;
import com.tptien.tpractice.Model.Test;
import com.tptien.tpractice.R;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>  implements Filterable {
    private List<Test> mListTest;
    private List<Test>mListTestFull;
    private Context mContext;
    private boolean on_attach = true;
    private long DURATION =500;
    private CardTestListener mCardTestListener;

    @Override
    public Filter getFilter() {
        return testFilter;
    }
    private Filter testFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint ==null ||constraint.length()==0){
                mListTestFull=mListTest;
            }
            else {
                ArrayList<Test> filteredListCourse = new ArrayList<>();
//
                String filterPattern =constraint.toString().toLowerCase().trim();
                Log.v("filter",filterPattern);
                for (Test item : mListTest){
                    if(item.getTopic() ==null){
                        if(item.getName().toLowerCase().contains(filterPattern)
                                ||item.getUsername().toLowerCase().contains(filterPattern)){
                            filteredListCourse.add(item);
                        }
                    }
                    else if(item.getName().toLowerCase().contains(filterPattern)
                            ||item.getUsername().toLowerCase().contains(filterPattern)
                            ||item.getTopic().toLowerCase().contains(filterPattern)){
                        filteredListCourse.add(item);
                        Log.v("name",item.getName());
                    }
                }
                mListTestFull = filteredListCourse;
            }
            FilterResults results =new FilterResults();
            results.values=mListTestFull;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if(filterResults.values!=null){
                mListTestFull=(ArrayList<Test>)filterResults.values;
            }
            notifyDataSetChanged();
        }
    };

    public interface CardTestListener{
        void onCardClick(int position);
        void onFavoriteClick(int pos);
    }
    public void setOnClickListener(CardTestListener mCardTestListener){
        this.mCardTestListener =mCardTestListener;
    }
    public TestAdapter(List<Test> mListTest, Context mContext) {
        this.mListTest = mListTest;
        this.mContext = mContext;
        this.mListTestFull=mListTest;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_test_infor,parent,false);
        ViewHolder viewHolder =new ViewHolder(v,mCardTestListener);
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Test  currentTest= mListTestFull.get(position);
        holder.tv_name.setText(currentTest.getName());
        holder.tv_topic.setText(currentTest.getTopic());
        holder.tv_time.setText(currentTest.getTime().concat(" phút"));
        holder.tv_member.setText(currentTest.getNumTester().concat(" học viên"));
        holder.tv_host.setText(currentTest.getUsername());
        setAnimation(holder.itemView,position);
    }
    private void setAnimation(View itemView, int i) {
        if(!on_attach){
            i = -1;
        }
        boolean isNotFirstItem = i == -1;
        i++;
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", 0.f, 0.5f, 1.0f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animator.setStartDelay(isNotFirstItem ? DURATION / 2 : (i * DURATION / 3));
        animator.setDuration(DURATION);
        animatorSet.play(animator);
        animator.start();
    }
    private void FromRightToLeft(View itemView, int i) {
        if(!on_attach){
            i = -1;
        }
        boolean not_first_item = i == -1;
        i = i + 1;
        itemView.setTranslationX(itemView.getX() + 400);
        itemView.setAlpha(0.f);
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getX() + 400, 0);
        ObjectAnimator animatorAlpha = ObjectAnimator.ofFloat(itemView, "alpha", 1.f);
        ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
        animatorTranslateY.setStartDelay(not_first_item ? DURATION : (i * DURATION));
        animatorTranslateY.setDuration((not_first_item ? 2 : 1) * DURATION);
        animatorSet.playTogether(animatorTranslateY, animatorAlpha);
        animatorSet.start();
    }
    @Override
    public int getItemCount() {
        return mListTestFull.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name,tv_topic,tv_time,tv_member,tv_host;
        private ImageButton btn_favorite;
        private CardTestListener mCardTestListener;
        public ViewHolder(@NonNull View itemView, final  CardTestListener mCardTestListener) {
            super(itemView);
            tv_name =(TextView)itemView.findViewById(R.id.tv_name);
            tv_topic =(TextView)itemView.findViewById(R.id.tv_topic);
            tv_time =(TextView)itemView.findViewById(R.id.tv_time);
            tv_member =(TextView)itemView.findViewById(R.id.tv_numMember);
            tv_host =(TextView)itemView.findViewById(R.id.tv_host);
            btn_favorite=(ImageButton)itemView.findViewById(R.id.btn_favorite);
            this.mCardTestListener =mCardTestListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    mCardTestListener.onCardClick(pos);
                }
            });
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =getAdapterPosition();
                    mCardTestListener.onFavoriteClick(pos);
                }
            });
        }
    }
}
