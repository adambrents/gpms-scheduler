package org.scheduler.data.dto.properties;

public class PropertyDTO {
    private final String propertyTypeName;
    private final String propertyName;
    private int propertyId;

    public PropertyDTO(String propertyTypeName, String propertyName) {
        this.propertyTypeName = propertyTypeName;
        this.propertyName = propertyName;
    }

    public PropertyDTO(String propertyTypeName, String propertyName, int propertyId) {
        this.propertyTypeName = propertyTypeName;
        this.propertyName = propertyName;
        this.propertyId = propertyId;
    }
    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public int getPropertyId(){
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
