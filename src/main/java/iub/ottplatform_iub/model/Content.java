package iub.ottplatform_iub.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Content implements Serializable {
    private static final long serialVersionUID = 1L;

    private String contentId;
    private transient SimpleStringProperty title;
    private transient SimpleStringProperty description;
    private transient SimpleStringProperty genre;
    private transient SimpleStringProperty language;
    private transient SimpleIntegerProperty year;
    private transient SimpleStringProperty rating;
    private String filePath;
    private String thumbnailPath;
    private String uploaderId;
    private transient SimpleBooleanProperty isApproved;
    private List<String> subtitles;
    private List<String> reviews;

    @Override
    public String toString() {
        return "Content{" +
                "contentId='" + contentId + '\'' +
                ", title=" + title +
                ", description=" + description +
                ", genre=" + genre +
                ", language=" + language +
                ", year=" + year +
                ", rating=" + rating +
                ", filePath='" + filePath + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", uploaderId='" + uploaderId + '\'' +
                ", isApproved=" + isApproved +
                ", subtitles=" + subtitles +
                ", reviews=" + reviews +
                '}';
    }

    public Content(String contentId, String title, String description, String genre,
                   String language, int year, String rating, String uploaderId) {
        this.contentId = contentId;
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.genre = new SimpleStringProperty(genre);
        this.language = new SimpleStringProperty(language);
        this.year = new SimpleIntegerProperty(year);
        this.rating = new SimpleStringProperty(rating);
        this.uploaderId = uploaderId;
        this.isApproved = new SimpleBooleanProperty(false);
        this.subtitles = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(title.get());
        out.writeObject(description.get());
        out.writeObject(genre.get());
        out.writeObject(language.get());
        out.writeInt(year.get());
        out.writeObject(rating.get());
        out.writeBoolean(isApproved.get());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        title = new SimpleStringProperty((String) in.readObject());
        description = new SimpleStringProperty((String) in.readObject());
        genre = new SimpleStringProperty((String) in.readObject());
        language = new SimpleStringProperty((String) in.readObject());
        year = new SimpleIntegerProperty(in.readInt());
        rating = new SimpleStringProperty((String) in.readObject());
        isApproved = new SimpleBooleanProperty(in.readBoolean());
    }

    // Getters and Setters
    public String getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public String getGenre() {
        return genre.get();
    }

    public SimpleStringProperty genreProperty() {
        return genre;
    }

    public String getLanguage() {
        return language.get();
    }

    public SimpleStringProperty languageProperty() {
        return language;
    }

    public int getYear() {
        return year.get();
    }

    public SimpleIntegerProperty yearProperty() {
        return year;
    }

    public String getRating() {
        return rating.get();
    }

    public SimpleStringProperty ratingProperty() {
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
        return isApproved.get();
    }

    public SimpleBooleanProperty isApprovedProperty() {
        return isApproved;
    }

    public SimpleStringProperty statusProperty() {
        return new SimpleStringProperty(isApproved.get() ? "Approved" : "Pending");
    }

    public List<String> getSubtitles() {
        return subtitles;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public void setLanguage(String language) {
        this.language.set(language);
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public void setRating(String rating) {
        this.rating.set(rating);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setApproved(boolean approved) {
        this.isApproved.set(approved);
    }

    public void addSubtitle(String subtitlePath) {
        subtitles.add(subtitlePath);
    }

    public void addReview(String review) {
        reviews.add(review);
    }
}