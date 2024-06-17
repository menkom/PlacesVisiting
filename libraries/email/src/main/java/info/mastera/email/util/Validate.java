package info.mastera.email.util;

/**
 * Helpful util methods to validate
 */
public class Validate {

    private Validate() {

    }

    /**
     * Returns {@link true} if object {@code str} is {@link null} or String is empty (equals "")
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    /**
     * check that obj is not null
     * Throws {@link IllegalArgumentException} if object is null
     */
    public static void notNull(Object obj, String errMsg) {
        if (obj == null) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * Can be used instead of Assert to check that obj (possibly {@link String}) is not empty
     * Throws {@link IllegalArgumentException} if object {@code isEmpty()}
     */
    public static void notEmpty(String str, String errMsg) {
        if (isEmpty(str)) {
            throw new IllegalArgumentException(errMsg);
        }
    }

    /**
     * Can be used instead of Assert to check that condition is correct otherwise throw an exception
     * Throws {@link IllegalArgumentException} if object {@code isEmpty()}
     */
    public static void isValid(boolean condition, String errMsg) {
        if (!condition) {
            throw new IllegalArgumentException(errMsg);
        }
    }
}