package github.bluepsm.joytyapp.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PostModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "body")
    private String body;

    @Column(name = "meeting_location")
    private String meeting_location;

    @Column(name = "meeting_city")
    private String meeting_city;

    @Column(name = "meeting_country")
    private String meeting_country;

    @Column(name = "meeting_datetime")
    private Long meeting_datetime;

    @Column(name = "cost_estimate", columnDefinition = "money")
    private BigDecimal cost_estimate;

    @Column(name = "cost_share")
    private Boolean cost_share;

    @Column(name = "meeting_done")
    private Boolean meeting_done;

    @Column(name = "created_at")
    private Long created_at;

    @Column(name = "last_updated")
    private Long last_updated;

    @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    //@ManyToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TagModel> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserModel user;

    public PostModel() {}

    public PostModel(String body, String meeting_location, String meeting_city, String meeting_country, 
        Long meeting_datetime, BigDecimal cost_estimate, Boolean cost_share, Boolean meeting_done) {
            this.body = body;
            this.meeting_location = meeting_location;
            this.meeting_city = meeting_city;
            this.meeting_country = meeting_country;
            this.meeting_datetime = meeting_datetime;
            this.cost_estimate = cost_estimate;
            this.cost_share = cost_share;
            this.meeting_done = meeting_done;
    }

}
