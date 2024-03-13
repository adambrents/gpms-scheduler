package org.scheduler.app.controller.handlers;

import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.data.dto.properties.InstrumentDTO;

import java.util.ArrayList;
import java.util.List;

public class InstrumentActionHandler implements IActionHandler<InstrumentDTO> {
    private final List<InstrumentDTO> instrumentList = new ArrayList<>();

    public List<InstrumentDTO> getInstrumentList() {
        return instrumentList;
    }

    @Override
    public void performConcreteListAction(boolean isAddAction, InstrumentDTO item) {
        if(isAddAction){
            instrumentList.add(item);
        }
        else{
            instrumentList.remove(item);
        }
    }
}
