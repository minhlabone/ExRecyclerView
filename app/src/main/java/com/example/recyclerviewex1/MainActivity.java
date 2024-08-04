package com.example.recyclerviewex1;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User> userList = new ArrayList<>();
    RecyclerView recyclerView;
    LetterAdapter letterAdapter;
    EditText edtSearch;
    AppCompatButton btndelete,btnSelectAll;
    boolean isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       initView();
        userList.add(new User("Diep"));
        userList.add(new User("Dang"));
        userList.add(new User("Anh"));
        userList.add(new User("Hao"));
        userList.add(new User("Hang"));
        userList.add(new User("An"));
        userList.add(new User("De"));
        userList.add(new User("Lan"));
        userList.add(new User("Tuan"));
        userList.add(new User("Huynh"));
        userList.add(new User("Mai"));
        userList.add(new User("Khang"));
        userList.add(new User("Bo"));
        userList.add(new User("An"));
        userList.add(new User("De"));
        userList.add(new User("Lan"));
//        List<User> sortedList = new ArrayList<>(userList);
//        Collections.sort(sortedList, new Comparator<User>() {
//            @Override
//            public int compare(User o1, User o2) {
//                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
//            }
//        });
//        for (User u: sortedList)
//        {Log.d("Sortedlist",u.getName());
//        }
//
//        for (User user: sortedList) {
//            long count = sortedList.stream()
//                    .filter(l -> l.getName().charAt(0) == user.getName().charAt(0))
//                    .count();
//            Log.d("coount", String.valueOf(count));
//        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        letterAdapter = new LetterAdapter(userList, new LetterAdapter.ItemClickListener() {
            @Override
            public void onCheck(int pos) {
                if(userList.get(pos).isIsselected() == false){
                    userList.get(pos).setIsselected(true);
                     letterAdapter.notifyDataSetChanged();
                }else {
                    userList.get(pos).setIsselected(false);
                    letterAdapter.notifyDataSetChanged();
                }
            }
        });
        letterAdapter.setData();
        letterAdapter.handlerList();
        letterAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(letterAdapter);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("searchText","OnBefore");
                letterAdapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("searchText","OnText");
                letterAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("searchText","OnAfter");

                letterAdapter.getFilter().filter(s);
            }
        });
        //Delete item is checked
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letterAdapter.deleteItemIsChecked();
            }
        });
        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = !isSelected;
                if(isSelected == true) {
                    btnSelectAll.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.baseline_check_circle_24,0);
                    letterAdapter.selectAll();
                }else {
                    btnSelectAll.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.empty,0);
                    letterAdapter.unSelectAll();
                }
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        edtSearch = findViewById(R.id.edt_search);
        btndelete = findViewById(R.id.btn_deleteItem);
        btnSelectAll = findViewById(R.id.btn_select_all_item);
    }
}