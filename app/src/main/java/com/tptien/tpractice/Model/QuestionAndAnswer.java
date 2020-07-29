package com.tptien.tpractice.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuestionAndAnswer implements Parcelable {
    @SerializedName("idquestion")
    @Expose
    private String idQuestion;
    @SerializedName("idtest")
    @Expose
    private String idTest;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("imgquestion")
    @Expose
    private String imgUrlQuestion;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("response")
    @Expose
    private String explain;
    private Bitmap bitmap;
    private String realPathImage;

    private ArrayList<MultipleChoice> listAnswer;
    @SerializedName("correctAnswer")
    @Expose
    private String correctAnswer;

    public QuestionAndAnswer(String question, String score, String explain, Bitmap bitmap, ArrayList<MultipleChoice> listAnswer) {
        this.question = question;
        this.score = score;
        this.explain = explain;
        this.bitmap = bitmap;
        this.listAnswer = listAnswer;
    }


    protected QuestionAndAnswer(Parcel in) {
        idQuestion = in.readString();
        idTest = in.readString();
        question = in.readString();
        imgUrlQuestion = in.readString();
        score = in.readString();
        explain = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        realPathImage = in.readString();
        listAnswer = in.createTypedArrayList(MultipleChoice.CREATOR);
        correctAnswer = in.readString();
    }

    public static final Creator<QuestionAndAnswer> CREATOR = new Creator<QuestionAndAnswer>() {
        @Override
        public QuestionAndAnswer createFromParcel(Parcel in) {
            return new QuestionAndAnswer(in);
        }

        @Override
        public QuestionAndAnswer[] newArray(int size) {
            return new QuestionAndAnswer[size];
        }
    };

    public QuestionAndAnswer(String idQuestion, String correctAnswer, String score) {
            this.idQuestion =idQuestion;
            this.correctAnswer=correctAnswer;
            this.score=score;
    }

    public String getRealPathImage() {
        return realPathImage;
    }

    public void setRealPathImage(String realPathImage) {
        this.realPathImage = realPathImage;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getImgUrlQuestion() {
        return imgUrlQuestion;
    }

    public void setImgUrlQuestion(String imgUrlQuestion) {
        this.imgUrlQuestion = imgUrlQuestion;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public ArrayList<MultipleChoice> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(ArrayList<MultipleChoice> listAnswer) {
        this.listAnswer = listAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idQuestion);
        parcel.writeString(idTest);
        parcel.writeString(question);
        parcel.writeString(imgUrlQuestion);
        parcel.writeString(score);
        parcel.writeString(explain);
        parcel.writeParcelable(bitmap, i);
        parcel.writeString(realPathImage);
        parcel.writeTypedList(listAnswer);
        parcel.writeString(correctAnswer);
    }
}
