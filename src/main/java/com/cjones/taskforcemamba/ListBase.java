package com.cjones.taskforcemamba;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cjones.taskforcemamba.helper.ReverseOrder;
import com.cjones.taskforcemamba.helper.RssFeedStructure;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.cjones.taskforcemamba.adapter.RssReaderListAdapter;
import com.cjones.taskforcemamba.helper.SortingOrder;
import com.cjones.taskforcemamba.helper.XmlHandler;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;


public class ListBase extends Activity {
    ListView _rssFeedListView;
    List<JSONObject> jobs;
    List<RssFeedStructure> rssStr;
    private RssReaderListAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_fragment_list_base);
        initCards();
    }

    private void initCards() {

        ArrayList<Card> cards = new ArrayList<Card>();
        for (int i = 0; i < 200; i++) {
            CardExample card = new CardExample(getApplication(), "My title " + i, "Inner text " + i);
            cards.add(card);
        }

        CardArrayAdapter mCardCursorAdapter = new CardArrayAdapter(getApplication(), cards);

        CardListView listView = (CardListView) findViewById(R.id.carddemo_list_base1);
        if (listView != null) {
            listView.setAdapter(mCardCursorAdapter);
        }
    }

    public class CardExample extends Card {

        protected String mTitleHeader;
        protected String mTitleMain;

        public CardExample(Context context, String titleHeader, String titleMain) {
            super(context, R.layout.carddemo_example_inner_content);
            this.mTitleHeader = titleHeader;
            this.mTitleMain = titleMain;
            init();
        }

        private void init() {

            //Create a CardHeader
            CardHeader header = new CardHeader(getApplication());

            //Set the header title
            header.setTitle(mTitleHeader);

            addCardHeader(header);

            //Add ClickListener
            setOnClickListener(new OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    Toast.makeText(getContext(), "Click Listener card=" + mTitleHeader, Toast.LENGTH_SHORT).show();
                }
            });

            //Set the card inner text
            setTitle(mTitleMain);
        }


    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.list_base, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            return super.onOptionsItemSelected(item);
        }
    }


