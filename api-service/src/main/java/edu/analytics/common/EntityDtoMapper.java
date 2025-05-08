package edu.analytics.common;

public interface EntityDtoMapper<E,D> {
    D toDto(E entity);
}
