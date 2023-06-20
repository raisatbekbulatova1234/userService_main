package my.project.userservicemain.service;

import lombok.RequiredArgsConstructor;
import my.project.userservicemain.entities.User;
import my.project.userservicemain.entities.dto.UserDTO;
import my.project.userservicemain.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserDTO> findAllUserDTO() {
        return userRepository.findAllUserDTO();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(User user) {
        User userEntity = getById(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(encodeIfNeed(user.getPassword()));
        userEntity.setName(user.getName());
        userEntity.setRoles(user.getRoles());
        return userEntity;
    }

    @Override
    public User save(User user) {
        user.setPassword(encodeIfNeed(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username: " + username);
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    private String encodeIfNeed(String password) {
        return passwordEncoder.encode(password);
    }
}
