package PibuStory.product;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cosmetics_great")
public class CosmeticsGreat {

    @Id
    private String id;
    private String cosmeticsId;
    private String nickname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCosmeticsId() {
        return cosmeticsId;
    }

    public void setCosmeticsId(String cosmeticsId) {
        this.cosmeticsId = cosmeticsId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
