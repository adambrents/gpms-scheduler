package org.scheduler.data.dto.factory;

import javafx.beans.property.BooleanProperty;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.dto.base.DTOMappingBase;
import org.scheduler.data.dto.mapping.StudentBookDTO;
import org.scheduler.data.dto.mapping.StudentInstrumentDTO;
import org.scheduler.data.dto.mapping.StudentLevelDTO;
import org.scheduler.data.dto.mapping.StudentTeacherDTO;

public class DTOFactory {
    public static DTOMappingBase<?> createMappingDTO(Constants.MAPPINGS type, int mapFromProperty, int mapToProperty, BooleanProperty selected) {
        switch (type) {
            case StudentLevel:
                return new StudentLevelDTO(mapFromProperty, mapToProperty, selected);
            case StudentTeacher:
                return new StudentTeacherDTO(mapFromProperty, mapToProperty, selected);
            case StudentBook:
                return new StudentBookDTO(mapFromProperty, mapToProperty, selected);
            case StudentInstrument:
                return new StudentInstrumentDTO(mapFromProperty, mapToProperty, selected);
            default:
                throw new IllegalArgumentException("Unknown DTO type: " + type);
        }
    }
}

