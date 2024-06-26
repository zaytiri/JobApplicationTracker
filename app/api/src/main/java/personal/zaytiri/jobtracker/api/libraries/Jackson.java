package personal.zaytiri.jobtracker.api.libraries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Jackson {
    public <T> String fromListToJson(List<T> list) {
        String out = "";

        getMapper().getFactory()
                .setStreamWriteConstraints(StreamWriteConstraints.builder().maxNestingDepth(Integer.MAX_VALUE).build());

        try {
            out = getMapper().writeValueAsString(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out;
    }

    public ObjectMapper getMapper(){
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
    }

    public <T> T fromJsonToObject(String json, T obj){
        T mappedObject;
        try {
            mappedObject = getMapper().readerForUpdating(obj).readValue(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return mappedObject;
    }

    public <T> String fromObjectToJson(T obj){
        try {
            return getMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
