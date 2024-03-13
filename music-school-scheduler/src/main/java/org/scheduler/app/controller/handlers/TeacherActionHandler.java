package org.scheduler.app.controller.handlers;

import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.data.dto.TeacherDTO;

import java.util.ArrayList;
import java.util.List;

public class TeacherActionHandler implements IActionHandler<TeacherDTO> {
    private final List<TeacherDTO> teacherList = new ArrayList<>();

    public List<TeacherDTO> getTeacherList() {
        return teacherList;
    }

    @Override
    public void performConcreteListAction(boolean isAddAction, TeacherDTO item) {
        if(isAddAction){
            teacherList.add(item);
        }
        else{
            teacherList.remove(item);
        }
    }
}
