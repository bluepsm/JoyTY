package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "username", length = 30)
    @Size(min = 3, max = 30)
    @NotBlank
    private String username;

    @Column(name = "password")
    @Size(min = 8)
    @NotBlank
    private String password;

    @Column(name = "email", length = 30)
    @Size(max = 30)
    @Email
    @NotBlank
    private String email;

    @Column(name = "first_name", length = 30)
    @Size(min = 2, max = 30)
    @NotBlank
    private String first_name;

    @Column(name = "last_name", length = 30)
    @Size(min = 2, max = 30)
    @NotBlank
    private String last_name;

    @Column(name = "gender")
    @NotBlank
    private String gender;

    @Column(name = "date_of_birth")
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date_of_birth;

    @Column(name = "phone_number", length = 12)
    @Size(min = 12, max = 12)
    @NotBlank
    private String phone_number;

    @Column(name = "country")
    @NotBlank
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    // UNIX time
    @Column(name = "created_at")
    @CreatedDate
    private Long created_at;

	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable( name = "user_role", 
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "role_id") ) 
	private Set<Role> roles;
	  
	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Post> posts;
	  
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Comment> comments;
	  
	@OneToMany(mappedBy = "request_by", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Request> send_request;
	  
	@ManyToMany(mappedBy = "party_member", fetch = FetchType.LAZY, cascade = CascadeType.ALL) 
	private Set<Post> join_party;
	 

    /* @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    } */

    public User() {}

    public User(String username, String password, String email, String first_name, String last_name, String gender, 
    		Date date_of_birth, String phone_number, String country, String state, String city) {
    	this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.phone_number = phone_number;
        this.country = country;
        this.state = state;
        this.city = city;
    }
}
