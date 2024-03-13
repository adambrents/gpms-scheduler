package org.scheduler.app.controller.handlers;

import org.scheduler.app.controller.handlers.base.IActionHandler;
import org.scheduler.data.dto.properties.BookDTO;

import java.util.ArrayList;
import java.util.List;

public class BookActionHandler implements IActionHandler<BookDTO> {
    private final List<BookDTO> bookList = new ArrayList<>();

    public List<BookDTO> getBookList() {
        return bookList;
    }

    @Override
    public void performConcreteListAction(boolean isAddAction, BookDTO item) {
        if(isAddAction){
            bookList.add(item);
        }
        else{
            bookList.remove(item);
        }
    }
}
