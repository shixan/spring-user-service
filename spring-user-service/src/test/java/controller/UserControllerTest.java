package controller;

import dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = com.example.springuserservice.UserServiceApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void testCreateUser() throws Exception {
        UserDto dto = new UserDto();
        dto.setName("Igor");
        dto.setEmail("igor@mail.ru");
        dto.setAge(22);

        UserDto savedDto = new UserDto();
        savedDto.setId(2L);
        savedDto.setName(dto.getName());
        savedDto.setEmail(dto.getEmail());
        savedDto.setAge(dto.getAge());

        when(userService.create(any(UserDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/users")
                        .contentType("application/json")
                        .content("""
                            {
                              "name": "Igor",
                              "email": "igor@mail.ru",
                              "age": 22
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Igor"));
    }

    @Test
    void testGetUser() throws Exception {
        UserDto dto = new UserDto();
        dto.setId(1L);
        dto.setName("Misha");
        dto.setEmail("misha@mail.ru");
        dto.setAge(33);

        when(userService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Misha"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDto user1 = new UserDto();
        user1.setId(1L);
        user1.setName("Sasha");
        user1.setEmail("sasha@mail.ru");
        user1.setAge(25);

        UserDto user2 = new UserDto();
        user2.setId(2L);
        user2.setName("Dima");
        user2.setEmail("Dima@mail.ru");
        user2.setAge(30);

        when(userService.getAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Sasha"))
                .andExpect(jsonPath("$[1].email").value("dima@mail.ru"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }
}
