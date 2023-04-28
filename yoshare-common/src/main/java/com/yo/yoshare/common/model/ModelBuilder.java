package com.yo.yoshare.common.model;

import com.yo.yoshare.common.db.TableModelTemplate;
import org.springframework.beans.BeanUtils;

public abstract class ModelBuilder {

    public static <T> T buildModel(T model) {
        TableModelTemplate template = new TableModelTemplate();
        BeanUtils.copyProperties(template, model);
        return model;
    }

    public static <T> T buildModel(T model, Object params) {
        TableModelTemplate template = new TableModelTemplate();
        BeanUtils.copyProperties(template, model);
        BeanUtils.copyProperties(params, model);
        return model;
    }

    public static <T> T buildModel(T model, Object params, Class clazz) throws IllegalAccessException, InstantiationException {
        if (!TableModelTemplate.class.isAssignableFrom(clazz)) {
            throw new ClassCastException();
        }
        Object template = clazz.newInstance();
        BeanUtils.copyProperties(template, model);
        BeanUtils.copyProperties(params, model);
        return model;
    }


}
