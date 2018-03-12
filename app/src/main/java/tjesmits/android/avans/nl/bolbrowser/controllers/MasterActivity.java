package tjesmits.android.avans.nl.bolbrowser.controllers;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import tjesmits.android.avans.nl.bolbrowser.R;
import tjesmits.android.avans.nl.bolbrowser.api.ProductTask;
import tjesmits.android.avans.nl.bolbrowser.api.interfaces.OnProductAvailable;
import tjesmits.android.avans.nl.bolbrowser.domain.Product;
import tjesmits.android.avans.nl.bolbrowser.utilities.ProductAdapter;
import tjesmits.android.avans.nl.bolbrowser.utilities.Tags;

/**
 * Created by Tom Smits on 9-3-2018.
 */

public class MasterActivity extends AppCompatActivity
        implements OnProductAvailable {

    private static final String TAG = ProductTask.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> mProductList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        handleIntent(getIntent());

        this.setTitle("Producten");

        // Create a list of dummy mPersonsList to fill our recyclerview.
        // These will be fetched from a web service later.
        mProductList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        executeSearch("Telefoon");

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new ProductAdapter(this, mProductList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void OnProductAvailable(Product product) {
        mProductList.add(product);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchMenu).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            executeSearch(query);
        }
    }

    private void executeSearch(String productName) {
        String[] params = { "https://api.bol.com/catalog/v4/search/?q=" + productName + "&offset=0&limit=50&dataoutput=products,categories&apikey=4C580B27B903413AB2AF698EF96E6A40&format=json" };
        ProductTask productTask = new ProductTask(this);
        productTask.execute(params);
    }

}
