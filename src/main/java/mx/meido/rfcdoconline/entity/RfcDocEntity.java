package mx.meido.rfcdoconline.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "rfc_doc")
public class RfcDocEntity {

    @Id
    private String rfcId;

    @Lob
    private byte[] content;

    @Lob
    private byte[] updates;

    @Lob
    private byte[] obsoletes;

    private Long time_created;
    private Long time_updated;

    @PrePersist
    public void onPersist() {
        this.time_created = System.currentTimeMillis();
    }

    @PreUpdate
    public void onUpdate() {
        this.time_updated = System.currentTimeMillis();
    }
}
