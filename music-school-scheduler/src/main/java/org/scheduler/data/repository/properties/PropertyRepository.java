package org.scheduler.data.repository.properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.scheduler.app.constants.Constants;
import org.scheduler.data.dto.properties.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

import static org.scheduler.app.constants.Constants.PROPERTIES.*;

public class PropertyRepository {
    private final Logger logger = LoggerFactory.getLogger(PropertyRepository.class);
    private final BookRepository bookRepository = new BookRepository();
    private final InstrumentRepository instrumentRepository = new InstrumentRepository();
    private final LevelRepository levelRepository = new LevelRepository();

    public ObservableList<PropertyDTO> getAllProperties() {
        ObservableList<PropertyDTO> propertyDTOS = FXCollections.observableArrayList();
        try {
            propertyDTOS.addAll(fetchProperties(bookRepository.getAllItems(), BOOK, BookDTO::name, BookDTO::bookId));
            propertyDTOS.addAll(fetchProperties(instrumentRepository.getAllItems(), INSTRUMENT, InstrumentDTO::instrumentName, InstrumentDTO::instrumentId));
            propertyDTOS.addAll(fetchProperties(levelRepository.getAllItems(), LEVEL, LevelDTO::name, LevelDTO::levelId));
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            logger.error("Error fetching properties", e);
        }
        return propertyDTOS;
    }

    private <T> ObservableList<PropertyDTO> fetchProperties(ObservableList<T> items, String propertyType,
                                                            PropertyNameExtractor<T> nameExtractor, PropertyIdExtractor<T> idExtractor) {
        ObservableList<PropertyDTO> properties = FXCollections.observableArrayList();
        for (T item : items) {
            properties.add(new PropertyDTO(propertyType, nameExtractor.extractName(item), idExtractor.extractId(item)));
        }
        return properties;
    }

    public void performPropertyAction(PropertyDTO propertyDTO, Constants.CRUD action) throws SQLException {
        switch (propertyDTO.getPropertyTypeName()) {
            case BOOK:
                bookRepository.performAction(new BookDTO(propertyDTO.getPropertyId(), propertyDTO.getPropertyName()), action);
                break;
            case INSTRUMENT:
                instrumentRepository.performAction(new InstrumentDTO(propertyDTO.getPropertyId(), propertyDTO.getPropertyName()), action);
                break;
            case LEVEL:
                levelRepository.performAction(new LevelDTO(propertyDTO.getPropertyId(), propertyDTO.getPropertyName()), action);
                break;
            default:
                logger.error("Unsupported property type: " + propertyDTO.getPropertyTypeName());
                throw new SQLException("Unsupported property type: " + propertyDTO.getPropertyTypeName());
        }
    }

    @FunctionalInterface
    interface PropertyNameExtractor<T> {
        String extractName(T item);
    }

    @FunctionalInterface
    interface PropertyIdExtractor<T> {
        int extractId(T item);
    }
}
