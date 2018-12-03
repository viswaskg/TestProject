package com.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.myapplication.R;
import com.myapplication.callbacks.MediaSelectedCallBack;
import com.myapplication.database.models.MediaList;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MediaAdapterInner extends RecyclerView.Adapter<MediaAdapterInner.MyViewHolder> {

    private List<MediaList> mediaLists;
    private MediaSelectedCallBack callBack;

    public MediaAdapterInner(List<MediaList> mediaLists, MediaSelectedCallBack callBack) {
        this.mediaLists = mediaLists;
        this.callBack = callBack;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media_listing_inner, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MediaList listItem = mediaLists.get(position);
        Picasso.get()
                .load(listItem.getThumb())
                .placeholder(R.mipmap.ic_placeholder)
                .error(R.mipmap.ic_placeholder)
                .into(holder.placeholder);
        holder.title.setText(listItem.getTitle());
        holder.description.setText(listItem.getDescription());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onMediaSelected(listItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mediaLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView placeholder;
        public TextView title, description;

        public MyViewHolder(View view) {
            super(view);
            placeholder = (ImageView) view.findViewById(R.id.iv_placeholder);
            title = (TextView) view.findViewById(R.id.tv_title);
            description = (TextView) view.findViewById(R.id.tv_desc);
        }
    }
}