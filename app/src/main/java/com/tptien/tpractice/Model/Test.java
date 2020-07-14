package com.tptien.tpractice.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Test implements Parcelable {
    @SerializedName("idtest")
    @Expose
    private String idTest;
    @SerializedName("idcourse")
    @Expose
    private String idCourse;
    @SerializedName("idhost")
    @Expose
    private String idHost;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("numQuestion")
    @Expose
    private String numQuestion;
    @SerializedName("numTester")
    @Expose
    private String numTester;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("topic")
    @Expose
    private String topic;

    protected Test(Parcel in) {
        idTest = in.readString();
        idCourse = in.readString();
        idHost = in.readString();
        username = in.readString();
        name = in.readString();
        time = in.readString();
        numQuestion = in.readString();
        numTester = in.readString();
        date = in.readString();
        topic = in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getIdHost() {
        return idHost;
    }

    public void setIdHost(String idHost) {
        this.idHost = idHost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumQuestion() {
        return numQuestion;
    }

    public void setNumQuestion(String numQuestion) {
        this.numQuestion = numQuestion;
    }

    public String getNumTester() {
        return numTester;
    }

    public void setNumTester(String numTester) {
        this.numTester = numTester;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idTest);
        parcel.writeString(idCourse);
        parcel.writeString(idHost);
        parcel.writeString(username);
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeString(numQuestion);
        parcel.writeString(numTester);
        parcel.writeString(date);
        parcel.writeString(topic);
    }
}
