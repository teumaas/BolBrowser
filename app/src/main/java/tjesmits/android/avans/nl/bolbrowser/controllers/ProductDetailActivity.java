package tjesmits.android.avans.nl.bolbrowser.controllers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import me.zhanghai.android.materialratingbar.MaterialRatingDrawable;
import tjesmits.android.avans.nl.bolbrowser.R;
import tjesmits.android.avans.nl.bolbrowser.domain.Product;
import tjesmits.android.avans.nl.bolbrowser.utilities.Tags;
import us.feras.mdv.MarkdownView;

/**
 * Created by Tom Smits on 9-3-2018.
 */

public class ProductDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setupActionBar();

        Bundle extras = getIntent().getExtras();

        Product mProduct = (Product) extras.getSerializable(Tags.PRODUCT_TITLE);

        ImageView mProductImage = (ImageView) findViewById(R.id.ProductImage);

        TextView textViewProductTitle = findViewById(R.id.textViewProductName);
        TextView textViewProductSummary = findViewById(R.id.textViewProductSummary);
        MaterialRatingBar materialRatingBarProduct = findViewById(R.id.ProductRating);
        TextView textViewPrice = findViewById(R.id.ProductPrice);
        MarkdownView textViewDescription = findViewById(R.id.ProductDescription);

        // ImageView uit scherm koppelen

        this.setTitle(mProduct.title);
        textViewProductTitle.setText(mProduct.title);
        textViewProductSummary.setText(mProduct.summary);
        materialRatingBarProduct.setProgress(mProduct.rating / 5);
        textViewPrice.setText(mProduct.price);
        textViewDescription.setBackgroundColor(000000);
        textViewDescription.loadMarkdown("<b>Productbeschrijving:</b> <br/><br/>" + mProduct.longDescription);
        Picasso.with(this)
                .load(mProduct.imageURL)
                .into( mProductImage );
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
