package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "partySize")
    @Min(1)
    @NotNull
    private Integer partySize;

    @Column(name = "placeName")
    private String placeName;
    
    @Column(name = "placeAddress")
    private String placeAddress;
    
    @Column(name = "placeLatitude")
    private Double placeLatitude;
    
    @Column(name = "placeLongtitude")
    private Double placeLongtitude;

    @Column(name = "meeting_datetime")
    private Date meetingDatetime;

    @Column(name = "costEstimate", columnDefinition = "money")
    private BigDecimal costEstimate;

    @Column(name = "costShare")
    private Boolean costShare;

    @Column(name = "joinner", columnDefinition = "int default 0")
    @Min(0)
    @NotNull
    private Integer joinner;

    @Column(name = "meetingDone", columnDefinition = "bit default 0")
    private Boolean meetingDone;

    @Column(name = "createdAt")
    @CreatedDate
    private Long createdAt;

    @Column(name = "lastUpdated")
    @LastModifiedDate
    private Long lastUpdated;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "postTag",
        joinColumns = @JoinColumn(name = "postId"),
        inverseJoinColumns = @JoinColumn(name = "tagId")
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "party",
        joinColumns = @JoinColumn(name = "postId"),
        inverseJoinColumns = @JoinColumn(name = "userId")
    )
    private Set<User> members;

    public Post() {}

    public Post(
    		String body, 
    		Integer partySize, 
    		String placeName, 
    		String placeAddress, 
    		Double placeLatitude, 
    		Double placeLongtitude, 
    		Date meetingDatetime, 
    		BigDecimal costEstimate, 
    		Boolean costShare) {
    	this.body = body;
        this.partySize = partySize;
        this.placeName = placeName;
		this.placeAddress = placeAddress;
		this.placeLatitude = placeLatitude;
		this.placeLongtitude = placeLongtitude;
		this.meetingDatetime = meetingDatetime;
        this.costEstimate = costEstimate;
        this.costShare = costShare;
    }
}
