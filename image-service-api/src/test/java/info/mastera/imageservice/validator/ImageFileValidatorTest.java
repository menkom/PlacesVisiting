package info.mastera.imageservice.validator;

import jakarta.validation.ConstraintValidatorContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;

class ImageFileValidatorTest {

    private final ImageFileValidator imageFileValidator =
            new ImageFileValidator(
                    List.of("image/png", "image/jpg", "image/jpeg", "image/webp"),
                    List.of("png", "jpg", "jpeg", "webp")
            );

    @Nested
    class IsValidTest {

        ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

        @BeforeEach
        void init() {
            var builder = Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
            Mockito.doReturn(builder).when(context).buildConstraintViolationWithTemplate(anyString());
        }

        @ParameterizedTest
        @CsvSource({
                "filename, image/png",
                "'', image/png",
                "image.jpg, text/plain",
                ", image/jpg",
        })
        void isValidFailedTest(String filename, String contentType) {
            MultipartFile uploadFile = new MockMultipartFile("name", filename, contentType, "".getBytes());

            boolean resultToTest = imageFileValidator.isValid(uploadFile, context);

            Assertions.assertThat(resultToTest).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
                "filename.png, image/png",
                ".jpg, image/png",
                "image.jpg, image/jpg",
                "image.name.jpeg, image/jpg",
                "image.name.jpeg, image/jpeg; UTF-8",
        })
        void isValidPassTest(String filename, String contentType) {
            MultipartFile uploadFile = new MockMultipartFile("name", filename, contentType, "".getBytes());

            boolean resultToTest = imageFileValidator.isValid(uploadFile, context);

            Assertions.assertThat(resultToTest).isTrue();
        }
    }

    @Nested
    class GetFileExtensionTest {

        @ParameterizedTest
        @ValueSource(strings = {
                "",
                " ",
                "name",
        })
        void getFileExtensionNotPresentTest(String filename) {

            Optional<String> resultToTest = imageFileValidator.getFileExtension(filename);

            Assertions.assertThat(resultToTest).isNotPresent();
        }

        @ParameterizedTest
        @CsvSource({
                "., ''",
                "name., ''",
                ".ext, ext",
                "name.ext, ext",
                "long.name.ext, ext",
        })
        void getFileExtensionPresentTest(String filename, String extension) {

            Optional<String> resultToTest = imageFileValidator.getFileExtension(filename);

            Assertions.assertThat(resultToTest).contains(extension);
        }
    }
}
