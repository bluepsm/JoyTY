package github.bluepsm.joyty.models;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "files")
public class File {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private String name;
	
	private String type;
	
	@Lob
	private byte[] data;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User fileOwner;
	
//	@OneToOne(mappedBy = "profileImg")
//  private User profileImgOf;
	
	public File() {}
	
	public File(String name, String type, byte[] data) {
		this.name = name;
		this.type = type;
		this.data = data;
	}
}
