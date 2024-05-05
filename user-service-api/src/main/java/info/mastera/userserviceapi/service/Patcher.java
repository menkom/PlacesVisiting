package info.mastera.userserviceapi.service;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class Patcher {

    public <T> void patch(T existing, T incomplete) throws IllegalAccessException {

        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> clazz = existing.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value = field.get(incomplete);
            if (value != null) {
                field.set(existing, value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
    }
}
