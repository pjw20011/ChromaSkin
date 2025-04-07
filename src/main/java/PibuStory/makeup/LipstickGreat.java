package PibuStory.makeup;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lipstick_great")
public class LipstickGreat {

    @Id
    private String id;
    private String lipstickId;
    private String colorImage;
    private String lipName;
    private String lipBrand;
    private String lipColorCode;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLipstickId() {
        return lipstickId;
    }

    public void setLipstickId(String lipstickId) {
        this.lipstickId = lipstickId;
    }

    public String getColorImage() {
        return colorImage;
    }

    public void setColorImage(String colorImage) {
        this.colorImage = colorImage;
    }

    public String getLipName() {
        return lipName;
    }

    public void setLipName(String lipName) {
        this.lipName = lipName;
    }

    public String getLipBrand() {
        return lipBrand;
    }

    public void setLipBrand(String lipBrand) {
        this.lipBrand = lipBrand;
    }

    public String getLipColorCode() {
        return lipColorCode;
    }

    public void setLipColorCode(String lipColorCode) {
        this.lipColorCode = lipColorCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
