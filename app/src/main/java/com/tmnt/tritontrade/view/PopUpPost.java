package com.tmnt.tritontrade.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tmnt.tritontrade.R;
import com.tmnt.tritontrade.controller.CurrentState;
import com.tmnt.tritontrade.controller.DownloadPhotosAsyncTask;
import com.tmnt.tritontrade.controller.Post;
import com.tmnt.tritontrade.controller.Server;
import com.tmnt.tritontrade.controller.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import static de.hdodenhof.circleimageview.R.styleable.CircleImageView;


/**
 * Created by omarkhawaja on 2/19/17.
 */

public class PopUpPost extends AppCompatActivity {
    private boolean cart_status = false;
    Toast t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = new Toast(getApplicationContext());
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_post);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .8));

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /* ArrayList<String> dummyPhotos = new ArrayList<>();
        dummyPhotos.add("http://xiostorage.com/wp-content/uploads/2015/10/test.png");
        dummyPhotos.add("http://barkpost-assets.s3.amazonaws.com/wp-content/uploads/2013/11/dogelog.jpg");
        dummyPhotos.add("https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg");
        dummyPhotos.add("http://barkpost-assets.s3.amazonaws.com/wp-content/uploads/2013/11/dogelog.jpg");
        dummyPhotos.add("https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg"); */

        //Post dummy = new Post("Doge", dummyPhotos, "Good Doge 4 u.", 12.42f, null, 0, 0, false, false, null, null, false);

        final Post p = getIntent().getParcelableExtra("category");
        loadPost(p);

        final User current_user = CurrentState.getInstance().getCurrentUser();
        if (current_user.getCartIDs().contains(p.getPostID())) {
            cart_status = true;
            ((Button) findViewById(R.id.cart)).setText("REMOVE FROM CART");
        }

        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                Toast t = new Toast(getApplicationContext());
                if (cart_status) {
                    current_user.removeFromCart(p.getPostID());
                }
                else {
                    current_user.addToCart(p.getPostID());
                }
                new ModifyUserCart().execute(current_user);
            }
        });
    }

    public void loadPost(Post p) {
        ArrayList<ImageView> photos = loadPhotos(p.getPhotos());
        LinearLayout photoContainer = (LinearLayout) findViewById(R.id.gallery);
        photoContainer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams photo_params =
                new LinearLayout.LayoutParams(175, 175);
        photo_params.gravity = Gravity.CENTER;
        for (int i = 0; i < photos.size(); i++) {
            final ImageView photoView = photos.get(i);

            //photoView.setImageDrawable(photos.get(i).getDrawable());
            //photoView.setAdjustViewBounds(true);
            //photoView.setCropToPadding(true);
            photoView.setLayoutParams(photo_params);
            photoView.setPadding(0,0,15,0);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView current_photo = (ImageView) findViewById(R.id.currentphoto);
                    current_photo.setImageDrawable(photoView.getDrawable());
                    Log.d("DEBUG", "clicked");
                }
            });
            photoContainer.addView(photos.get(i), i);
            //if (i == 0) photoView.performClick();

        }
        ImageView current_photo;
        current_photo = (ImageView) findViewById(R.id.currentphoto);
        //current_photo.setImageResource(R.drawable.blurred_geisel);
        //current_photo.postInvalidate();

        Bitmap bitmap = ((BitmapDrawable)current_photo.getDrawable()).getBitmap();
        current_photo.setImageBitmap(bitmap);


        TextView name = (TextView) findViewById(R.id.name);
        name.setText(p.getProductName());

        TextView price = (TextView) findViewById(R.id.price);
        price.setText("$" + String.valueOf(p.getPrice()));

        TextView desc = (TextView) findViewById(R.id.description);
        desc.setText(p.getDescription());
    }


    public ArrayList<ImageView> loadPhotos(ArrayList<String> photos) {

        ArrayList<ImageView> iv_array = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            ImageView d = new de.hdodenhof.circleimageview.CircleImageView(getApplicationContext());
            new DownloadPhotosAsyncTask(d)
                    .execute(photos.get(i));
            iv_array.add(d);
        }
        return iv_array;

    }

    public class ModifyUserCart extends AsyncTask<User, Void, Boolean> {
        @Override
        protected Boolean doInBackground(User... params) {
            try {
                Server.modifyExistingUser(params[0]);
                return true;
            }
            catch (IOException e) {
                Log.d("DEBUG", e.toString());
                return false;
            }
            catch (IllegalArgumentException e2) {
                Log.d("DEBUG", e2.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            t.cancel();
            if (bool) {
                if(cart_status) {
                    t.makeText(PopUpPost.this, "Item removed from cart", Toast.LENGTH_SHORT).show();
                    ((Button) findViewById(R.id.cart)).setText("ADD TO CART");
                }
                else {
                    t.cancel();
                    t.makeText(PopUpPost.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                    ((Button) findViewById(R.id.cart)).setText("REMOVE FROM CART");
                }
                cart_status = !cart_status;
            }
            else {
                t.makeText(PopUpPost.this, "Bad connection to server or bad post ID", Toast.LENGTH_SHORT);
            }
        }
    }




}
