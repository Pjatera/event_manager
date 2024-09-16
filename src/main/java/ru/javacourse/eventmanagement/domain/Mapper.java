package ru.javacourse.eventmanagement.domain;

/**
 * This interface provides methods for mapping between different types of objects.
 *
 * @param <T1> The type of the DTO.
 * @param <T2> The type of the model object from the service.
 * @param <T3> The type of the entity object from the database.
 */
public interface Mapper<T1, T2, T3> {

    /**
     * Maps a model object to a DTO object.
     *
     * @param model The model object to be mapped.
     * @return The DTO object resulting from the mapping.
     */
    T1 mapToDto(T2 model);

    /**
     * Maps a DTO object to a model object.
     *
     * @param dto The DTO object to be mapped.
     * @return The model object resulting from the mapping.
     */
    T2 mapFromDto(T1 dto);

    /**
     * Maps an entity object to a model object.
     *
     * @param entity The entity object to be mapped.
     * @return The model object resulting from the mapping.
     */
    T2 mapFromEntity(T3 entity);

    /**
     * Maps a model object to an entity object.
     *
     * @param model The model object to be mapped.
     * @return The entity object resulting from the mapping.
     */
    T3 mapToEntity(T2 model);
}

