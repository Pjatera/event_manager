package ru.javacourse.eventmanagement.utill;

public interface Mapper<T1, T2, T3> {
    T1 mapToDto(T2 model);

    T2 mapFromDto(T1 dto);

    T2 mapFromEntity(T3 entity);

    T3 mapToEntity(T2 model);
}

