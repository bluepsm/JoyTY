package github.bluepsm.joyty.models.notification;

import java.io.Serializable;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import github.bluepsm.joyty.models.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "type")
	@Enumerated(EnumType.STRING)
	private EType type;
	
	@Column(name = "entity")
	@Enumerated(EnumType.STRING)
	private EEntity entity;
	
	@Column(name = "entityId")
	@NotNull
	private Long entityId;
	
	@Column(name = "relatedEntity")
	@Enumerated(EnumType.STRING)
	private EEntity relatedEntity;
	
	@Column(name = "relatedEntityId")
	@NotNull
	private Long relatedEntityId;
	
	@Column(name = "createdAt")
    @CreatedDate
    private Long createdAt;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable( name = "notificationTo", 
		joinColumns = @JoinColumn(name = "notificationId"),
		inverseJoinColumns = @JoinColumn(name = "userId") ) 
	private Set<User> toUsers;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable( name = "notificationFrom", 
		joinColumns = @JoinColumn(name = "notificationId"),
		inverseJoinColumns = @JoinColumn(name = "userId") ) 
	private User fromUser;
	
	public Notification() {}
	
	public Notification(EType type, EEntity entity, Long entityId, EEntity relatedEntity, Long relatedEntityId) {
		this.type = type;
		this.entity = entity;
		this.entityId = entityId;
		this.relatedEntity = relatedEntity;
		this.relatedEntityId = relatedEntityId;
	}
}
