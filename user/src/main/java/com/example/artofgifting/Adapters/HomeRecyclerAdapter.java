package com.example.artofgifting.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.Myviewholder> {
    private Context context;
    //    private static OnItemClickListener mlistener;
    private ArrayList<CategoryModel> catnamearraylist;
    private HomeSubCategoryRecyclerAdapter subcategoryadapter;
    DatabaseReference myRef= FirebaseDatabase.getInstance().getReference();


    public HomeRecyclerAdapter(Context context, ArrayList<CategoryModel> catnamearraylist) {
        this.context = context;
        this.catnamearraylist= catnamearraylist;
    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_catname_rec, null, false);
        return new Myviewholder(myview/*listener*/);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myviewholder holder, final int position) {
        final int[] count = new int[1];
        final ArrayList<SubCategoryModel> subcategoryarraylist=new ArrayList<>();
        if(!catnamearraylist.isEmpty()){
            holder.catname.setText(catnamearraylist.get(position).getCatname());

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subcategoryarraylist.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.child("Categories").child(catnamearraylist.get(position).getCatkey()).getChildren()) {
                        if(dataSnapshot1.exists()){
                            if(dataSnapshot1.hasChildren()){
                                SubCategoryModel subCategoryModel=dataSnapshot1.getValue(SubCategoryModel.class);
                                subcategoryarraylist.add(subCategoryModel);
                            }
                        }
                    }
                    if(subcategoryarraylist.size()>6){
                        holder.viewall.setVisibility(View.VISIBLE);
                    }
                    if(subcategoryarraylist.size()>4){
                        holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                    }
                    else if(subcategoryarraylist.size()<4 && subcategoryarraylist.size()>1){
                        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,2));
                    }
                    else if(subcategoryarraylist.size()==1){
                        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,1));
                    }
                    if(subcategoryarraylist.size()>6){
                        count[0] =6;
                    }else
                    {
                        count[0] =subcategoryarraylist.size();
                    }
                    subcategoryadapter = new HomeSubCategoryRecyclerAdapter(context, subcategoryarraylist,null,count[0]);
                    holder.recyclerView.setAdapter(subcategoryadapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
       }


    }

    @Override
    public int getItemCount() {
        return catnamearraylist.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView catname;
        RecyclerView recyclerView;
        Button viewall;

        public Myviewholder(@NonNull View itemView/*final OnItemClickListener listener*/) {
            super(itemView);
            catname = itemView.findViewById(R.id.user_cat_name_raw_catname_rec);
            recyclerView = itemView.findViewById(R.id.user_subcat_recyclerview_raw_catname_rec);
            viewall=itemView.findViewById(R.id.user_btn_viewall_home);
        }
    }

}
