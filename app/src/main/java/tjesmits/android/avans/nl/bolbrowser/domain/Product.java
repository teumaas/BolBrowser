package tjesmits.android.avans.nl.bolbrowser.domain;

import java.io.Serializable;

/**
 * Created by Tom Smits on 9-3-2018.
 */

public class Product implements Serializable {

    public String ID;
    public String title;
    public String summary;
    public int rating;
    public String price;
    public String longDescription;
    public String imageThumbURL;
    public String imageURL;

    public Product(String title, String summary, int rating, String price, String ID, String longDescription, String imageThumbURL, String imageURL) {
        this.ID = ID;
        this.title = title;
        this.summary = summary;
        this.rating = rating;
        this.price = price;
        this.longDescription = longDescription;
        this.imageThumbURL = imageThumbURL;
        this.imageURL = imageURL;
    }

    public static class ProductBuilder {

        public String ID;
        public String title;
        public String summary;
        public int rating;
        public String price;
        public String longDescription;
        public String imageThumbURL;
        public String imageURL;

        public ProductBuilder(String title, String summary, int rating, String price) {
            this.title = title;
            this.summary = summary;
            this.rating = rating;
            this.price = price;
        }

        public ProductBuilder setID(String ID) {
            this.ID = ID;
            return this;
        }

        public ProductBuilder setDescription(String longDescription) {
            this.longDescription = longDescription;
            return this;
        }

        public ProductBuilder setImageURL(String imageThumbURL, String imageURL) {
            this.imageThumbURL = imageThumbURL;
            this.imageURL = imageURL;
            return this;
        }

        public Product build() {
            return new Product(title, summary, rating, price, ID, longDescription, imageThumbURL, imageURL);
        }

    }
}
