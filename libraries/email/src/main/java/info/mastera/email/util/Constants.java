package info.mastera.email.util;

import java.util.regex.Pattern;

public class Constants {

    private Constants() {

    }

    public static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
}
