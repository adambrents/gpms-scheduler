package org.scheduler.app.controller.handlers.base;

import org.scheduler.data.dto.interfaces.ISqlConvertible;

public interface IActionHandler<T extends ISqlConvertible> {
    public void performConcreteListAction(boolean isAddAction, T item);
}
