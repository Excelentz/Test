package com.example.admin.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayNews extends AppCompatActivity {
    private static final String TITLE = "nickname";
    private static final String DESCRIPTION = "content";
    String title, link, comments;
    private ArrayList<HashMap<String, String>> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_news);
        Intent intent = getIntent();
        //int position = Integer.parseInt(intent.getStringExtra("position"));
        //String message = intent.getStringExtra("position");
        title = intent.getStringExtra("title");
        link = intent.getStringExtra("link");
        comments = intent.getStringExtra("comments");
        TextView itemTitle = (TextView) findViewById(R.id.textTitle);
        itemTitle.setText(title);
        DownloadInfo task1 = new DownloadInfo();
        task1.execute(link);
    }

    public void titleClick(View v) {

        Intent intent = new Intent(this.getBaseContext(), BrowserActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }

    class DownloadInfo extends AsyncTask<String, Void, Bitmap> {
        protected void onPostExecute(Bitmap bmp) {
            TextView itemTitle = (TextView) findViewById(R.id.textTitle);
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            ListView listView = (ListView) findViewById(R.id.listView2);
            ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar2);
            imageView.setImageBitmap(bmp);

            pb.setVisibility(View.GONE);
            itemTitle.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);

            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), commentList,
                    R.layout.list_item, new String[]{TITLE, DESCRIPTION},
                    new int[]{R.id.text1, R.id.text2});
            if (listView != null) {
                listView.setAdapter(adapter);
            } else {
                Log.v("?", "null listivew");
            }
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            String imgLink = null;
            try {
                Document doc = Jsoup.connect(link).get();
                Elements pngs = doc.select("img[title$=" + title);
                imgLink = pngs.attr("src");
                Elements commentsNickname = doc.select("a.nickname");
                int commentsNumber = commentsNickname.size();
                Elements commentsContent = doc.select("p.content");
                commentList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> hm;
                for (int i = 0; i < commentsNumber; i++) {
                    hm = new HashMap<>();
                    hm.put(TITLE, commentsNickname.get(i).text());
                    hm.put(DESCRIPTION, commentsContent.get(i).text());
                    commentList.add(hm);
                }
                URL imgURL = new URL(imgLink);
                HttpURLConnection connection = (HttpURLConnection) imgURL.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
