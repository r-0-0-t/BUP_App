package com.sust.bup_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Seeds extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;

    private RecyclerView mBlogList;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeds);

        getSupportActionBar().setTitle("উন্নত বীজ এবং চারা");


        drawer = (DrawerLayout) findViewById(R.id.drawerid);
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer_id);
        navigationView.setItemIconTintList(null);
        drawerToggle = new ActionBarDrawerToggle(this,drawer,R.string.nav_open,R.string.nav_close);

        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        Spinner spinner = findViewById(R.id.idCropSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.crops_list,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Seeds");

        mDatabaseReference.keepSynced(true);

        mBlogList = (RecyclerView) findViewById(R.id.seeds_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerid);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();
        Intent intent = null;

        switch (id){

            case R.id.home:
                intent = new Intent(this,MainActivity.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.community:
                intent = new Intent(this,Community.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.solution:
                intent = new Intent(this,Solution.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.cultivation:
                intent = new Intent(this,Cultivation.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.seeds:
                intent = new Intent(this,Seeds.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.fertilizer:
                intent = new Intent(this,Fertilizer.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.equipment:
                intent = new Intent(this,Equipment.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_help:
                intent = new Intent(this,Helpline.class);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_feedback:
                intent = new Intent(this,Feedback.class);
                drawer.closeDrawer(GravityCompat.START);
                break;
        }
        startActivity(intent);


        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SeedsList,Seeds.BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SeedsList, Seeds.BlogViewHolder>(
                SeedsList.class,
                R.layout.seeds_view,
                Seeds.BlogViewHolder.class,
                mDatabaseReference
        ) {
            @Override
            protected void populateViewHolder(Seeds.BlogViewHolder viewHolder, SeedsList model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Seeds.this,SolutionPage.class);
                        intent.putExtra("blog_id",post_key);
                        startActivity(intent);
                    }
                });

            }

        };
        mBlogList.setAdapter(firebaseRecyclerAdapter);


    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Button mBuyNow;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mBuyNow = itemView.findViewById(R.id.mBuyBtn);
        }

        public void setTitle(String title) {
            TextView post_tile = mView.findViewById(R.id.mPostTitle);
            post_tile.setText(title);
        }

        public void setDesc(String desc) {
            TextView post_desc = mView.findViewById(R.id.mPostDesc);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = mView.findViewById(R.id.mSelectImage);
            Picasso.get().load(image).into(post_image);
        }

        public void setPrice(String price) {
            TextView post_price = mView.findViewById(R.id.mPostPrice);
            post_price.setText(price);
        }
    }

}
