package PibuStory.personalcolor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Lip")  // db 컬렉션에 매핑
public class Lib {
    @Id
    private String id;
    private String lib_type;
    private String color_type;
    private String product;
    private String image_link;
    private String detail_link;
    private String brand;
    private int price_after;
    private int price_before;
    private int review_count;
    private double average;

    // Getters and Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLib_type() {
        return lib_type;
    }

    public void setLib_type(String lib_type) {
        this.lib_type = lib_type;
    }

    public String getColor_type() {
        return color_type;
    }

    public void setColor_type(String color_type) {
        this.color_type = color_type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public void setDetail_link(String detail_link) {
        this.detail_link = detail_link;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice_after() {
        return price_after;
    }

    public void setPrice_after(int price_after) {
        this.price_after = price_after;
    }

    public int getPrice_before() {
        return price_before;
    }

    public void setPrice_before(int price_before) {
        this.price_before = price_before;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}