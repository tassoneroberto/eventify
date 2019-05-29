package com.company.eventify.utilities;

import android.content.Intent;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.company.eventify.R;
import com.company.eventify.organizer.OrganizerActivity;
import com.company.eventify.user.UserActivity;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OrganizerActivity organizer;
    private UserActivity user;
    private String contextCase = "";
    private String imageUrl = "http://eventifyserver.altervista.org/thumbnails/";

    public EventAdapter(List<Event> eventList, OrganizerActivity organizer) {

        this.eventList = eventList;
        this.organizer = organizer;
        contextCase = "organizer";
    }

    public EventAdapter(List<Event> eventList, UserActivity user, String contextCase) {
        this.eventList = eventList;
        this.user = user;
        this.contextCase = contextCase;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        Event e = eventList.get(i);
        eventViewHolder.setEvent(e);
        eventViewHolder.organizer_name.setText(e.getOrganizer());
        eventViewHolder.title.setText(e.getTitle());
        eventViewHolder.place.setText(e.getLocation());
        eventViewHolder.phone.setText(e.getPhone());
        eventViewHolder.data.setText(e.getOpening().substring(0, e.getOpening().length() - 3) + "\t\t"
                + e.getEnding().substring(0, e.getEnding().length() - 3));
        eventViewHolder.description.setText(e.getDescription());
        if (e.getCategory().equals("Cinema")) {
            eventViewHolder.image_card.setImageResource(R.drawable.cinema);
            eventViewHolder.card.setBackgroundResource(R.color.cinema);
        } else if (e.getCategory().equals("Disco")) {
            eventViewHolder.image_card.setImageResource(R.drawable.disco);
            eventViewHolder.card.setBackgroundResource(R.color.disco);
        } else if (e.getCategory().equals("Theatre")) {
            eventViewHolder.image_card.setImageResource(R.drawable.theatre);
            eventViewHolder.card.setBackgroundResource(R.color.theatre);
        } else if (e.getCategory().equals("Festival")) {
            eventViewHolder.image_card.setImageResource(R.drawable.festival);
            eventViewHolder.card.setBackgroundResource(R.color.festival);
        } else if (e.getCategory().equals("Food & Drink")) {
            eventViewHolder.image_card.setImageResource(R.drawable.food_drink);
            eventViewHolder.card.setBackgroundResource(R.color.food_drink);
        } else if (e.getCategory().equals("Live Music")) {
            eventViewHolder.image_card.setImageResource(R.drawable.livemusic);
            eventViewHolder.card.setBackgroundResource(R.color.music);
        } else if (e.getCategory().equals("Museum")) {
            eventViewHolder.image_card.setImageResource(R.drawable.museum);
            eventViewHolder.card.setBackgroundResource(R.color.museum);
        } else if (e.getCategory().equals("Outdoor")) {
            eventViewHolder.image_card.setImageResource(R.drawable.outdoor);
            eventViewHolder.card.setBackgroundResource(R.color.outdoor);

        }
        if (contextCase.equals("organizer")) {
            eventViewHolder.btn_edit.setVisibility(View.VISIBLE);
            eventViewHolder.btn_delete.setVisibility(View.VISIBLE);
            eventViewHolder.organizer_name.setVisibility(View.GONE);
        } else {
            if (e.isAdded()) {
                eventViewHolder.btn_remove.setVisibility(View.VISIBLE);
            } else {
                eventViewHolder.btn_add.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout, viewGroup, false);

        return new EventViewHolder(itemView);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final int DURATION = 250;
        protected TextView title;
        protected TextView phone;
        protected TextView place;
        protected TextView data;
        protected TextView organizer_name;
        protected TextView description;
        protected LinearLayout card;
        Event me;
        private ImageButton share_btn;
        private ViewGroup linearLayoutDetails;
        private ImageView imageViewExpand;
        private ImageView image_card;
        private Button btn_remove;
        private Button btn_add;
        private Button btn_delete;
        private Button btn_edit;
        private ImageButton fb_btn;

        public EventViewHolder(View v) {
            super(v);
            share_btn = v.findViewById(R.id.share_btn);
            fb_btn = v.findViewById(R.id.fb_btn);

            phone = v.findViewById(R.id.phone);
            phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            organizer_name = v.findViewById(R.id.organizer_name);
            card = v.findViewById(R.id.card_layout);
            title = v.findViewById(R.id.title);
            place = v.findViewById(R.id.place);
            data = v.findViewById(R.id.time);
            image_card = v.findViewById(R.id.image_card);
            description = v.findViewById(R.id.description);
            linearLayoutDetails = v.findViewById(R.id.linearLayoutDetails);
            imageViewExpand = v.findViewById(R.id.imageViewExpand);
            imageViewExpand.setOnClickListener(this);
            btn_remove = v.findViewById(R.id.btn_remove);
            btn_add = v.findViewById(R.id.btn_add);
            btn_delete = v.findViewById(R.id.btn_delete);
            btn_edit = v.findViewById(R.id.btn_edit);
            phone.setOnClickListener(this);
            btn_remove.setOnClickListener(this);
            btn_add.setOnClickListener(this);
            btn_delete.setOnClickListener(this);
            btn_edit.setOnClickListener(this);
            image_card.setOnClickListener(this);
            place.setOnClickListener(this);
            data.setOnClickListener(this);
            share_btn.setOnClickListener(this);
            fb_btn.setOnClickListener(this);

        }

        public void setEvent(Event e) {
            me = e;
        }

        @Override
        public void onClick(View v) {
            if (!contextCase.equals("organizer"))
                user.setExit(false);
            else
                organizer.setExit(false);
            switch (v.getId()) {
            case R.id.phone:
                if (!contextCase.equals("organizer"))
                    UserInteraction.sendCall(user, phone.getText().toString());
                break;
            case R.id.place:
                if (!contextCase.equals("organizer"))
                    UserInteraction.openMap(user, me);
                break;
            case R.id.time:
                if (!contextCase.equals("organizer"))
                    UserInteraction.getNotificationCalendar(user, me);
                break;
            case R.id.image_card:
                toggleDetails(imageViewExpand);
                break;
            case R.id.imageViewExpand:
                toggleDetails(v);
                break;
            case R.id.btn_delete:
                organizer.deleteEvent(me.getId());
                int position = getAdapterPosition();
                eventList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, eventList.size());
                break;
            case R.id.btn_edit:

                organizer.editEvent(me);
                break;
            case R.id.btn_remove: {
                v.setVisibility(View.GONE);
                this.btn_add.setVisibility(View.VISIBLE);

                user.removeEvent(me.getId());
                if (contextCase.equals("calendar")) {
                    int position2 = getAdapterPosition();
                    eventList.remove(position2);
                    notifyItemRemoved(position2);
                    notifyItemRangeChanged(position2, eventList.size());
                }
                break;
            }
            case R.id.btn_add: {
                v.setVisibility(View.GONE);
                this.btn_remove.setVisibility(View.VISIBLE);
                user.addEvent(me.getId());
                break;
            }
            case R.id.share_btn:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBodyText = "";

                String event = me.getTitle() + "\n" + me.getLocation() + "\nOpening: " + me.getOpening() + "\nEnding: "
                        + me.getEnding() + "\n" + me.getDescription();
                shareBodyText += event + "\n";
                shareBodyText += "http://eventifyserver.altervista.org/index.html";
                shareBodyText += "\n";

                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject/Title");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                if (contextCase.equals("organizer")) {
                    organizer.startActivity(Intent.createChooser(intent, "Choose sharing method"));
                } else {
                    user.startActivity(Intent.createChooser(intent, "Choose sharing method"));
                }
                break;

            case R.id.fb_btn:
                if (!contextCase.equals("organizer")) {
                    imageUrl += getImage(me);
                    user.setEvent(me.getTitle(), me.getDescription(), imageUrl);
                    user.shareButton.performClick();
                } else {
                    imageUrl += getImage(me);
                    organizer.setEvent(me.getTitle(), me.getDescription(), imageUrl);
                    organizer.shareButton.performClick();
                }
                break;
            }

        }

        private void toggleDetails(View view) {
            if (linearLayoutDetails.getVisibility() == View.GONE) {
                ExpandAndCollapseViewUtil.expand(linearLayoutDetails, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_expand_more_white_24dp);
                rotate(-180.0f);
            } else {
                ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
                imageViewExpand.setImageResource(R.drawable.ic_expand_less_white_24dp);
                rotate(180.0f);
            }
        }

        private void rotate(float angle) {
            Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            imageViewExpand.startAnimation(animation);
        }

        private String getImage(Event e) {
            String img = "outdoor.png";
            if (e.getCategory().equals("Cinema")) {
                img = "cinema.png";
            } else if (e.getCategory().equals("Disco")) {
                img = "disco.png";
            } else if (e.getCategory().equals("Theatre")) {
                img = "theatre.png";
            } else if (e.getCategory().equals("Festival")) {
                img = "festival.png";
            } else if (e.getCategory().equals("Food & Drink")) {
                img = "food_drink.png";
            } else if (e.getCategory().equals("Live Music")) {
                img = "livemusic.png";
            } else if (e.getCategory().equals("Museum")) {
                img = "museum.png";
            } else if (e.getCategory().equals("Outdoor")) {
                img = "outdoor.png";
            }
            return img;
        }

    }
}
