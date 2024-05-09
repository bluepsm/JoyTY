package github.bluepsm.joyty.models;

import java.io.Serializable;
import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "request")
@Getter
@Setter
@ToString
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Request implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "body")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(32) default 'PENDING'")
    private ERequest status = ERequest.PENDING;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date created_at;

    @ManyToOne
    @JoinColumn(name = "request_by", referencedColumnName = "id")
    private User request_by;

    @ManyToOne
    @JoinColumn(name = "request_to", referencedColumnName = "id")
    private Post request_to;

    /* @PrePersist
    protected void onCreate() {
        created_at = System.currentTimeMillis();
    } */

    public Request() {}

    public Request(String body) {
        this.body = body;
    }
}
