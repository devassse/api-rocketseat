package mz.co.keomasoftware.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;
    
    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsername(userModel.getUsername());
        if(user != null ){
            System.err.println("User Name ja existe!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User exists");
        }

        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashed);
        var newUser = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(newUser);
    }

    @PostMapping("/doLogin")
    public ResponseEntity doLogin(@RequestBody UserModel userModel){
        var user = this.userRepository.findByUsernameAndPassword(userModel.getUsername(), userModel.getPassword());
        System.out.println("User" + user);
        if(user != null ){
            System.err.println("User Encontrado!");
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("User Encontrado!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Dados de login errados!");
    }

}
