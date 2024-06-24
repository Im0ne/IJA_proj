/**
 * @file RoomDTOSerializer.java
 * @author Ivan Onufriienko
 */
package ija.project.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import ija.project.dto.RoomDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * Provides serialization and deserialization functionality for RoomDTO objects to and from JSON format.
 * This class utilizes Jackson's ObjectMapper to perform the conversion tasks, ensuring that RoomDTO
 * instances can be easily converted to JSON strings for storage or network transmission, and vice versa.
 * It implements the JsonSerializer interface, providing a standardized way to serialize and deserialize
 * specific types.
 */
@Slf4j
public class RoomDTOSerializer implements JsonSerializer<RoomDTO> {

    /**
     * The ObjectReader instance used to deserialize JSON strings into RoomDTO objects.
     */
    private final ObjectReader objectReader = new ObjectMapper().reader();

    /**
     * The ObjectWriter instance used to serialize RoomDTO objects into JSON strings.
     */
    private final ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    /**
     * Deserializes a JSON string into a RoomDTO object.
     * This method takes a JSON string representation of a RoomDTO and converts it back into a RoomDTO
     * object using Jackson's ObjectReader. It ensures that the JSON string is correctly interpreted
     * according to the RoomDTO class structure.
     *
     * @param json The JSON string to be deserialized into a RoomDTO object.
     * @return The deserialized RoomDTO object.
     * @throws IOException If an error occurs during the deserialization process.
     */
    @Override
    public RoomDTO deserilizeObject(@NonNull String json) throws IOException {
        return objectReader.readValue(json, RoomDTO.class);
    }
    
    /**
     * Serializes a RoomDTO object into a JSON string.
     * This method takes a RoomDTO object and converts it into a JSON string using Jackson's ObjectWriter.
     * It formats the JSON string with a default pretty printer to enhance readability.
     *
     * @param object The RoomDTO object to be serialized into a JSON string.
     * @return The serialized JSON string representation of the RoomDTO object.
     * @throws IOException If an error occurs during the serialization process.
     */
    @Override
    public String serializeObject(@NonNull RoomDTO object) throws IOException {
        return objectWriter.writeValueAsString(object);
    }
}