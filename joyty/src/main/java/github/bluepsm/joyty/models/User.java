package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import github.bluepsm.joyty.models.notification.Notification;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "username", length = 30)
    @Size(min = 3, max = 30)
    @NotBlank
    private String username;

	@JsonIgnore
    @Column(name = "password")
    @Size(min = 8)
    @NotBlank
    private String password;

    @Column(name = "email", length = 30)
    @Size(max = 30)
    @Email
    @NotBlank
    private String email;

    @Column(name = "firstName", length = 30)
    @Size(min = 2, max = 30)
    @NotBlank
    private String firstName;

    @Column(name = "lastName", length = 30)
    @Size(min = 2, max = 30)
    @NotBlank
    private String lastName;

    @Column(name = "gender")
    @NotBlank
    private String gender;

    @Column(name = "dateOfBirth")
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    @Column(name = "phoneNumber", length = 12)
    @Size(min = 12, max = 12)
    @NotBlank
    private String phoneNumber;

    @Column(name = "country")
    @NotBlank
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    // UNIX time
    @Column(name = "createdAt")
    @CreatedDate
    private Long createdAt;

    //@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable( name = "userRole", 
		joinColumns = @JoinColumn(name = "userId"),
		inverseJoinColumns = @JoinColumn(name = "roleId") ) 
	private Set<Role> roles;
	 
	@JsonIgnore
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Post> posts;
	
	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Comment> comments;
	
	@JsonIgnore
	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Request> requests;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "members", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Post> parties;
	
	@JsonIgnore
    @ManyToMany(mappedBy = "toUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Notification> notificationRecieve;
	
	@JsonIgnore
    @OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Notification> notificationTo;

    public User() {}

    public User(
    		String username, 
    		String password, 
    		String email, 
    		String firstName, 
    		String lastName, 
    		String gender, 
    		Date dateOfBirth, 
    		String phoneNumber, 
    		String country, 
    		String state, 
    		String city) {
    	this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.state = state;
        this.city = city;
    }
}
