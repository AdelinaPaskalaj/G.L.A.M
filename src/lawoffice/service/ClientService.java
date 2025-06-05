package lawoffice.service;

import lawoffice.dao.ClientDAO;

public class ClientService {

    private ClientDAO clientDAO = new ClientDAO();

    public String registerClient(String name, String email, String personalId, String phone) {
        if (clientDAO.emailExists(email)) {
            return "Email already exists!";
        }
        if (clientDAO.personalIdExists(personalId)) {
            return "Personal ID already exists!";
        }
        boolean inserted = clientDAO.insertClient(name, email, personalId, phone);
        if (inserted) {
            return "Client registered successfully.";
        } else {
            return "Error registering client.";
        }
    }
}
