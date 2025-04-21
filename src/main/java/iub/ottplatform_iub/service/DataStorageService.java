package iub.ottplatform_iub.service;

import iub.ottplatform_iub.model.User;
import iub.ottplatform_iub.model.Content;
import iub.ottplatform_iub.model.Subscription;
import iub.ottplatform_iub.model.SubscriptionPlan;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataStorageService {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.dat";
    private static final String CONTENT_FILE = DATA_DIR + "/content.dat";
    private static final String SUBSCRIPTIONS_FILE = DATA_DIR + "/subscriptions.dat";
    private static final String PLANS_FILE = DATA_DIR + "/plans.dat";

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
        System.out.println("Saving content. Current content count: " + contents.size());
        System.out.println("Content details: " + content.getTitle() + ", ID: " + content.getContentId());

        // Find and update existing content or add new content
        boolean contentExists = false;
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getContentId().equals(content.getContentId())) {
                contents.set(i, content);
                contentExists = true;
                break;
            }
        }

        if (!contentExists) {
            contents.add(content);
        }

        saveObject(contents, CONTENT_FILE);
        System.out.println("Content saved successfully. New content count: " + contents.size());
    }

    public List<Content> loadContent() {
        List<Content> contents = loadObject(CONTENT_FILE);
        System.out.println("Loading content from file. Content count: " + contents.size());
        for (Content content : contents) {
            System.out.println("Loaded content: " + content.getTitle() + ", ID: " + content.getContentId());
        }
        return contents;
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

    // Subscription Plan operations
    public void saveSubscriptionPlan(SubscriptionPlan plan) {
        List<SubscriptionPlan> plans = loadSubscriptionPlans();
        plans.add(plan);
        saveObject(plans, PLANS_FILE);
    }

    public List<SubscriptionPlan> loadSubscriptionPlans() {
        return loadObject(PLANS_FILE);
    }

    public SubscriptionPlan findPlanById(String planId) {
        List<SubscriptionPlan> plans = loadSubscriptionPlans();
        return plans.stream()
                .filter(plan -> plan.getPlanId().equals(planId))
                .findFirst()
                .orElse(null);
    }

    public void updateSubscriptionPlan(SubscriptionPlan updatedPlan) {
        List<SubscriptionPlan> plans = loadSubscriptionPlans();
        for (int i = 0; i < plans.size(); i++) {
            if (plans.get(i).getPlanId().equals(updatedPlan.getPlanId())) {
                plans.set(i, updatedPlan);
                break;
            }
        }
        saveObject(plans, PLANS_FILE);
    }

    public void deleteSubscriptionPlan(String planId) {
        List<SubscriptionPlan> plans = loadSubscriptionPlans();
        plans.removeIf(plan -> plan.getPlanId().equals(planId));
        saveObject(plans, PLANS_FILE);
    }

    // Generic save/load methods
    public <T> void saveObject(T object, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> loadObject(String filePath) {
        File file = new File(filePath);
        System.out.println("Attempting to load from file: " + filePath);
        System.out.println("File exists: " + file.exists());

        if (!file.exists()) {
            System.out.println("File does not exist, returning empty list");
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<T> loadedList = (List<T>) ois.readObject();
            System.out.println("Successfully loaded " + loadedList.size() + " items from file");
            return loadedList;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading from file: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}