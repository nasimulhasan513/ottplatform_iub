package iub.ottplatform_iub.service;

import iub.ottplatform_iub.model.User;
import iub.ottplatform_iub.model.Content;
import iub.ottplatform_iub.model.Subscription;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataStorageService {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String CONTENT_FILE = DATA_DIR + "/content.dat";
    private static final String SUBSCRIPTIONS_FILE = DATA_DIR + "/subscriptions.dat";

    public DataStorageService() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    // User operations
    public void saveUser(User user) {
        List<User> users = loadUsers();
        users.add(user);
        saveObject(users, USERS_FILE);
    }

    public List<User> loadUsers() {
        return loadObject(USERS_FILE);
    }

    public User findUserByEmail(String email) {
        List<User> users = loadUsers();
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Content operations
    public void saveContent(Content content) {
        List<Content> contents = loadContent();
        contents.add(content);
        saveObject(contents, CONTENT_FILE);
    }

    public List<Content> loadContent() {
        return loadObject(CONTENT_FILE);
    }

    public Content findContentById(String contentId) {
        List<Content> contents = loadContent();
        return contents.stream()
                .filter(content -> content.getContentId().equals(contentId))
                .findFirst()
                .orElse(null);
    }

    public void deleteContent(Content content) {
        List<Content> contents = loadContent();
        contents.removeIf(c -> c.getContentId().equals(content.getContentId()));
        saveObject(contents, CONTENT_FILE);
    }

    // Subscription operations
    public void saveSubscription(Subscription subscription) {
        List<Subscription> subscriptions = loadSubscriptions();
        subscriptions.add(subscription);
        saveObject(subscriptions, SUBSCRIPTIONS_FILE);
    }

    public List<Subscription> loadSubscriptions() {
        return loadObject(SUBSCRIPTIONS_FILE);
    }

    public Subscription findSubscriptionByUserId(String userId) {
        List<Subscription> subscriptions = loadSubscriptions();
        return subscriptions.stream()
                .filter(sub -> sub.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    // Generic save/load methods
    private <T> void saveObject(T object, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadObject(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}