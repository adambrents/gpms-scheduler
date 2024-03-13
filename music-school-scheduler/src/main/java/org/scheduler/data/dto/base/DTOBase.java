package org.scheduler.data.dto.base;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.scheduler.data.dto.interfaces.ISqlConvertible;
import org.scheduler.data.repository.base.BaseRepository;

import java.sql.SQLException;
import java.util.List;

public abstract class DTOBase<T extends ISqlConvertible> implements ISqlConvertible<T> {
    public String name;
    public int id;
    public BooleanProperty isSelected = new SimpleBooleanProperty();

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

    public <dto extends ISqlConvertible, repo extends BaseRepository> List<dto> getMappingsById(dto item, repo repository) throws SQLException, InstantiationException, IllegalAccessException {
        return repository.getAllItemsFromType(item.getClass());
    }
}
