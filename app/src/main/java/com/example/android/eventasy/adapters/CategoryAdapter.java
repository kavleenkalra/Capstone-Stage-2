package com.example.android.eventasy.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.eventasy.R;
import com.example.android.eventasy.retrofit.model.CategoryResponse;
import com.example.android.eventasy.ItemClickListener;

/**
 * Created by kakalra on 12/21/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    CategoryResponse categoryResponse;
    private Context context;
    private ItemClickListener clickListener;

    public CategoryAdapter(Context context, CategoryResponse categoryResponse){
        this.context=context;
        this.categoryResponse=categoryResponse;
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener=itemClickListener;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        TextView categoryNameView;

        CategoryViewHolder(View itemView){
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.category_card_view);
            categoryNameView=(TextView)itemView.findViewById(R.id.category_name_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(clickListener!=null){
                clickListener.onClick(view,getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return categoryResponse == null? 0:categoryResponse.getCategoryList().size();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item,parent,false);
        CategoryViewHolder categoryViewHolder=new CategoryViewHolder(v);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.categoryNameView.setText(Html.fromHtml((String)categoryResponse.getCategoryList().get(position).getCategoryName()).toString());
    }
}
