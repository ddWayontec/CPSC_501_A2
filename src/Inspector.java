import java.lang.reflect.*;

public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        if (c == Object.class) {
            return;
        }

        superclassInfo(c, obj, recursive, depth);
        interfaceInfo(c, obj, recursive, depth);
        constructorInfo(c, depth);
        methodInfo(c, depth);
        fieldInfo(c, obj, recursive, depth);
    }



    private void printFormatted(String line, int depth) {
        String tabs = "";
        for (int i=0; i < depth; i++) {
            tabs += "\t";
        }

        System.out.println(tabs + line);
    }

    private void superclassInfo(Class c, Object obj, boolean recursive, int depth) {
        Class superClass = c.getSuperclass();
        if (superClass != null) {
            printFormatted("Superclass name: " + superClass.getName(), depth);
            inspectClass(superClass, obj, recursive, depth+1);
        }
        else {
            printFormatted("No superclass exists", depth);
        }
    }

    private void interfaceInfo(Class c, Object obj, boolean recursive, int depth) {
        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> interfce : interfaces) {
            printFormatted("Interface name: " + interfce.getSimpleName(), depth);
            inspectClass(interfce, obj, recursive, depth+1);
        }
    }

    private void constructorInfo(Class c, int depth) {
        Constructor<?>[] constructors = c.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            printFormatted("Constructor name: " + constructor.getName(), depth);
            Class<?>[] constructorParamTypes = constructor.getParameterTypes();
            String parametertypes = "(";
            for (Class<?> constructorParamtype : constructorParamTypes) {
                parametertypes += constructorParamtype.getTypeName() + ",";
            }
            parametertypes = parametertypes.substring(0, parametertypes.length()-1) + ")";
            printFormatted("Constructor parameter types: " + parametertypes, depth);
            printFormatted("Constructor modifiers: " + Modifier.toString(constructor.getModifiers()), depth);
        }
    }

    private void methodInfo(Class c, int depth) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            printFormatted("Method name: " + method.getName(), depth);
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            String exceptionsThrown = "";
            for (Class<?> exception : exceptionTypes) {
                exceptionsThrown += exception.getSimpleName() + ", ";
            }
            if (exceptionsThrown.length() >= 2) {
                exceptionsThrown = exceptionsThrown.substring(0, exceptionsThrown.length()-2);
            }
            printFormatted("Exceptions method throws: " + exceptionsThrown, depth);

            String parametertypes = "(";
            Class<?>[] methodParamTypes = method.getParameterTypes();
            for (Class<?> methodParamtype : methodParamTypes) {
                parametertypes += methodParamtype.getTypeName() + ",";
            }
            parametertypes = parametertypes.substring(0, parametertypes.length()-1) + ")";
            printFormatted("Method parameter types: " + parametertypes, depth);

            printFormatted("Method return type: " + method.getReturnType().getSimpleName(), depth);

            printFormatted("Method modifiers: " + Modifier.toString(method.getModifiers()), depth);
        }
    }

    private void fieldInfo(Class c, Object obj, boolean recursive, int depth) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            printFormatted("Field name: " + field.getName(), depth);
            printFormatted("Field type: " + field.getType().getSimpleName(), depth);
            printFormatted("Field modifiers: " + Modifier.toString(field.getModifiers()), depth);

            Object object = null;
            try {
                object = field.get(obj);
            }
            catch (IllegalAccessException e) {
                printFormatted("Can not access field object", depth);
            }
            if (object == null) {
                printFormatted("Object is null" , depth);
            }
            else if (object.getClass().isArray()) {
                handleArray(object.getClass(), object, recursive, depth);
            }
            else if (!object.getClass().isPrimitive() && !recursive) {
                printFormatted("Reference value: class " + obj.getClass().getSimpleName() + " with identity has code " + obj.hashCode(), depth);

            }
            else {
                printFormatted("Object value: " + object, depth);
            }
            //cant recurse into field, this causes stack overflow, not sure why
//            if (recursive && object != null && !object.getClass().isArray()) {
//                inspectClass(object.getClass(), object, recursive, depth+1);
//            }


        }
    }

    private void handleArray(Class c, Object obj, boolean recursive, int depth) {
        printFormatted("Field is array", depth);
        printFormatted("Array component type: " + c.getComponentType().getTypeName(), depth);
        printFormatted("Array length: " + Array.getLength(obj), depth);
        String arrayContent = "[";
        for (int i=0; i<Array.getLength(obj); i++) {
            Object object = Array.get(obj, i);
            if (object != null) {
                arrayContent += object.getClass().getSimpleName() + ",";
            }
            else {
                arrayContent += "null,";
            }
        }
        arrayContent = arrayContent.substring(0, arrayContent.length()-1) + "]";
        printFormatted("Array content: " + arrayContent, depth);
        if (recursive == true) {
            for (int i=0; i<Array.getLength(obj); i++) {
                Object object = Array.get(obj, i);
                if (object != null) {
                    inspectClass(object.getClass(), object, recursive, depth+1);
                }
            }
        }
    }




}

