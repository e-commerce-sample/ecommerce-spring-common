package com.ecommerce.common.utils;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

//Http request command of list as root element should be wrapped in this class in order to be validated by JSR-303
public class ListCommand<T> extends ArrayList<T> {
    @Valid
    public List<T> getList() {
        return this;
    }
}
