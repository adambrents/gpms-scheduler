package org.scheduler.data.dto.base;

import org.scheduler.app.constants.Constants;
import org.scheduler.data.dto.interfaces.ISqlConvertible;

public abstract class DTOMappingBase<T extends ISqlConvertible> extends DTOBase<T>{
    public int mappingFromId;
    public int mappingToId;

    public Constants.MAPPINGS mapping;

    public int getMappingFromId() {
        return mappingFromId;
    }

    public void setMappingFromId(int mappingFromId) {
        this.mappingFromId = mappingFromId;
    }

    public int getMappingToId() {
        return mappingToId;
    }

    public void setMappingToId(int mappingToId) {
        this.mappingToId = mappingToId;
    }

    public abstract Constants.MAPPINGS getMapping();

}
