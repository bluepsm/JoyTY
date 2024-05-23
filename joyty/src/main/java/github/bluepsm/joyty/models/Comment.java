package github.bluepsm.joyty.models;

import java.io.Serializable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
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

    @Column(name = "createdAt")
    @CreatedDate
    private Long createdAt;

    @Column(name = "lastUpdated")
    @LastModifiedDate
    private Long lastUpdated;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    public Comment() {}

    public Comment(String body) {
        this.body = body;
    }
}
