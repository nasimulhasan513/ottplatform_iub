# OTT Platform

A JavaFX-based Over-The-Top (OTT) media streaming platform that allows users to upload, review, and watch content with different user roles and permissions.

## Features

### User Roles

1. **Regular Users**

   - Browse and watch approved content
   - Search for content
   - View content details
   - Rate and review content

2. **Content Uploaders**

   - Upload new content (videos)
   - Add content metadata (title, description, genre, etc.)
   - View their uploaded content
   - Track content status

3. **Content Reviewers**

   - Review pending content
   - Approve or reject content
   - View content details
   - Filter content by status

4. **Finance Managers**

   - View revenue overview
   - Manage refund requests
   - Configure payment gateways
   - Generate financial reports

5. **Subscription Managers**
   - Create and manage subscription plans
   - Handle user subscriptions
   - Create promotions
   - Monitor subscription metrics

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── iub/
│   │       └── ottplatform_iub/
│   │           ├── controller/         # FXML controllers
│   │           ├── model/             # Data models
│   │           ├── service/           # Business logic
│   │           └── OTTPlatformApplication.java
│   └── resources/
│       ├── fxml/                      # FXML views
│       └── styles/                    # CSS styles
```

## Key Components

### Models

- `User.java`: User information and authentication
- `Content.java`: Content metadata and file information
- `Subscription.java`: Subscription plans and user subscriptions

### Controllers

- `LoginController.java`: User authentication
- `UserDashboardController.java`: Regular user interface
- `UploaderDashboardController.java`: Content upload interface
- `ReviewerDashboardController.java`: Content review interface
- `FinanceDashboardController.java`: Financial management interface
- `SubscriptionDashboardController.java`: Subscription management interface

### Services

- `DataStorageService.java`: Data persistence and retrieval
- `AuthenticationService.java`: User authentication and authorization

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven
- JavaFX SDK

### Installation

1. Clone the repository
2. Import the project into your IDE
3. Configure JavaFX SDK in your IDE
4. Build the project using Maven:
   ```bash
   mvn clean install
   ```

### Running the Application

1. Run the main class: `OTTPlatformApplication`
2. Default login credentials:
   - Username: admin
   - Password: admin

## User Interface

### Login Screen

- Username/password authentication
- Role-based access control

### User Dashboard

- Content browsing
- Search functionality
- Content details view
- Rating and review system

### Uploader Dashboard

- Content upload form
- Content metadata management
- Upload history
- Status tracking

### Reviewer Dashboard

- Content review interface
- Approval/rejection workflow
- Status filtering
- Content details view

### Finance Dashboard

- Revenue overview
- Refund management
- Payment gateway configuration
- Report generation

### Subscription Dashboard

- Plan management
- User subscription tracking
- Promotion management
- Subscription analytics

## Development Guidelines

### Code Style

- Follow Java coding conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Maintain consistent indentation

### Testing

- Unit tests for business logic
- Integration tests for data services
- UI tests for critical workflows

### Documentation

- Javadoc comments for public methods
- README updates for new features
- Code comments for complex logic

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
