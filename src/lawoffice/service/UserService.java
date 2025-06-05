package lawoffice.service;

import lawoffice.dao.UserDAO;
import lawoffice.model.User;

public class UserService {
    private final UserDAO dao = new UserDAO();

    public User login(String email, String pwd) {
        User u = dao.findByEmail(email);
        if (u != null && pwd.equals(u.getPassword()) && "Active".equalsIgnoreCase(u.getStatus())) {
            return u;
        }
        return null;
    }

    public boolean setUserStatus(int userId, String status) {
        return dao.updateUserStatus(userId, status);
    }


    public boolean registerClient(User u) {
        u.setRole("Client");
        return dao.createUser(u);
    }
}
