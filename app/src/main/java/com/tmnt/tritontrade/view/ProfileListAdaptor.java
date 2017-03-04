package com.tmnt.tritontrade.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmnt.tritontrade.R;
import com.tmnt.tritontrade.controller.CurrentState;
import com.tmnt.tritontrade.controller.DownloadPhotosAsyncTask;
import com.tmnt.tritontrade.controller.Post;
import com.tmnt.tritontrade.controller.Server;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nikolasn97 on 2/23/17.
 */

class ProfileListAdaptor extends BaseAdapter {

    private Context context;
    private CustomAdapter.CustomFilter filter;
    private ArrayList<Post> posts;
    private ArrayList<Post> filterList;
    private LayoutInflater inflater;
    private int count; //Current amount of items displayed
    private int stepNumber; //Amount of items loaded on next display
    private final int startCount=20; //Start amount of items being displayed

    /**
     * Constructor
     * @param context
     * @param posts
     */
    public ProfileListAdaptor(Context context, ArrayList<Post> posts) {
        this.posts=posts;
        this.filterList=posts;
        this.context = context;
        this.count = startCount;
        this.stepNumber=10;
    }

    /**
     * Setter for posts
     * @param posts
     */
    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    /**
     * Setter for count
     * @param count
     */
    public void setCount(int count){
        this.count=count;
    }

    /**
     * Gets the total amount of items in the list
     * @return
     */
    @Override
    public int getCount() {
        if (posts.size() < startCount){
            return posts.size(); //returns total of items in the list
        }
        else{
            return count;
        }
    }


    /**
     * Sets the ListView back to its initial count number
     */
    public void reset(){
        count = startCount;
        notifyDataSetChanged();
    }

    /**
     * Show more views, or the bottom
     * @return true if the entire data set is being displayed, false otherwise
     */
    public boolean showMore(){
        if(count == posts.size()) {
            return true;
        }else{
            count = Math.min(count + stepNumber, posts.size()); //don't go past the end
            notifyDataSetChanged(); //the count size has changed, so notify the super of the change
            return count == posts.size();
        }
    }

    /**
     * Returns item at given position in list
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position){
        return posts.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * Holder for post object
     */
    public class ViewHolder{
        TextView title;
        TextView description;
        //TextView userTag;
        //TextView price;
        //TextView category;
        //ImageView image;
    }

    /**
     * Sets items to
     * @param position position of current item
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent){
        View catView = convertView;

        //using adapter to display all categories
        if(convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            catView = inflater.inflate(R.layout.row, null);
        }
        ProfileListAdaptor.ViewHolder postHolder = new ProfileListAdaptor.ViewHolder();

        //Set everything in feed row
        postHolder.title = (TextView) catView.findViewById(R.id.title);
        postHolder.description = (TextView) catView.findViewById(R.id.description);
        //postHolder.userTag = (TextView) catView.findViewById(R.id.username_tag);
        //postHolder.category=(TextView) catView.findViewById(R.id.category_text);
        //postHolder.price = (TextView) catView.findViewById(R.id.price);
        //postHolder.image = (ImageView) catView.findViewById(R.id.row_pic);

        postHolder.title.setText(posts.get(position).getProductName());
        postHolder.description.setText(posts.get(position).getDescription());

        /*
        if(CurrentState.getInstance() != null) {
            postHolder.userTag.setText(CurrentState.getInstance().getCurrentUser().getName());
        }
        */

        //postHolder.category.setText(posts.get(position).getTags().get(1));
        //postHolder.price.setText("$"+String.valueOf(posts.get(position).getPrice()));
        /*
        new DownloadPhotosAsyncTask(postHolder.image)
                .execute(posts.get(position).getPhotos().get(0));
                */

        //Click listener for post
        catView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PopUpPost.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("category", posts.get(position));
                ((Activity)context).startActivityForResult(i, 1);

            }
        });
        return catView;
    }


}
