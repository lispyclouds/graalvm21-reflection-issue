public class ReflectionIssue {
    public static void main(String... args) throws NoSuchMethodException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        var clazz = Class.class;
        var meth = clazz.getMethod("forName", String.class);
        var stringClazz = meth.invoke(null, "java.lang.String");

        System.out.println(stringClazz);
    }
}
