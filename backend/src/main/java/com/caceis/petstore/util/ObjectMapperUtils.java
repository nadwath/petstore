package com.caceis.petstore.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for object mapping operations using ObjectMapper
 */
@Component
public class ObjectMapperUtils {
    
    private static ObjectMapper objectMapper;

    @Autowired
    public ObjectMapperUtils(ObjectMapper objectMapper) {
        ObjectMapperUtils.objectMapper = objectMapper;
    }

    /**
     * Maps an object to another class type
     *
     * @param source      source object to map from
     * @param targetClass target class to map to
     * @param <T>        target type
     * @param <S>        source type
     * @return mapped object of target type
     */
    public static <T, S> T mapObject(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return objectMapper.convertValue(source, targetClass);
    }

    /**
     * Maps an object to generic class type
     *
     * @param source      source object to map from
     * @param targetClass target class to map to
     * @param <T>        target type
     * @param <S>        source type
     * @return mapped object of target type
     */
    public static <T, S> T mapObject(S source, TypeReference<T> targetClass) {
        if (source == null) {
            return null;
        }
        return objectMapper.convertValue(source, targetClass);
    }

    /**
     * Maps a list of objects to a list of another class type
     *
     * @param sourceList  source list to map from
     * @param targetClass target class to map to
     * @param <T>        target type
     * @param <S>        source type
     * @return list of mapped objects of target type
     */
    public static <T, S> List<T> mapList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        
        return sourceList.stream()
                .map(source -> mapObject(source, targetClass))
                .collect(Collectors.toList());
    }
}