//package PibuStory.board;
//
//import java.util.Date;
//import java.util.List;
//
//public class CommentDto {
//
//    private String id;
//    private String boardId;
//    private String author;
//    private String content;
//    private Date createdDate;
//    private int great;
//    private String parentCommentId;
//    private List<String> childrenCommentIds;
//
//    // Getters and Setters
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getBoardId() {
//        return boardId;
//    }
//
//    public void setBoardId(String boardId) {
//        this.boardId = boardId;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
//
//    public int getGreat() {
//        return great;
//    }
//
//    public void setGreat(int great) {
//        this.great = great;
//    }
//
//    public String getParentCommentId() {
//        return parentCommentId;
//    }
//
//    public void setParentCommentId(String parentCommentId) {
//        this.parentCommentId = parentCommentId;
//    }
//
//    public List<String> getChildrenCommentIds() {
//        return childrenCommentIds;
//    }
//
//    public void setChildrenCommentIds(List<String> childrenCommentIds) {
//        this.childrenCommentIds = childrenCommentIds;
//    }
//
//    // toDto method
//    public static CommentDto toDto(Comment comment) {
//        CommentDto dto = new CommentDto();
//        dto.setId(comment.getId());
//        dto.setBoardId(comment.getBoardId());
//        dto.setAuthor(comment.getAuthor());
//        dto.setContent(comment.getContent());
//        dto.setCreatedDate(comment.getCreatedDate());
//        dto.setGreat(comment.getGreat());
//        dto.setParentCommentId(comment.getParentCommentId());
//        dto.setChildrenCommentIds(comment.getChildrenCommentIds());
//        return dto;
//    }
//
//    // toEntity method
//    public Comment toEntity() {
//        Comment comment = new Comment();
//        comment.setId(this.id);
//        comment.setBoardId(this.boardId);
//        comment.setAuthor(this.author);
//        comment.setContent(this.content);
//        comment.setCreatedDate(this.createdDate);
//        comment.setGreat(this.great);
//        comment.setParentCommentId(this.parentCommentId);
//        comment.setChildrenCommentIds(this.childrenCommentIds);
//        return comment;
//    }
//}
package PibuStory.board;

import PibuStory.board.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDto {

    private String id;
    private String boardId;
    private String author;
    private String content;
    private String createdDate;
    private int great;
    private String parentCommentId;
    private List<String> childrenCommentIds = new ArrayList<>();

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

    // Static method to convert from Comment to CommentDto
    public static CommentDto toDto(PibuStory.board.Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setBoardId(comment.getBoardId());
        dto.setAuthor(comment.getAuthor());
        dto.setContent(comment.getContent());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setGreat(comment.getGreat());
        dto.setParentCommentId(comment.getParentCommentId());
        dto.setChildrenCommentIds(comment.getChildrenCommentIds());
        return dto;
    }

    // Method to convert from CommentDto to Comment entity
    public PibuStory.board.Comment toEntity() {
        PibuStory.board.Comment comment = new Comment();
        comment.setId(this.id);
        comment.setBoardId(this.boardId);
        comment.setAuthor(this.author);
        comment.setContent(this.content);
        comment.setCreatedDate(this.createdDate);
        comment.setGreat(this.great);
        comment.setParentCommentId(this.parentCommentId);
        comment.setChildrenCommentIds(this.childrenCommentIds);
        return comment;
    }
}
