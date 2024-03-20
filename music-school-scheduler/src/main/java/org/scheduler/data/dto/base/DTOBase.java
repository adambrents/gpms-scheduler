package org.scheduler.data.dto.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.scheduler.data.dto.interfaces.IReportable;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.base.BaseRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public abstract class DTOBase<T extends ISqlConvertible> implements ISqlConvertible<T>, IReportable {
    //Polymorphism for name, id, isSelected, createdBy, updatedBy, createDate, updateDate
    //Encapsulated properties across all DTOs are private and set via setter or constructor
    //17 Inheritors of this class
    public String name;
    public int id;
    public BooleanProperty isSelected = new SimpleBooleanProperty();
    public int createdBy;
    public int updatedBy;
    public LocalDateTime createDate;
    public LocalDateTime updateDate;
    public void setSelected(boolean b) {
        isSelected.set(b);
    }

    public BooleanProperty getSelected() {
        return isSelected;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name){
        this.name = name;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public <dto extends ISqlConvertible, repo extends BaseRepository> List<dto> getMappingsById(dto item, repo repository) throws SQLException, InstantiationException, IllegalAccessException {
        return repository.getAllItemsFromType(item);
    }
}
