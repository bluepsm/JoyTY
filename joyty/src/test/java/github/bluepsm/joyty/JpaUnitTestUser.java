package github.bluepsm.joyty;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import github.bluepsm.joyty.models.User;
import github.bluepsm.joyty.repositories.UserRepository;

@DataJpaTest
public class JpaUnitTestUser {
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	UserRepository repository;
	
	@Test
	public void shouldFindNoUsersIfEmpty() {
		Iterable<User> users = repository.findAll();
		assertThat(users).isEmpty();
	}
	
	@Test
	public void shouldStoreUser() {
		User user = repository.save(new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco"));
		assertThat(user).hasFieldOrPropertyWithValue("username", "jdoe");
		assertThat(user).hasFieldOrPropertyWithValue("password", "password1234");
		assertThat(user).hasFieldOrPropertyWithValue("email", "jdoe@gmail.com");
		assertThat(user).hasFieldOrPropertyWithValue("firstName", "John");
		assertThat(user).hasFieldOrPropertyWithValue("lastName", "Doe");
		assertThat(user).hasFieldOrPropertyWithValue("gender", "male");
		assertThat(user).hasFieldOrPropertyWithValue("dateOfBirth", new Date(2000-11-06));
		assertThat(user).hasFieldOrPropertyWithValue("phoneNumber", "062-745-4515");
		assertThat(user).hasFieldOrPropertyWithValue("country", "United States");
		assertThat(user).hasFieldOrPropertyWithValue("state", "California");
		assertThat(user).hasFieldOrPropertyWithValue("city", "San Francisco");
	}
	
	@Test
	public void shouldFindAllUsers() {
		User user1 = new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user1);
		
		User user2 = new User(
				"janejane",
				"1234pass",
				"janeddoe@gmail.com",
				"Jane",
				"Doe",
				"female",
				new Date(1998-01-18),
				"085-455-4714",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user2);
		
		Iterable<User> users = repository.findAll();
		assertThat(users).hasSize(2).contains(user1, user2);
	}
	
	@Test
	public void shouldFindUserById() {
		User user1 = new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user1);
		
		User user2 = new User(
				"janejane",
				"1234pass",
				"janeddoe@gmail.com",
				"Jane",
				"Doe",
				"female",
				new Date(1998-01-18),
				"085-455-4714",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user2);
		
		User foundUser = repository.findById(user2.getId()).get();
		assertThat(foundUser).isEqualTo(user2);
	}
	
	@Test
	public void shouldUpdateUserById() {
		User user1 = new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user1);
		
		User user2 = new User(
				"janejane",
				"1234pass",
				"janeddoe@gmail.com",
				"Jane",
				"Doe",
				"female",
				new Date(1998-01-18),
				"085-455-4714",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user2);
		
		User updateUser2 = new User(
				"janeD",
				"12345678",
				"jjaneddoe@gmail.com",
				"Janet",
				"Doak",
				"other",
				new Date(1995-07-20),
				"065-812-1214",
				"Japan",
				"Tokyo",
				"Shibuya");
		
		User user = repository.findById(user2.getId()).get();
		user.setUsername(updateUser2.getUsername());
		user.setPassword(updateUser2.getPassword());
		user.setEmail(updateUser2.getEmail());
		user.setFirstName(updateUser2.getFirstName());
		user.setLastName(updateUser2.getLastName());
		user.setGender(updateUser2.getGender());
		user.setDateOfBirth(updateUser2.getDateOfBirth());
		user.setPhoneNumber(updateUser2.getPhoneNumber());
		user.setCountry(updateUser2.getCountry());
		user.setState(updateUser2.getState());
		user.setCity(updateUser2.getCity());
		repository.save(user);
		
		User checkUser = repository.findById(user2.getId()).get();
		assertThat(checkUser.getId()).isEqualTo(user2.getId());
		assertThat(checkUser.getUsername()).isEqualTo(user2.getUsername());
		assertThat(checkUser.getPassword()).isEqualTo(user2.getPassword());
		assertThat(checkUser.getEmail()).isEqualTo(user2.getEmail());
		assertThat(checkUser.getFirstName()).isEqualTo(user2.getFirstName());
		assertThat(checkUser.getLastName()).isEqualTo(user2.getLastName());
		assertThat(checkUser.getGender()).isEqualTo(user2.getGender());
		assertThat(checkUser.getDateOfBirth()).isEqualTo(user2.getDateOfBirth());
		assertThat(checkUser.getPhoneNumber()).isEqualTo(user2.getPhoneNumber());
		assertThat(checkUser.getCountry()).isEqualTo(user2.getCountry());
		assertThat(checkUser.getState()).isEqualTo(user2.getState());
		assertThat(checkUser.getCity()).isEqualTo(user2.getCity());
	}
	
	@Test
	public void shouldDeleteUserById() {
		User user1 = new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user1);
		
		User user2 = new User(
				"janejane",
				"1234pass",
				"janeddoe@gmail.com",
				"Jane",
				"Doe",
				"female",
				new Date(1998-01-18),
				"085-455-4714",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user2);
		
		repository.deleteById(user1.getId());
		
		Iterable<User> users = repository.findAll();
		
		assertThat(users).hasSize(1).contains(user2);
	}
	
	@Test
	public void shouldDeleteAllUsers() {
		User user1 = new User(
				"jdoe",
				"password1234",
				"jdoe@gmail.com",
				"John",
				"Doe",
				"male",
				new Date(2000-11-06),
				"062-745-4515",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user1);
		
		User user2 = new User(
				"janejane",
				"1234pass",
				"janeddoe@gmail.com",
				"Jane",
				"Doe",
				"female",
				new Date(1998-01-18),
				"085-455-4714",
				"United States",
				"California",
				"San Francisco");
		entityManager.persist(user2);
		
		repository.deleteAll();
		
		assertThat(repository.findAll().isEmpty());
	}
}
