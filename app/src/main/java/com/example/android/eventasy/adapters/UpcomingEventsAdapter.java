package com.example.android.eventasy.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventasy.R;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventList;
import com.example.android.eventasy.ItemClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by kakalra on 12/19/2016.
 */

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingEventsAdapter.EventViewHolder> {

    private Context context;
    EventList upcomingEventList;
    private ItemClickListener clickListener;

    public UpcomingEventsAdapter(Context context, EventList upcomingEventList){
        this.context=context;
        this.upcomingEventList=upcomingEventList;
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener=itemClickListener;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        ImageView eventImageView;
        TextView eventNameView;
        ProgressBar spinner;

        EventViewHolder(View itemView){
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.upcoming_events_card_view);
            eventImageView=(ImageView)itemView.findViewById(R.id.upcoming_events_image_view);
            eventNameView=(TextView)itemView.findViewById(R.id.upcoming_events_name_view);
            spinner=(ProgressBar)itemView.findViewById(R.id.upcoming_event_progress_bar);
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
        return upcomingEventList == null? 0:upcomingEventList.getEventList().size();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_event_list_item,parent,false);
        EventViewHolder eventViewHolder=new EventViewHolder(v);
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int position) {
        Event event=upcomingEventList.getEventList().get(position);
        if(event!=null){
            holder.spinner.setVisibility(View.VISIBLE);

            if(event.getEventTitle()!=null)
                holder.eventNameView.setText(event.getEventTitle());
            if(event.getEventImage()!=null && event.getEventImage().getBlock200Image()!=null){
                Picasso.with(context)
                        .load(event.getEventImage().getBlock200Image().getImageUrl())
                        .error(R.drawable.error_image)
                        .into(holder.eventImageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.spinner!=null)
                                    holder.spinner.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                if (holder.spinner!=null)
                                    holder.spinner.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }
}
