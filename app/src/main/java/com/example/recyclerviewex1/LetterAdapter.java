package com.example.recyclerviewex1;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.MyViewHolder> {
    List<User> userList;
    List<User> datalist = new ArrayList<>() ;
    ItemClickListener itemClickListener;
    public LetterAdapter(List<User> userList, ItemClickListener itemClickListener) {
        this.userList = userList;
        this.itemClickListener = itemClickListener;
    }
    public void setData(){
         datalist.clear();
         datalist.addAll(userList);
    }
    public interface ItemClickListener{
         void onCheck(int pos);
    }
    public void deleteItemIsChecked(){
        userList.removeIf(user -> user.isIsselected());
        handlerList();
    }
    public void selectAll(){
        userList.forEach(
                user -> user.isselected = true
        );
      notifyDataSetChanged();
    }
    public void unSelectAll(){
        userList.forEach(user -> user.isselected = false);
        notifyDataSetChanged();
    }

    public void handlerList() {
        // Step 1: sort list theo kí tự bảng chữ cái
        List<User> sortedList = new ArrayList<>(userList);
        Collections.sort(sortedList, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getName().toUpperCase().compareTo(o2.getName().toUpperCase());
            }
        });

        // Step 2:Đếm số lần xuất hiện của kí tự đầu với các kí tự đầu trong list
        for (User letter : sortedList) {
            long count = sortedList.stream()
                    .filter(l -> l.getName().charAt(0) == letter.getName().charAt(0))
                    .count();
         // sau mỗi lần lặp thì nó sẽ suất 1 count t count đó sẽ tìm kiếm vị trí , neeus count = 1 thì chỉ có ONLY, nếu count
            if (count == 1) {
                letter.type = TYPE.ONLY;
            } else {
                // tìm chữ xuất hiện đầu tiên
                User first = sortedList.stream()
                        .filter(l -> l.getName().charAt(0) == letter.getName().charAt(0))
                        .findFirst()
                        .orElse(null);
               // tìm chữ xuất hiện cuối cùng
                User last = sortedList.stream()
                        .filter(l -> l.getName().charAt(0) == letter.getName().charAt(0))
                        .reduce((firstL, secondL) -> secondL)
                        .orElse(null);
               // nếu letter đó nằm ở đầu thì set nó là FIRST còn năm ở cuối thì set LAST
                if (first == letter) {
                    letter.type = TYPE.FIRST;
                } else if (last == letter) {
                    letter.type = TYPE.LAST;
                } else {
                    // còn lại là MID
                    letter.type = TYPE.MID;
                }
            }
        }

        // Step 3: Clear the original list and add all elements from sorted list
        userList.clear();
        userList.addAll(sortedList);

        // Step 4: Log the result and notify data set changed
        Log.d("check", "handleData " + userList.toString());
        notifyDataSetChanged();
    }
    public Filter getFilter(){
        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //lọc kết quả
                String queryKey  = constraint.toString();
                if (queryKey.isEmpty()) {
                     userList = datalist;
//                    for (int i=0; i<8;i++){
//                       Log.d("userlist",userList.get(i).getName());
//                    }
//                    for (int i=0; i<8;i++){
//                        Log.d("datalist",datalist.get(i).getName());
//                    }
                } else {
                    List<User> list = new ArrayList<>();
                    for (User letter :datalist) {
                        if (letter.getName().toUpperCase().contains(queryKey.toUpperCase())) {
                            list.add(letter);
                        }
                    }
                   userList= list;
                    for (int i=0; i<userList.size();i++){
                     Log.d("datalist",userList.get(i).getName()+""+i);
                   }
                }
                FilterResults filterResult = new FilterResults();
                filterResult.values = userList;
                Log.d("check", "filterList: " + userList);
                return filterResult;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                         userList = (List<User>) results.values;
                         handlerList();
                         notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        int a = position;
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onCheck(a);
            }
        });
        holder.onBind(user);
    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        AppCompatTextView tv_name;
        View line;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_name = itemView.findViewById(R.id.tv_name);
            line = itemView.findViewById(R.id.view);
        }

        public void onBind(User letter) {
            tv_name.setText(letter.getName());
            tv_title.setText(String.valueOf(letter.getName().charAt(0)));

            switch (letter.getType()) {

                case FIRST:
                    line.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_name.setBackgroundResource(R.drawable.bg_first);
                    break;
                case MID:
                    line.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.GONE);
                    tv_name.setBackgroundResource(R.drawable.bg_mid);
                    break;
                case LAST:
                    line.setVisibility(View.VISIBLE);
                    tv_title.setVisibility(View.GONE);
                    tv_name.setBackgroundResource(R.drawable.bg_last);
                    break;
                case ONLY:
                    line.setVisibility(View.GONE);
                    tv_title.setVisibility(View.VISIBLE);
                    tv_name.setBackgroundResource(R.drawable.bg_only);
                    break;
            }
            if(letter.isIsselected() == true){
                tv_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_24,0,0,0);
            }else {
                tv_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.empty,0,0,0);
            }
        }
    }
}
