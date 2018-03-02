package com.example.user.moveappstage1.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.moveappstage1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 22/02/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.IamgeViewHolder> {
    List<String> Links;
    Context context;
    final private OnItemClickListner listner;

    public ImageAdapter(List<String> links, Context context, OnItemClickListner onItemClickListner) {
        this.listner=onItemClickListner;
        this.Links = links;
        this.context=context;
    }
public ImageAdapter(Context context,OnItemClickListner listner){
this.context=context;
    this.listner = listner;
}

    @Override
    public IamgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context =parent.getContext();
        int LayoutFormListItem = R.layout.list_item;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(LayoutFormListItem,parent,false);
        IamgeViewHolder holder=new IamgeViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(IamgeViewHolder holder, int position) {
        holder.putPic(context,Links.get(position));

    }

    @Override
    public int getItemCount() {
if (null==Links)
    return 0;
        return Links.size();
    }
    class IamgeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;


        public IamgeViewHolder(View itemView) {
            super(itemView);
       imageView= itemView.findViewById(R.id.tv_iamge_view);
       itemView.setOnClickListener(this);
        }
        void putPic(Context context,String Pic)
        {
            Picasso.with(context).load(Pic).into(imageView);
        }

        @Override
        public void onClick(View view) {
            int itemclicked=getAdapterPosition();
            listner.onItemClicked(itemclicked);
        }
    }
    public  interface OnItemClickListner{
        void onItemClicked(int i);
    }
    public void setLinks(List<String> links){
    this.Links=links;
    notifyDataSetChanged();
    }
}
