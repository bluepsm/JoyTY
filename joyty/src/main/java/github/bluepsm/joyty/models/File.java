package github.bluepsm.joyty.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files")
@EntityListeners(AuditingEntityListener.class)
public class File {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private String name;
	
	private String type;
	
	@Lob
	private byte[] data;
	
	@Column(name = "uploadedAt")
    @CreatedDate
    private Long uploadedAt;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User fileOwner;
	
	public File() {}
	
	public File(String name, String type, byte[] data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}
}
