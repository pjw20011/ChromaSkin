package PibuStory.board;

import PibuStory.board.Reply;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "comment")
public class Comment {

    @Id
    private String id;
    private String boardId;
    private String author;
    private String content;
    private String createdDate;
    private int great = 0;
    private String parentCommentId;
    private List<String> childrenCommentIds = new ArrayList<>();
    private List<PibuStory.board.Reply> replies = new ArrayList<>(); // replies 필드 추가

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getGreat() {
        return great;
    }

    public void setGreat(int great) {
        this.great = great;
    }

    public String getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(String parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public List<String> getChildrenCommentIds() {
        return childrenCommentIds;
    }

    public void setChildrenCommentIds(List<String> childrenCommentIds) {
        this.childrenCommentIds = childrenCommentIds;
    }

    // Helper method to add a child comment ID
    public void addChildCommentId(String childId) {
        this.childrenCommentIds.add(childId);
    }

    public List<PibuStory.board.Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }
}
