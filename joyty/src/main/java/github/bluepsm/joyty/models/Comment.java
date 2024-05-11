package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "body")
    @NotBlank
    private String body;

    @Column(name = "created_at")
    @CreatedDate
    private Long created_at;

    @Column(name = "last_updated")
    @LastModifiedDate
    private Long last_updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    /* @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    } */

    public Comment() {}

    public Comment(String body) {
        this.body = body;
    }
}
