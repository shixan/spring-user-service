package service;

import dto.UserDto;
import mapper.UserMapper;
import model.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getById(Long id) {
        return userRepository.findById(id).map(UserMapper::toDto).orElse(null);
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }

    public UserDto create(UserDto dto) {
        User user = UserMapper.toEntity(dto);
        return UserMapper.toDto(userRepository.save(user));
    }

    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return UserMapper.toDto(userRepository.save(user));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
