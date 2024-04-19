package github.bluepsm.joytyapp.models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "request")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RequestModel implements Serializable{

    enum Status {
        PENDING,
        ACCEPT,
        REJECT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "body")
    private String body;

    @Column(name = "status")
    private String status = Status.PENDING.toString();

    @Column(name = "created_at")
    private Long created_at;

    @ManyToOne
    @JoinColumn(name = "request_by", referencedColumnName = "id")
    private UserModel request_by;

    @ManyToOne
    @JoinColumn(name = "request_to", referencedColumnName = "id")
    private PostModel request_to;

    @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    }

    public RequestModel() {}

    public RequestModel(String body, String status) {
        this.body = body;
        this.status = status;
    }
    
}
