package com.mahesh.demofirebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Feed> stories;
    private OnCardListener onCardListener;

    public ImageAdapter(Context mContext, List<Feed> stories,OnCardListener onCardListener){
        this.context=mContext;
        this.stories=stories;
        this.onCardListener = onCardListener;
    }


    public class ImageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle,tvDescription;
        public ImageView imageView;
        OnCardListener onCardListener;

        public ImageViewHolder(@NonNull View itemView,OnCardListener onCardListener) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_textView);
            tvDescription = itemView.findViewById(R.id.description_textView);
            imageView = itemView.findViewById(R.id.feed_imageView);
            this.onCardListener = onCardListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                onCardListener.onCardClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(view,onCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
            Feed story = stories.get(position);
            holder.tvTitle.setText(story.getTitle());
            holder.tvDescription.setText(story.getDescription());
            Picasso.with(context).load(story.getImageLink()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public interface OnCardListener {
        void onCardClick(int position);
    }

}
