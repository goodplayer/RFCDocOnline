package mx.meido.rfcdoconline.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import mx.meido.rfcdoconline.entity.RfcDocEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"rfcContent"})
public class RfcDoc {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    @Getter
    @Setter
    private String rfcId;

    @Getter
    @Setter
    @NonNull
    boolean notFound;

    @Getter
    private List<String> updates = new ArrayList<String>();
    @Getter
    private List<String> obsoletes = new ArrayList<String>();

    @Getter
    @Setter
    private byte[] rfcContent;

    public RfcDocEntity toEntity() {
        RfcDocEntity entity = new RfcDocEntity();
        entity.setRfcId(this.rfcId);
        entity.setContent(this.rfcContent);
        try {
            entity.setObsoletes(objectMapper.writeValueAsBytes(this.obsoletes));
            entity.setUpdates(objectMapper.writeValueAsBytes(this.updates));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json error", e);
        }
        return entity;
    }

    public RfcDoc fromEntity(RfcDocEntity entity) {
        this.rfcId = entity.getRfcId();
        this.rfcContent = entity.getContent();

        try {
            if (entity.getUpdates() != null) {
                this.updates = objectMapper.readValue(entity.getUpdates(), new TypeReference<List<String>>() {
                });
            }
            if (entity.getObsoletes() != null) {
                this.obsoletes = objectMapper.readValue(entity.getObsoletes(), new TypeReference<List<String>>() {
                });
            }
        } catch (IOException e) {
            throw new RuntimeException("json error", e);
        }
        return this;
    }
}
