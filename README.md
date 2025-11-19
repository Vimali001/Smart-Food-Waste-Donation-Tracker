# Smart Food Waste & Donation Tracker

A comprehensive Spring Boot web application for tracking food items, predicting expiry dates, suggesting recipes, and facilitating food donations to NGOs.

## 🚀 Features

### User Module
- ✅ User Registration & Login
- ✅ Add Food Items (Name, Quantity, Purchase Date, Expiry Date)
- ✅ Edit / Remove Food Items
- ✅ Expiry Alerts (e.g., "Expiring in 2 days")
- ✅ Recipe Suggestions for leftover/expiring items
- ✅ Donate Food to nearby NGOs

### NGO Module
- ✅ NGO Registration & Login
- ✅ View Nearby Donation Requests
- ✅ Accept Pickup Requests
- ✅ Update Status: Collected / Delivered / Completed

### Admin Module
- ✅ Admin Login
- ✅ Manage Users & NGOs
- ✅ Approve/Reject NGOs
- ✅ Platform Statistics:
  - Total food saved
  - Total donations
  - Waste reduced report

### Smart Features
1. **Expiry Prediction Logic (Java)**
   - Based on category:
     - Vegetable = 3 days
     - Cooked food = 1 day
     - Packaged = 15 days
     - Fruit = 5 days
     - Dairy = 7 days
     - Meat = 2 days
     - Bread = 3 days

2. **Leftover Recipe Suggestions**
   - Stores recipes in database
   - Suggests based on items expiring soon

3. **Donation Matching System**
   - Finds nearest NGOs based on pincode

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.1.5
- **Frontend**: HTML5, CSS3, JavaScript (Vanilla)
- **Database**: MySQL 8.0
- **ORM**: JPA/Hibernate
- **Build Tool**: Maven

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Your favorite IDE (IntelliJ IDEA, Eclipse, etc.)

## 🗄️ Database Setup

1. Create a MySQL database:
```sql
CREATE DATABASE food_tracker_db;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

3. The application will automatically create tables on startup (DDL auto mode is set to `update`).

## 🚀 Running the Application

1. **Clone the repository** (if applicable)

2. **Configure MySQL**:
   - Update `application.properties` with your MySQL credentials

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```
   
   Or use your IDE to run `FoodTrackerApplication.java`

4. **Access the application**:
   - Open browser: `http://localhost:8080`
   - Landing page: `http://localhost:8080/index.html`

## 📝 Default Admin Credentials

- **Email**: `admin@foodtracker.com`
- **Password**: `admin123`

(You can also create a user with role "ADMIN" in the database)

## 📁 Project Structure

```
src/main/
├── java/com/foodtracker/
│   ├── config/              # Configuration classes
│   │   ├── WebMvcConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/          # REST Controllers
│   │   ├── AdminController.java
│   │   ├── DonationController.java
│   │   ├── FoodItemController.java
│   │   ├── NGOController.java
│   │   ├── RecipeController.java
│   │   └── UserController.java
│   ├── model/               # Entity classes
│   │   ├── Donation.java
│   │   ├── FoodItem.java
│   │   ├── NGO.java
│   │   ├── Recipe.java
│   │   └── User.java
│   ├── repository/          # JPA Repositories
│   ├── service/             # Business Logic
│   └── FoodTrackerApplication.java
└── resources/
    ├── static/              # Frontend files
    │   ├── css/
    │   │   └── style.css
    │   ├── js/
    │   │   └── api.js
    │   ├── index.html
    │   ├── login.html
    │   ├── register.html
    │   ├── dashboard.html
    │   ├── addfood.html
    │   ├── foodlist.html
    │   ├── donate.html
    │   ├── donations_status.html
    │   ├── admin_dashboard.html
    │   └── ngo_dashboard.html
    └── application.properties
```

## 📚 API Endpoints

### User APIs
- `POST /api/users/register` - Register new user
- `POST /api/users/login` - User login
- `GET /api/users/current` - Get current user
- `POST /api/users/logout` - User logout

### Food Item APIs
- `GET /api/food-items` - Get all user's food items
- `GET /api/food-items/active` - Get active food items
- `GET /api/food-items/expiring-soon` - Get expiring items
- `POST /api/food-items` - Add food item
- `PUT /api/food-items/{id}` - Update food item
- `DELETE /api/food-items/{id}` - Delete food item

### Donation APIs
- `POST /api/donations` - Create donation request
- `GET /api/donations/user` - Get user's donations
- `GET /api/donations/ngo` - Get NGO's donations
- `GET /api/donations/pending` - Get pending donations
- `POST /api/donations/{id}/accept` - Accept donation (NGO)
- `PUT /api/donations/{id}/status` - Update donation status

### NGO APIs
- `POST /api/ngos/register` - Register NGO
- `POST /api/ngos/login` - NGO login
- `GET /api/ngos/nearby?pincode=XXXXXX` - Find nearby NGOs

### Recipe APIs
- `GET /api/recipes` - Get all recipes
- `GET /api/recipes/suggest` - Get recipe suggestions
- `GET /api/recipes/category/{category}` - Get recipes by category

### Admin APIs
- `POST /api/admin/login` - Admin login
- `GET /api/admin/statistics` - Get platform statistics
- `GET /api/admin/users` - Get all users
- `GET /api/admin/ngos` - Get all NGOs
- `GET /api/admin/ngos/pending` - Get pending NGOs
- `PUT /api/admin/ngos/{id}/approve` - Approve NGO
- `PUT /api/admin/ngos/{id}/reject` - Reject NGO

## 🎨 Frontend Pages

1. **index.html** - Landing page
2. **login.html** - Login page (User/NGO/Admin)
3. **register.html** - Registration page (User/NGO)
4. **dashboard.html** - User dashboard
5. **addfood.html** - Add food item form
6. **foodlist.html** - List all food items
7. **donate.html** - Donate food page
8. **donations_status.html** - View donation status
9. **admin_dashboard.html** - Admin dashboard
10. **ngo_dashboard.html** - NGO dashboard

## 🔐 Security

- Spring Security is configured to allow public access to static resources and API endpoints
- Session-based authentication
- CORS enabled for all origins (configure for production)

## 📝 Notes

- The application uses session-based authentication
- Password is stored in plain text (for demo purposes - use password hashing in production)
- NGO status must be "APPROVED" to accept donations
- Food items are marked as "DONATED" when donation is requested

## 🐛 Troubleshooting

1. **Database Connection Error**:
   - Verify MySQL is running
   - Check credentials in `application.properties`
   - Ensure database exists

2. **Port 8080 already in use**:
   - Change port in `application.properties`: `server.port=8081`

3. **Tables not created**:
   - Check `spring.jpa.hibernate.ddl-auto=update` in `application.properties`
   - Review application logs for errors

## 📄 License

This project is open source and available for educational purposes.

## 👨‍💻 Development

To contribute or modify:
1. Ensure all dependencies are resolved: `mvn clean install`
2. Run tests (if any): `mvn test`
3. Start development server with hot reload enabled

---

**Made with ❤️ for reducing food waste and helping communities**

