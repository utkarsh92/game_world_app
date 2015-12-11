package com.example.prakhargupta.myapplication5.adapter;

/**
 * Created by Pewds on 20-Nov-15.
 */

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.prakhargupta.myapplication5.Expand_Post;
import com.example.prakhargupta.myapplication5.FeedImageView;
import com.example.prakhargupta.myapplication5.R;
import com.example.prakhargupta.myapplication5.app.AppController;
import com.example.prakhargupta.myapplication5.data.FeedItem;

import java.util.List;

public class FeedListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    String likess;
    ImageButton ib1;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return feedItems.get(position).getType();
    }

    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        FeedItem item = feedItems.get(position);

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        if (type == 0)
        {
            convertView = inflater.inflate(R.layout.feed_item, null);

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView title = (TextView) convertView.findViewById(R.id.txtTitle);
            TextView content = (TextView) convertView.findViewById(R.id.txtContent);
            TextView likes = (TextView) convertView.findViewById(R.id.likes);
            TextView comments = (TextView) convertView.findViewById(R.id.comments);
            NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);

            name.setText(item.getName());
            date.setText(item.getDate());

            // Chcek for empty title
            if (!TextUtils.isEmpty(item.getTitle())) {
                title.setText(item.getTitle());
                title.setVisibility(View.VISIBLE);
            } else {
                // tile is empty, remove from view
                title.setVisibility(View.GONE);
            }

            // Chcek for empty content
            if (!TextUtils.isEmpty(item.getContent())) {
                content.setText(item.getContent());
                content.setVisibility(View.VISIBLE);
            } else {
                // content is empty, remove from view
                content.setVisibility(View.GONE);
            }



            String likessss = item.getLikes();
            TextView likeDisplay = (TextView) convertView.findViewById(R.id.likeDisplay);
            if (likessss.equals("-99")) {
                likes.setVisibility(View.GONE);
                likeDisplay.setVisibility(View.GONE);
            }else {
                likes.setText(item.getLikes());
            }

            String commentssss = item.getComments();
            TextView commentDisplay = (TextView) convertView.findViewById(R.id.commentDisplay);
            if (commentssss.equals("-99")) {
                comments.setVisibility(View.GONE);
                commentDisplay.setVisibility(View.GONE);
            }else {
                comments.setText(item.getComments());
            }


            profilePic.setImageUrl(item.getProfilePic(), imageLoader);

//        // Checking for null feed url
//        if (item.getUrl() != null) {
//            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//                    + item.getUrl() + "</a> "));
//
//            // Making url clickable
//            url.setMovementMethod(LinkMovementMethod.getInstance());
//            url.setVisibility(View.VISIBLE);
//        } else {
//            // url is null, remove from the view
//            url.setVisibility(View.GONE);
//        }

        } else if (type == 1)
        {

            convertView = inflater.inflate(R.layout.like_button, null);

            TextView tv1 = (TextView) convertView.findViewById(R.id.no_of_likes);
            tv1.setText(item.getLikes() + " people like this.");
//            ib1 = (ImageButton) convertView.findViewById(R.id.imageButton);
//
//            likess = item.getLikedornot();
////            Toast.makeText(convertView.getContext(),"likedornot: " + likess,Toast.LENGTH_SHORT).show();
//            if (likess.equals("1"))
//            {
//                ib1.setImageResource(R.drawable.star_on);
//            }
//
//            ib1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    System.out.println("button clicked");
//                    ib1.setImageResource(R.drawable.star_on);
//
////                    listAdapter = new FeedListAdapter(Expand_Post.class, feedItems);
////                    listView.setAdapter(listAdapter);
//
////                    if (likess.equals("1"))
////                    {
////                        ib1.setImageResource(R.drawable.star_off);
////                        likess = "0";
////                    }else
////                    {
////                        ib1.setImageResource(R.drawable.star_on);
////                        likess = "1";
////
////                    }
//                }
//            });

        } else if (type == 2)
        {

            convertView = inflater.inflate(R.layout.answers, null);

            TextView cname = (TextView) convertView.findViewById(R.id.cname);
            TextView cdate = (TextView) convertView.findViewById(R.id.cdate);
            TextView ccontent = (TextView) convertView.findViewById(R.id.ctxtContent);
//            EditText temp = (EditText) convertView.findViewById(R.id.editText9);
//            cname.setText("dlsvlsvls");


//            item = new FeedItem();
            cname.setText(item.getName());
            cdate.setText(item.getDate());
            ccontent.setText(item.getContent());


        } else if (type == 3)
        {
            convertView = inflater.inflate(R.layout.single_item, null);

            TextView item1 = (TextView) convertView.findViewById(R.id.item1);

            item1.setText(item.getName());
        }


        return convertView;
    }
}
