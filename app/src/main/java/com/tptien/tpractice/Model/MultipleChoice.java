package com.tptien.tpractice.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MultipleChoice implements Parcelable {
    @SerializedName("idquestion")
    @Expose
    private String idQuestion;
    @SerializedName("idanswer")
    @Expose
    private String idAnswer;
    @SerializedName("answer")
    @Expose
    private String  answer;



    public MultipleChoice(String answer) {
        this.answer = answer;
    }

    protected MultipleChoice(Parcel in) {
        idQuestion = in.readString();
        idAnswer = in.readString();
        answer = in.readString();
    }

    public static final Creator<MultipleChoice> CREATOR = new Creator<MultipleChoice>() {
        @Override
        public MultipleChoice createFromParcel(Parcel in) {
            return new MultipleChoice(in);
        }

        @Override
        public MultipleChoice[] newArray(int size) {
            return new MultipleChoice[size];
        }
    };

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdAnswer() {
        return idAnswer;
    }

    public void setIdAnswer(String idAnswer) {
        this.idAnswer = idAnswer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idQuestion);
        parcel.writeString(idAnswer);
        parcel.writeString(answer);
    }
}
