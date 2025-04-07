package PibuStory.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;  // Field 어노테이션 추가
import java.util.List;

@Getter
@Setter
public class Cosmetics {

    private String id;

    @Field("cosmetics_type")  // 필드 이름을 MongoDB 컬럼 이름에 맞춰 매핑
    private String cosmeticsType;

    private String product;
    private String image_link;
    private String detail_link;
    private String brand;
    private int price_after;
    private int price_before;
    private String brand_link;
    private List<String> ingredient;
    private int great;
    private int review_count;
    private double rating_average;
    private double star_5;
    private double star_4;
    private double star_3;
    private double star_2;
    private double star_1;
    private double dry;
    private double combination;
    private double oily;
    private double pibu_3;
    private double pibu_2;
    private double pibu_1;
    private double irritation_no;
    private double irritation_normal;
    private double irritation_yes;

    // 기타 getter, setter 메서드들...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getIrritation_yes() {
        return irritation_yes;
    }

    public void setIrritation_yes(double irritation_yes) {
        this.irritation_yes = irritation_yes;
    }

    public double getIrritation_normal() {
        return irritation_normal;
    }

    public void setIrritation_normal(double irritation_normal) {
        this.irritation_normal = irritation_normal;
    }

    public double getIrritation_no() {
        return irritation_no;
    }

    public void setIrritation_no(double irritation_no) {
        this.irritation_no = irritation_no;
    }

    public double getPibu_1() {
        return pibu_1;
    }

    public void setPibu_1(double pibu_1) {
        this.pibu_1 = pibu_1;
    }

    public double getPibu_2() {
        return pibu_2;
    }

    public void setPibu_2(double pibu_2) {
        this.pibu_2 = pibu_2;
    }

    public double getPibu_3() {
        return pibu_3;
    }

    public void setPibu_3(double pibu_3) {
        this.pibu_3 = pibu_3;
    }

    public double getOily() {
        return oily;
    }

    public void setOily(double oily) {
        this.oily = oily;
    }

    public double getCombination() {
        return combination;
    }

    public void setCombination(double combination) {
        this.combination = combination;
    }

    public double getDry() {
        return dry;
    }

    public void setDry(double dry) {
        this.dry = dry;
    }

    public double getStar_1() {
        return star_1;
    }

    public void setStar_1(double star_1) {
        this.star_1 = star_1;
    }

    public double getStar_2() {
        return star_2;
    }

    public void setStar_2(double star_2) {
        this.star_2 = star_2;
    }

    public double getStar_3() {
        return star_3;
    }

    public void setStar_3(double star_3) {
        this.star_3 = star_3;
    }

    public double getStar_4() {
        return star_4;
    }

    public void setStar_4(double star_4) {
        this.star_4 = star_4;
    }

    public double getStar_5() {
        return star_5;
    }

    public void setStar_5(double star_5) {
        this.star_5 = star_5;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public double getRating_average() {
        return rating_average;
    }

    public void setRating_average(double rating_average) {
        this.rating_average = rating_average;
    }

    public int getReview_count() {
        return review_count;
    }

    public void setReview_count(int review_count) {
        this.review_count = review_count;
    }

    public List<String> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<String> ingredient) {
        this.ingredient = ingredient;
    }

    public String getBrand_link() {
        return brand_link;
    }

    public void setBrand_link(String brand_link) {
        this.brand_link = brand_link;
    }

    public int getPrice_before() {
        return price_before;
    }

    public void setPrice_before(int price_before) {
        this.price_before = price_before;
    }

    public int getPrice_after() {
        return price_after;
    }

    public void setPrice_after(int price_after) {
        this.price_after = price_after;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDetail_link() {
        return detail_link;
    }

    public void setDetail_link(String detail_link) {
        this.detail_link = detail_link;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCosmetics_type() {
        return cosmeticsType;
    }

    public void setCosmetics_type(String cosmeticsType) {
        this.cosmeticsType = cosmeticsType;
    }
}
