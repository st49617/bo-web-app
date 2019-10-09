package cz.upce.webapp.dao.users.service;

import cz.upce.webapp.dao.users.model.User;
import cz.upce.webapp.dao.users.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Martin Volenec / st46661
 *
 * This service cares about login process in general.
 */

@Service
public class LoginServiceImpl implements InitializingBean
{
    private final UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }

    public User findByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
        {
            return user.get();
        }

        throw new NoSuchElementException("User with given e-mail does not exist!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        /*User user = new User();
        user.setId(1);
        user.setEmail("test@test.cz");
        user.setPassword("$2a$12$TNH.f4YLpJgCVduQPUejUOuIifkJ5T7DjINwgslGI5s4u7.Plm6d.");
        user.setFirstName("Pavel");
        user.setLastName("Jetensky");
        userRepository.save(user);*/
    }
}
