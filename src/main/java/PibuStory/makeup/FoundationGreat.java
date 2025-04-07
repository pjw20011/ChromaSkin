package PibuStory.makeup;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "foundation_great")
public class FoundationGreat {

    @Id
    private String id;
    private String foundationLabel;
    private String color;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoundationLabel() {
        return foundationLabel;
    }

    public void setFoundationLabel(String foundationLabel) {
        this.foundationLabel = foundationLabel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
