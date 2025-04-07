package PibuStory.makeup;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "makeup") // MongoDB의 컬렉션 이름이 "makeup"인 경우
public class Lipstick {

    @Id
    private String id;
    private String colorimage; // 립스틱 이미지 URL
    private String brand;      // 립스틱 브랜드
    private String name;       // 립스틱 이름
    private String colorname;  // 색상 이름 (예: "심장폭격")
    private String colorcode;  // 색상 코드 (예: "#FF6666")

    // 기본 생성자
    public Lipstick() {}

    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorimage() {
        return colorimage;
    }

    public void setColorimage(String colorimage) {
        this.colorimage = colorimage;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }

    // 디버깅을 위한 toString 메서드 추가
    @Override
    public String toString() {
        return "Lipstick{" +
                "id='" + id + '\'' +
                ", colorimage='" + colorimage + '\'' +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", colorname='" + colorname + '\'' +
                ", colorcode='" + colorcode + '\'' +
                '}';
    }
}
