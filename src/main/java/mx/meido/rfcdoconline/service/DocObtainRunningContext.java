package mx.meido.rfcdoconline.service;

import lombok.Data;

import java.util.List;

@Data
public class DocObtainRunningContext {
    private List<String> currentTask;
}
