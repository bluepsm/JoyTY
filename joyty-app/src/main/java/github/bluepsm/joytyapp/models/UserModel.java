package github.bluepsm.joytyapp.models;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class UserModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private Date date_of_birth;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    // UNIX time
    @Column(name = "created_at")
    private Long created_at;

    @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PostModel> posts;

    public UserModel() {}

    public UserModel(String username, String password, String email, String first_name, 
        String last_name, String gender, Date date_of_birth, String phone_number,
        String city, String country) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.first_name = first_name;
            this.last_name = last_name;
            this.gender = gender;
            this.date_of_birth = date_of_birth;
            this.phone_number = phone_number;
            this.city = city;
            this.country = country;
    }

}
