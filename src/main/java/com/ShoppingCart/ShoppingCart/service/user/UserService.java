package com.ShoppingCart.ShoppingCart.service.user;

import com.ShoppingCart.ShoppingCart.dto.UserDto;
import com.ShoppingCart.ShoppingCart.entity.Role;
import com.ShoppingCart.ShoppingCart.entity.User;
import com.ShoppingCart.ShoppingCart.exceptions.AlreadyExistException;
import com.ShoppingCart.ShoppingCart.exceptions.ResourceNotFoundException;
import com.ShoppingCart.ShoppingCart.repo.RoleRepository;
import com.ShoppingCart.ShoppingCart.repo.UserRepository;
import com.ShoppingCart.ShoppingCart.request.CreateUserRequest;
import com.ShoppingCart.ShoppingCart.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;



    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        System.out.println("CART = " + user.getCart());
        return user;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail((request.getEmail()))) {
            throw new AlreadyExistException(request.getEmail() + " User already exist");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        existingUser.setFirstName(request.getFirstName());
        existingUser.setLastName(request.getLastName());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundException("User Not Found");});
    }

    @Override
    public UserDto convertToUserDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
