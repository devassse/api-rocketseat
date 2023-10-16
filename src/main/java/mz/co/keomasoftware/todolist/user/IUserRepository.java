package mz.co.keomasoftware.todolist.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserModel, UUID> {
    UserModel findByUsername(String username);
    UserModel findByUsernameAndPassword(String username, String password);
}
