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

        //2. print immediate superclass name
        Class superClass = c.getSuperclass();
        if (superClass != null) {
            printFormatted("Superclass name: " + superClass.getName(), depth);
            //2.a. explore superclass
            exploreClass(superClass, obj, recursive, depth);
        }
        else {
            printFormatted("No superclass exists", depth);
        }


        //3. print name of interface class implements
        Class<?>[] interfaces = c.getInterfaces();
        for (Class<?> interfce : interfaces) {
            printFormatted("Interface name: " + interfce.getSimpleName(), depth);
            //3.a explore interface superclass
            exploreClass(interfce, obj, recursive, depth);
        }

        //4. constructor info
        Constructor<?>[] constructors = c.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            printFormatted("Constructor name: " + constructor.getName(), depth);
            Class<?>[] constructorParamTypes = constructor.getParameterTypes();
            String parametertypes = "(";
            for (Class<?> constructorParamtype : constructorParamTypes) {
                parametertypes += constructorParamtype.getTypeName() + ",";
            }
            parametertypes += ")";
            printFormatted("Constructor parameter types: " + parametertypes, depth);
            printFormatted("Constructor modifiers: " + Modifier.toString(constructor.getModifiers()), depth);
        }

        //5. method info
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            printFormatted("Method name: " + method.getName(), depth);
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            String exceptionsThrown = "";
            for (Class<?> exception : exceptionTypes) {
                exceptionsThrown += exception.getTypeName() + ", ";
            }
            printFormatted("Exceptions method throws: " + exceptionsThrown, depth);

            String parametertypes = "(";
            Class<?>[] methodParamTypes = method.getParameterTypes();
            for (Class<?> methodParamtype : methodParamTypes) {
                parametertypes += methodParamtype.getTypeName() + ",";
            }
            parametertypes += ")";
            printFormatted("Constructor parameter types: " + parametertypes, depth);

            printFormatted("Method return type: " + method.getReturnType().getSimpleName(), depth);

            printFormatted("Method modifiers: " + Modifier.toString(method.getModifiers()), depth);
        }

        //6. fields
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            //field.setAccessible(true);
            printFormatted("Field name: " + field.getName(), depth);
            printFormatted("Field type: " + field.getType().getSimpleName(), depth);
            printFormatted("Field modifiers: " + Modifier.toString(field.getModifiers()), depth);
        }


        if (c.isArray()) {
            System.out.println(obj.getClass().getName());
            System.out.println(c.getComponentType().getTypeName());
            System.out.println(Array.getLength(obj));
            String arrayContent = "[";
            for (int i=0; i<Array.getLength(obj); i++) {
                if (Array.get(obj, i) != null) {
                    arrayContent += Array.get(obj, i).getClass().getSimpleName() + ",";
                }
                else {
                    arrayContent += "null,";
                }
            }
            arrayContent += "]";
            printFormatted(arrayContent, depth);
            if (recursive == true) {
                for (int i=0; i<Array.getLength(obj); i++) {
                    exploreArray(obj, depth);
                }
            }

        }
    }


    private void exploreArray(Object obj, int depth) {

    }

    private void exploreClass(Class c, Object obj, boolean recursive, int depth) {
        inspectClass(c, obj, recursive, depth+1);
    }


    private void printFormatted(String line, int depth) {
        String tabs = "";
        for (int i=0; i < depth; i++) {
            tabs += "\t";
        }

        System.out.println(tabs + line);
    }




}