package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "posts")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "body")
    @NotBlank
    private String body;

    @Column(name = "party_size")
    @Min(1)
    @NotNull
    private Integer party_size;

    @Column(name = "place_name")
    private String place_name;
    
    @Column(name = "place_address")
    private String place_address;
    
    @Column(name = "place_latitude")
    private Double place_latitude;
    
    @Column(name = "place_longtitude")
    private Double place_longtitude;

    @Column(name = "meeting_datetime")
    private Date meeting_datetime;

    @Column(name = "cost_estimate", columnDefinition = "money")
    private BigDecimal cost_estimate;

    @Column(name = "cost_share")
    private Boolean cost_share;

    @Column(name = "joinner", columnDefinition = "int default 0")
    @Min(0)
    @NotNull
    private Integer joinner;

    @Column(name = "meeting_done", columnDefinition = "bit default 0")
    private Boolean meeting_done;

    @Column(name = "created_at")
    @CreatedDate
    private Long created_at;

    @Column(name = "last_updated")
    @LastModifiedDate
    private Long last_updated;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User author;

    @JsonIgnore
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "join", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> requests;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "party",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members;

    /* @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    } */

    public Post() {}

    public Post(
    		String body, 
    		Integer party_size, 
    		String place_name, 
    		String place_address, 
    		Double place_latitude, 
    		Double place_longtitude, 
    		Date meeting_datetime, 
    		BigDecimal cost_estimate, 
    		Boolean cost_share) {
    	this.body = body;
        this.party_size = party_size;
        this.place_name = place_name;
		this.place_address = place_address;
		this.place_latitude = place_latitude;
		this.place_longtitude = place_longtitude;
		this.meeting_datetime = meeting_datetime;
        this.cost_estimate = cost_estimate;
        this.cost_share = cost_share;
    }
}
