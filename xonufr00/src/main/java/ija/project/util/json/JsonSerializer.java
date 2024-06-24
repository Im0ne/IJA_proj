/**
 * @file JsonSerializer.java
 * @author Ivan Onufriienko
 */
package ija.project.util.json;

import lombok.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a JSON serializer.
 * This interface is implemented by classes that provide JSON serialization and deserialization functionality.
 */
public interface JsonSerializer<T> {
    /**
     * Deserialize json to object
     * @param json - the json string
     * @return deserialized object
     * @throws IOException - deserialization failed
     */
    T deserilizeObject(@NonNull String json) throws IOException;

    /**
     * Serialized object to pretty json string
     * @param object - object to serialize
     * @return serialized object as json string
     * @throws IOException - serialization failed
     */
    String serializeObject(@NonNull T object) throws IOException;
}
