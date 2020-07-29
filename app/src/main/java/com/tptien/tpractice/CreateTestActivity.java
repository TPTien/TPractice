package com.tptien.tpractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.tptien.tpractice.Adapter.QuestionAnswerAdapter;
import com.tptien.tpractice.Model.MultipleChoice;
import com.tptien.tpractice.Model.QuestionAndAnswer;
import com.tptien.tpractice.Service.APIService;
import com.tptien.tpractice.Service.DataService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CreateTestActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =101 ;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private QuestionAnswerAdapter mQuestionAnswerAdapter;
    private ArrayList<QuestionAndAnswer> listQuestionAnswers =new ArrayList<>();
    private static final int SELECT_IMAGE =101;
    private String pathImage= "";
    private Uri uriImage=null;
    private int imageSelectedPosition=0;
    private FloatingActionButton fab_addQuestion;
    private ArrayList<MultipleChoice>tempList= new ArrayList<>();
    private EditText edt_name,edt_topic,edt_description,edt_timer;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String dirPath="https://tstudy.000webhostapp.com/TStudy/image/";
    private String fullPath ="";
    private String idHost ="";
    private int totalScore=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);
        mToolbar=(Toolbar)findViewById(R.id.toolbar_createTest);
        edt_name=(EditText)findViewById(R.id.edt_name);
        edt_topic=(EditText)findViewById(R.id.edt_topic);
        edt_description=(EditText)findViewById(R.id.edt_description);
        edt_timer =(EditText)findViewById(R.id.edt_timer);
        setSupportActionBar(mToolbar);
        //
        mSharedPreferences =getSharedPreferences("loginAccount",MODE_PRIVATE);
        idHost=mSharedPreferences.getString("idUser",null);
        Log.d("idUser",idHost);
        //
        getSupportActionBar().setTitle("Tạo đề");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //tempList.add(new MultipleChoice("A."+" "));
        tempList.add(new MultipleChoice(""));
        fab_addQuestion =(FloatingActionButton)findViewById(R.id.fab_addQuestion);
        fab_addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MultipleChoice>temp =new ArrayList<>();
                //temp.add(new MultipleChoice("new answer"));
                temp.add(new MultipleChoice(""));
                listQuestionAnswers.add(new QuestionAndAnswer("","","",null,temp));
                mQuestionAnswerAdapter.notifyItemInserted(mQuestionAnswerAdapter.getItemCount()-1);
                //mQuestionAnswerAdapter.notifyDataSetChanged();
                //mRecyclerView.scrollToPosition(mQuestionAnswerAdapter.getItemCount() -1);
                mRecyclerView.smoothScrollToPosition(mQuestionAnswerAdapter.getItemCount()-1);
            }
        });
        listQuestionAnswers.add(new QuestionAndAnswer("","","",null,tempList));
        mRecyclerView =(RecyclerView)findViewById(R.id.recyclerView_createTest);
