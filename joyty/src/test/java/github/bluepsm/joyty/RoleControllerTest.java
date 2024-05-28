package github.bluepsm.joyty;

import javax.net.ssl.SSLEngineResult.Status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import github.bluepsm.joyty.controllers.RoleController;
import github.bluepsm.joyty.models.ERole;
import github.bluepsm.joyty.models.Role;
import github.bluepsm.joyty.repositories.RoleRepository;
import github.bluepsm.joyty.services.RoleService;

@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(RoleController.class)
public class RoleControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	private RoleService service;
	
	@MockBean
	private RoleRepository roleRepository;
	
	Role role1 = new Role(ERole.ROLE_USER);
	Role role2 = new Role(ERole.ROLE_MODERATOR);
	Role role3 = new Role(ERole.ROLE_ADMIN);
	
	@Test
	void shouldCreateRole() throws Exception {
		mockMvc.perform(post("/api/role").contentType(MediaType.APPLICATION_JSON)
		        .content(objectMapper.writeValueAsString(role1)))
		        .andExpect(status().isCreated())
		        .andDo(print());
	}
	
}
