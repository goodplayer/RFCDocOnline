package mx.meido.rfcdoconline.service;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RfcMetadata {
    private List<String> obsoletes;
    private List<String> updates;

    public void standardize() {
        if (obsoletes != null) {
            this.obsoletes = obsoletes.stream().map(p -> p.toLowerCase().trim()).collect(Collectors.toList());
        }
        if (updates != null) {
            this.updates = updates.stream().map(p -> p.toLowerCase().trim()).collect(Collectors.toList());
        }
    }
}
