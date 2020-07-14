package com.tptien.tpractice.Service;


import com.google.gson.JsonArray;
import com.tptien.tpractice.Model.MultipleChoice;
import com.tptien.tpractice.Model.QuestionAndAnswer;
import com.tptien.tpractice.Model.Test;
import com.tptien.tpractice.User;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DataService {

//    @GET("register.php")
//    Call<User> Register(@Query("username") String username, @Query("password") String password, @Query("displayname") String displayName);
//    @GET("login.php")
//    Call<User> Login(@Query("username") String username, @Query("password") String password);
//
//    @FormUrlEncoded
//    @POST("recentCourses.php")
////    Call<List<Course>> getRecentCourses(@Query("idcourse")String idCourse
////                                        ,@Query("coursename")String courseName
////                                        ,@Query("username")String username
////                                        ,@Query("topic")String topic,@Query("nummember")String numMember,@Query("numVoca")String numVoca);
//    Observable<List<Course>> getRecentCourses(@Field("iduser") String idUser);
//    @GET("allCourses.php")
//    Call<List<Course>> getAllCourses();
//    @GET("createdCourses.php")
//    Call<List<Course>> getCreatedCourses(@Query("iduser") String idUser);
//    @GET("joinedCourses.php")
//    Call<List<Course>> getJoinedCourses(@Query("iduser") String idUser);
//    //    Call<Course> restoreCourses(@Query("idcourse")String idCourse
////                            ,@Query("courname")String courseName
////                            ,@Query("idhost")String idHost,@Query("topic")String topic,@Query("nummember")String numMember
////                            ,@Query("numVoca")String numVocab
////                            ,@Query("datecreate")String dateCreate);
//    @GET("deleteCourse.php")
//    Call<Void> deleteCourse(@Query("idcourse") String idCourse);
//    @GET("leaveCourse.php")
//    Call<Void> leaveCourse(@Query("iduser") String idUser, @Query("idcourse") String idCourse);
//    @GET("suggestedCourses.php")
//    Call<List<Course>> getSuggestedCourses(@Query("iduser") String idUser);
//
//    @GET("friendInCourse.php")
//    Observable<List<User>> getFriendCourse(@Query("idhost") String idHost, @Query("select") String select);
//    @FormUrlEncoded
//    @POST("inviteFriend.php")
//    Call<Void> inviteFriend(@Field("array[]") Object[] mList, @Field("idHost") String idHost, @Field("namecourse") String nameCourse, @Field("topic") String topic);
//
//    @Multipart
//    @POST("createNewCourse.php")
//    Observable<String> createNewCourse(@Part List<MultipartBody.Part> file, @Part("word[]") ArrayList<RequestBody> wordList,
//                                       @Part("meaning[]") ArrayList<RequestBody> meaningList,
//                                       @Part("fullpath[]") ArrayList<RequestBody> fullPathList,
//                                       @Part("listfriend[]") ArrayList<RequestBody> listFriendInvited,
//                                       @Part("nameCourse") RequestBody nameCourse,
//                                       @Part("topic") RequestBody topic,
//                                       @Part("idHost") RequestBody idHost);
//    @GET("getHintTopic.php")
//    Observable<List<String>>getHintTopic();
//
//    @FormUrlEncoded
//    @POST("getInvitationCourse.php")
//    Observable<List<Course>>getInvitationCourse(@Field("idhost") String idHost);
//
//    @FormUrlEncoded
//    @POST("acceptOrCancelInvite.php")
//    Observable<String>acceptOrCancelInvitation(@Field("idHost") String idHost, @Field("idcourse") String idCourse, @Field("select") String select);
//
//    @FormUrlEncoded
//    @POST("getListVocab.php")
//    Observable<List<Vocabulary>>getListVocabulary(@Field("idcourse") String idCourse);
    @GET("login.php")
    Observable<User>loginAccount(@Query("username")String userName, @Query("password")String password);

    @GET("register.php")
    Observable<User> registerAccount(@Query("username") String username, @Query("password") String password, @Query("displayname") String displayName);


//    @Multipart
//    @Headers({
//            "Content-Type: application/json",
//            "x-access-token: eyJhbGciOiJIU"
//    })
//    @POST("createTest.php")
//    Observable<String> createTest(@Part List<MultipartBody.Part> file, @Part("test[]") ArrayList<RequestBody> test,
//                                       @Part("nameCourse") RequestBody nameCourse,
//                                       @Part("topic")RequestBody topic,
//                                       @Part("idHost")RequestBody idHost,
//                                       @Part("description")RequestBody description);

    @Multipart
    @POST("createTest.php")
//    @Headers({
//            "Content-Type: application/json",
//            "x-access-token: eyJhbGciOiJIU"
//    })
    Observable<String>createTest(@Part("json") RequestBody json,@Part List<MultipartBody.Part>file
                                ,@Part("topic")RequestBody topic
                                ,@Part("name")RequestBody nameTest
                                ,@Part("description")RequestBody description,
                                 @Part("idHost")RequestBody idHost,
                                 @Part("timer")RequestBody timer);
    @FormUrlEncoded
    @POST("getNewestTest.php")
    Observable<List<Test>> getNewestTest(@Field("idHost")String idHost,@Field("select")String select,@Field("idTest") String idTest);

    @FormUrlEncoded
    @POST("insertOrDelete.php")
    Observable<String> insertOrDelete(@Field("idHost")String idHost,@Field("select")String sel,@Field("idtest")String idTest);

    @FormUrlEncoded
    @POST("getListAnswer.php")
    Observable<List<MultipleChoice>>getListAnswer(@Field("idTest")String idTest);

    @FormUrlEncoded
    @POST("getListQuestion.php")
    Observable<List<QuestionAndAnswer>>getListQuestion(@Field("idTest")String idTest);

    @FormUrlEncoded
    @POST("getDataTest.php")
    Observable<Test>getDataTest(@Field("idTest")String idTest);

    @FormUrlEncoded
    @POST("insertOrUpdateTester.php")
    Observable<String>insertOrUpdateTester(@Field("idUser")String idUser,
                                           @Field("idTest")String idtest,
                                           @Field("timeDone") int timeDone,
                                           @Field("percentDone") String percentDone,
                                           @Field("numWrong")int numWrong,
                                           @Field("numTrue")int numTrue);
}
