/**
 * @file DtoToEntityBuilder.java
 * @author Ivan Onufriienko
 */
package ija.project.dto;

import lombok.NonNull;

/**
 * Represents a data transfer object (DTO) for a Block.
 * This class is used to transfer the state of a Block without exposing the Block itself.
 * It encapsulates the position and dimensions of a Block.
 */
public interface DtoToEntityBuilder<T> {
    
    /**
     * Builds an entity from the DTO.
     *
     * @return The entity built from the DTO.
     */
    @NonNull
    T buildEntity();
}
