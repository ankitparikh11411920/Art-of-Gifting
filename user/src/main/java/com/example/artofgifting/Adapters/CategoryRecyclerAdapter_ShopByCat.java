package com.example.artofgifting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artofgifting.Models.CategoryModel;
import com.example.artofgifting.Models.SubCategoryModel;
import com.example.artofgifting.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryRecyclerAdapter_ShopByCat extends RecyclerView.Adapter<CategoryRecyclerAdapter_ShopByCat.CategoryHolder> {

    Context context;
    ArrayList<CategoryModel> categorynamearraylist;
    SubCategoryRecyclerAdapter_ShopByCat subcategoryadapter;

    public CategoryRecyclerAdapter_ShopByCat(Context context, ArrayList<CategoryModel> categorynamearraylist) {
        this.context = context;
        this.categorynamearraylist = categorynamearraylist;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context)
                .inflate(R.layout.raw_cat_disp,parent,false);
        return new CategoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, final int position) {

        final int[] flag = {0};
        final DatabaseReference myRef;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        final ArrayList<String> subcategoryarraylist=new ArrayList<>();

        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        holder.textView.setText(categorynamearraylist.get(position).getCatname());
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(flag[0]==0){
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.button.setBackgroundResource(R.drawable.ic_expand_less);
                    flag[0] =1;
                    subcategoryarraylist.clear();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.child("Categories").child(categorynamearraylist.get(position).getCatkey()).getChildren()){
                                if(dataSnapshot1.hasChildren()){
                                    SubCategoryModel subCategoryModel=dataSnapshot1.getValue(SubCategoryModel.class);
                                    subcategoryarraylist.add(subCategoryModel.getSubcatname());
                                    subcategoryarraylist.remove("catname");
                                    subcategoryarraylist.remove("catimageurl");
                                    subcategoryadapter=new SubCategoryRecyclerAdapter_ShopByCat(context,subcategoryarraylist,categorynamearraylist);
                                    holder.recyclerView.setAdapter(subcategoryadapter);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    holder.button.setBackgroundResource(R.drawable.ic_expand_more);
                    holder.linearLayout.setVisibility(View.GONE);
                    flag[0]=0;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return categorynamearraylist.size();
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;
        RecyclerView recyclerView;
        LinearLayout linearLayout;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.user_cat_name_shopbycategory);
            linearLayout=itemView.findViewById(R.id.category_recyclerview_linearlayout);
            button=itemView.findViewById(R.id.user_cat_expand_shopbycategory);
            recyclerView=itemView.findViewById(R.id.user_subcat_recyclerview_shopbycategory);

        }
    }
}
