package org.scheduler.app.controller.handlers;

import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.data.dto.properties.LevelDTO;

import java.util.ArrayList;
import java.util.List;

public class LevelActionHandler implements IActionHandler<LevelDTO> {
    private final List<LevelDTO> levelList = new ArrayList<>();

    public List<LevelDTO> getLevelList() {
        return levelList;
    }

    @Override
    public void performConcreteListAction(boolean isAddAction, LevelDTO item) {
        if(isAddAction){
            levelList.add(item);
        }
        else{
            levelList.remove(item);
        }
    }
}
