package iub.ottplatform_iub.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Content implements Serializable {
    private String contentId;
    private String title;
    private String description;
    private String genre;
    private String language;
    private int year;
    private String rating;
    private String filePath;
    private String thumbnailPath;
    private String uploaderId;
    private boolean isApproved;
    private List<String> subtitles;
    private List<String> reviews;

    public Content(String contentId, String title, String description, String genre,
            String language, int year, String rating, String uploaderId) {
        this.contentId = contentId;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.language = language;
        this.year = year;
        this.rating = rating;
        this.uploaderId = uploaderId;
        this.isApproved = false;
        this.subtitles = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    // Getters and Setters
    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public int getYear() {
        return year;
    }

    public String getRating() {
        return rating;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public void addSubtitle(String subtitlePath) {
        subtitles.add(subtitlePath);
    }

    public void addReview(String review) {
        reviews.add(review);
    }
}