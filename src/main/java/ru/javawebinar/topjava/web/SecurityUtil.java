package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    enum User {
        USER(1),
        ADMIN(2);

        private int id;

        User(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    static User currentUser = User.USER;

    public static void setCurrentUser(User currentUser) {
        SecurityUtil.currentUser = currentUser;
    }

    public static int authUserId() {
        return currentUser.getId();
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}