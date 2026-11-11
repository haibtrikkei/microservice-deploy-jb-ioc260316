package ra.userservice.service;

import ra.userservice.entity.Users;

import java.util.List;

public interface UserService {
    List<Users>  findAll();
    Users findById(Long id);
    Users insert(Users user);
    Users update(Long userId, Users user);
    void deleteById(Long id);
}