//        for(int i=0;i<5;i++){
//            ArrayList<MultipleChoice>listAnswer =new ArrayList<>();
//            listAnswer.add(new MultipleChoice("A"));
////            for (int j=0;j<4;j++){
////
////            }
//
//        }
        linearLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mQuestionAnswerAdapter=new QuestionAnswerAdapter(this,listQuestionAnswers);
        mQuestionAnswerAdapter.setOnClickListener(new QuestionAnswerAdapter.QuestionCardListener() {
            @Override
            public void onButtonDeleteClick(int position) {
                listQuestionAnswers.remove(position);
                mQuestionAnswerAdapter.notifyItemRemoved(position);
                mQuestionAnswerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSelectImageClick(int position) {
                imageSelectedPosition=position;
                Intent intent =new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),SELECT_IMAGE);
            }

            @Override
            public void onAddAnswerClick(int position) {
//                listQuestionAnswers.get(position).getListAnswer().add(new MultipleChoice("new answer"));
//                mQuestionAnswerAdapter.answerAdapter.notifyDataSetChanged();
//                //mQuestionAnswerAdapter.notifyItemInserted(listQuestionAnswers.get(position).getListAnswer().size() -1);
//                mQuestionAnswerAdapter.notifyDataSetChanged();
            }
        });
        mQuestionAnswerAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mQuestionAnswerAdapter);
        //


        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        //

        enableSwipeToDeleteAndUndo();

    }
    public void enableSwipeToDeleteAndUndo(){
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final QuestionAndAnswer item = mQuestionAnswerAdapter.getData().get(position);

                mQuestionAnswerAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.container_createTest), "Bạn vừa xóa thành công.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mQuestionAnswerAdapter.restoreItem(item, position);
                        //mRecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.setTextColor(getResources().getColor(R.color.white));
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(mRecyclerView);
    }
    private MultipartBody.Part prepareFilePart(String partName, String filePath){
        File file=new File(filePath);
        String file_path =file.getAbsolutePath();
        RequestBody requestBody= RequestBody.create(MediaType.parse("multipart/form-data"),file);
        return MultipartBody.Part.createFormData(partName,file_path,requestBody);
    }
    public static RequestBody createBodyRequestJson(String string) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), string);
        return requestBody;
    }
    public static RequestBody createBodyRequestString(String string) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), string);
        return requestBody;
    }
    private void uploadTest(String idHost){
        //
        Log.d("answerCorrect",listQuestionAnswers.get(0).getListAnswer().get(0).getAnswer());
        List<MultipartBody.Part>listPicture =new ArrayList<>();
        if(listQuestionAnswers.size()==0){
            Toast.makeText(this,"Vui lòng tạo bộ đề thi",Toast.LENGTH_SHORT).show();
        }else {
            for (int i= 0;i<listQuestionAnswers.size();i++){
                listQuestionAnswers.get(i).setBitmap(null);
                if(listQuestionAnswers.get(i).getScore().isEmpty()
                        ||((listQuestionAnswers.get(i).getQuestion().equals("")) && listQuestionAnswers.get(i).getImgUrlQuestion()==null)
                        ||listQuestionAnswers.get(i).getListAnswer().size()==0
                        ||listQuestionAnswers.get(i).getCorrectAnswer()==null){
                    Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin câu hỏi!",Toast.LENGTH_SHORT).show();
                    mRecyclerView.smoothScrollToPosition(i);
                    return;
                }else {
                    totalScore+=Integer.parseInt(listQuestionAnswers.get(i).getScore());
                    if(listQuestionAnswers.get(i).getImgUrlQuestion() !=null){
                        listPicture.add(prepareFilePart("upload[]",listQuestionAnswers.get(i).getRealPathImage()));
                    }

                }
            }
            Log.d("ToltalScore", String.valueOf(totalScore));
            Gson gson = new Gson();
//            JsonElement element = gson.toJsonTree(listQuestionAnswers, new TypeToken<List<QuestionAndAnswer>>() {}.getType());
//
//            if (! element.isJsonArray() ) {
//                // fail appropriatelythrow new SomeException();
//            }
//            JsonArray jsonArray = element.getAsJsonArray();
            String json =gson.toJson(listQuestionAnswers);
            Log.d("json",json.toString());
            //get input data
            String name =edt_name.getText().toString();
            String topic=edt_topic.getText().toString();
            String description =edt_description.getText().toString();
            String timer =edt_timer.getText().toString();
            if(name.isEmpty() ||topic.isEmpty()||timer.isEmpty()){
                Toast.makeText(this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(this,String.valueOf(listQuestionAnswers.size()),Toast.LENGTH_SHORT).show();
                DataService dataService = APIService.getService();
                Observable<String> observable =dataService.createTest(createBodyRequestJson(json.toString()),
                                                                        listPicture,
                                                                        createBodyRequestString("1"),
                                                                        createBodyRequestString(topic),
                                                                        createBodyRequestString(name),
                                                                        createBodyRequestString(description),
                                                                        createBodyRequestString(idHost),
                                                                        createBodyRequestString(timer),
                                                                        createBodyRequestString(String.valueOf(totalScore)));
                observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String s) {
                                Log.d("response",s);
                                Toast.makeText(CreateTestActivity.this,"Tạo thành công !",Toast.LENGTH_SHORT).show();
                                finish();
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

            //


        }

    }
    public String getRealPathFromURI (Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE && data!=null) {
            uriImage=data.getData();
            pathImage=getRealPathFromURI(uriImage);
            //get image'name in path file
            String imageName = pathImage.substring(pathImage.lastIndexOf("/")+1);
            //get full path
            fullPath=dirPath.concat(imageName);
            Log.d("pathImage",pathImage.toString());

            try {
                InputStream inputStream =getContentResolver().openInputStream(uriImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                listQuestionAnswers.get(imageSelectedPosition).setBitmap(bitmap);
                listQuestionAnswers.get(imageSelectedPosition).setRealPathImage(pathImage);
                listQuestionAnswers.get(imageSelectedPosition).setImgUrlQuestion(fullPath);
                Log.d("fullPath",listQuestionAnswers.get(imageSelectedPosition).getImgUrlQuestion());
                mQuestionAnswerAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_test,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_done :
                //Toast.makeText(this,listQuestionAnswers.get(0).getQuestion(),Toast.LENGTH_SHORT).show();

                uploadTest(idHost);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}