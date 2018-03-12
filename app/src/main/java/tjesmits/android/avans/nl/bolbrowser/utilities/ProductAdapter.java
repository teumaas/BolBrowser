package tjesmits.android.avans.nl.bolbrowser.utilities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import me.zhanghai.android.materialratingbar.MaterialRatingDrawable;
import tjesmits.android.avans.nl.bolbrowser.R;
import tjesmits.android.avans.nl.bolbrowser.controllers.ProductDetailActivity;
import tjesmits.android.avans.nl.bolbrowser.domain.Product;

/**
 * Created by Tom Smits on 9-3-2018.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<Product> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The view (list_row_item) that contains the view items
        public View view;

        // The view items that our view displays
        public TextView mTextViewTitle;
        public TextView mTextViewSummary;
        public TextView mTextViewPrice;

        public ImageView mImageViewProduct;
        public MaterialRatingBar mMaterialRatingBarProduct;

        public ViewHolder(View v) {
            super(v);
            this.view = v;
            this.view.setOnClickListener(this);

            mTextViewTitle = (TextView) v.findViewById(R.id.ProductTitle);
            mTextViewSummary = (TextView) v.findViewById(R.id.ProductSummary);
            mTextViewPrice = (TextView) v.findViewById(R.id.ProductPrice);
            mImageViewProduct = (ImageView) v.findViewById(R.id.ProductImage);
            mMaterialRatingBarProduct = (MaterialRatingBar) v.findViewById(R.id.ProductRating);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Product mProduct = mDataset.get(position);

            Intent personDetailIntent = new Intent(
                    view.getContext().getApplicationContext(),
                    ProductDetailActivity.class);

            personDetailIntent.putExtra(Tags.PRODUCT_TITLE, mProduct);

            view.getContext().startActivity(personDetailIntent);
        }
    }

    // Provide a suitable constructor (depending on the kind of dataset)
    public ProductAdapter(Context context, ArrayList<Product> myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // Create a new view.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_row_item, parent, false);

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Product mProduct = mDataset.get(position);
        holder.mTextViewTitle.setText(mProduct.title);
        holder.mTextViewSummary.setText(mProduct.summary);
        holder.mTextViewPrice.setText(mProduct.price);
        holder.mMaterialRatingBarProduct.setProgress(mProduct.rating / 5);

        Picasso.with(mContext)
                .load(mProduct.imageThumbURL)
                .into(holder.mImageViewProduct);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


