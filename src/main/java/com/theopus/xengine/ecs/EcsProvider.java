package com.theopus.xengine.ecs;

import com.theopus.xengine.ecs.mapper.EntityManager;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.mapper.ViewEntityManager;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.inject.Provider;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class EcsProvider implements Provider {

    private final EntitySystemManager manager;

    public EcsProvider(EntitySystemManager manager) {
        this.manager = manager;
    }


    @Override
    public void provide(Object target, List<TaskComponent> container) throws IllegalAccessException {
        Class<?> targetClass = target.getClass();

        List<Field> fields = collectFields(targetClass);
//        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            boolean annotated = field.isAnnotationPresent(Ecs.class);
            if (annotated) {
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type.equals(TraitMapper.class)) {
                    Class<? extends Trait> traitClass = (Class<? extends Trait>) getGenericType(field);
                    TraitMapper<? extends Trait> readMapper = manager.getReadMapper(traitClass);
                    field.set(target, readMapper);
                    container.add(0, readMapper);
                } else if (type.equals(WriteTraitMapper.class)) {
                    Class<? extends Trait> traitClass = (Class<? extends Trait>) getGenericType(field);
                    WriteTraitMapper<? extends Trait> writeMapper = manager.getWriteMapper(traitClass);
                    field.set(target, writeMapper);
                    container.add(0, writeMapper);
                } else if (type.equals(EntityManager.class)) {
                    EntityManager entityManager = manager.getEntityManager();
                    field.set(target, entityManager);
                    container.add(0, entityManager);
                } else if (type.equals(ViewEntityManager.class)) {
                    ViewEntityManager entityManager = manager.getViewManager();
                    field.set(target, entityManager);
                    container.add(0, entityManager);
                }
            }
        }

    }

    private Class<?> getGenericType(Field field) {
        Type genericType = field.getGenericType();
        ParameterizedType aType = (ParameterizedType) genericType;
        Type[] parameterArgTypes = aType.getActualTypeArguments();
        return (Class) parameterArgTypes[0];
    }
}
