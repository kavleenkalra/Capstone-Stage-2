package com.example.android.eventasy.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.eventasy.R;
import com.example.android.eventasy.Utility;
import com.example.android.eventasy.retrofit.model.Event;
import com.example.android.eventasy.retrofit.model.EventResponse;
import com.example.android.eventasy.ItemClickListener;
import com.squareup.picasso.Picasso;

/**
 * Created by kakalra on 12/26/2016.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{

    private Context context;
    EventResponse eventResponse;
    private ItemClickListener clickListener;

    public EventListAdapter(Context context,EventResponse eventResponse){
        this.context=context;
        this.eventResponse=eventResponse;
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener=itemClickListener;
    }

    public class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        ImageView eventListImageView;
        TextView eventTitleTextView;
        TextView eventDateTimeVenueTextView;
        ProgressBar spinner;

        EventListViewHolder(View itemView){
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.event_list_card_view);
            eventListImageView=(ImageView)itemView.findViewById(R.id.event_list_image_view);
            eventTitleTextView=(TextView)itemView.findViewById(R.id.event_title_text_view);
            eventDateTimeVenueTextView=(TextView)itemView.findViewById(R.id.event_date_time_venue_view);
            spinner=(ProgressBar)itemView.findViewById(R.id.event_list_progress_bar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener!=null){
                clickListener.onClick(view,getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemCount() {
        return (eventResponse == null? 0:eventResponse.getAllEvents().getEventList().size());
    }

    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item,parent,false);
        EventListViewHolder eventListViewHolder=new EventListViewHolder(v);
        return eventListViewHolder;
    }

    @Override
    public void onBindViewHolder(final EventListAdapter.EventListViewHolder holder, int position) {
        Event event = eventResponse.getAllEvents().getEventList().get(position);
        if (event != null) {
            holder.spinner.setVisibility(View.VISIBLE);

            if (event.getEventImage() != null && event.getEventImage().getBlock200Image() != null)
                Picasso.with(context)
                        .load(event.getEventImage().getBlock200Image().getImageUrl())
                        .placeholder(R.drawable.grey)
                        .error(R.drawable.error_image)
                        .into(holder.eventListImageView, new com.squareup.picasso.Callback() {
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
            if (event.getEventTitle() != null)
                holder.eventTitleTextView.setText(eventResponse.getAllEvents().getEventList().get(position).getEventTitle());
            if (event.getEventStartTime() != null && event.getEventVenueName() != null)
                holder.eventDateTimeVenueTextView.setText(Utility.getFormattedDate(eventResponse.getAllEvents().getEventList().get(position).getEventStartTime()) + " at " + event.getEventVenueName());
        }
    }
}
