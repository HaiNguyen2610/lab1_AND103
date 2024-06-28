package fpoly.hainxph46327.demoapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import fpoly.hainxph46327.demoapi.databinding.ActivityMainBinding;
import todo.Todo;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        database=FirebaseFirestore.getInstance();
        setContentView(view);
        binding.btnAdd.setOnClickListener(v -> {
            insertFirebase(binding.result);
        });
        binding.btnUpdate.setOnClickListener(v -> {
            updateFirebase(binding.result);
        });
        binding.btnDelete.setOnClickListener(v -> {
            deleteFirebase(binding.result);
        });
        SelectDataFromFirebase(binding.result);
    }
    String id="";
    Todo todo=null;
    public  void insertFirebase(TextView tvResult){
        id= UUID.randomUUID().toString();
        todo= new Todo(id,"title 2", "content 2");
        HashMap<String, Object> mapTodo= todo.convertHashmap();

        database.collection("TODO").document(id).set(mapTodo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Lỗi: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void updateFirebase(TextView tvResult){
        id="726be8eb-9ca0-491f-b01a-2d73a917d7cc";
        todo=new Todo(id,"sua title 1","sua content 1");
        database.collection("TODO").document(todo.getId())
                .update(todo.convertHashmap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Sửa thất bại với lỗi: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void deleteFirebase(TextView tvResult){
        id="726be8eb-9ca0-491f-b01a-2d73a917d7cc";
        database.collection("TODO").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Xóa thất bại: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    String strResult="";
    public ArrayList<Todo> SelectDataFromFirebase(TextView tvResult){
        ArrayList<Todo> list=new ArrayList<>();
        database.collection("TODO")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){//sau khi lay du lieu thanh cong
                            strResult="";
                            //doc theo tung dong du lieu
                            for(QueryDocumentSnapshot document: task.getResult()){
                                //chuyen dong doc duoc sang doi tuong
                                Todo toDo1=document.toObject(Todo.class);
                                //chuyen doi tuong thanhchuoi
                                strResult +="\n Id: "+toDo1.getId();
                                list.add(toDo1);//them vao list
                            }
                            //hien thi ket qua
                            tvResult.setText(strResult);
                        }
                        else {
                            tvResult.setText("Doc du lieu that bai");
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        tvResult.setText(e.getMessage());
                    }
                });
        return list;
    }
}