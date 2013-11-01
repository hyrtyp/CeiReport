package com.poqop.document.events;

import java.lang.reflect.Method;

public abstract class SafeEvent<T> implements Event<T>
{
    private final Class<?> listenerType;

    protected SafeEvent()
    {
        listenerType = getListenerType();
    }

    private Class<?> getListenerType()
    {
        for (Method method : getClass().getMethods())
        {
            if ("dispatchSafely".equals(method.getName()) && !method.isSynthetic())//判断是否是“复合字段”
            {
                return method.getParameterTypes()[0];//返回一组class对象
            }
        }
        throw new RuntimeException("Couldn't find dispatchSafely method");
    }

    @SuppressWarnings({"unchecked"})
    public final void dispatchOn(Object listener)
    {
        if (listenerType.isAssignableFrom(listener.getClass()))//是用来判断一个类Class1和另一个类Class2是否相同或是另一个类的超类或接口。
        {
            dispatchSafely((T) listener);
        }
    }

    public abstract void dispatchSafely(T listener);
}
