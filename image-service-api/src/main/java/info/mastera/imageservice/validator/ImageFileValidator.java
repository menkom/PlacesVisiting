package info.mastera.imageservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public class ImageFileValidator implements ConstraintValidator<ValidImageType, MultipartFile> {

    private static final String MESSAGE_TEMPLATE = "Only PNG or JPG images are allowed.";

    private final List<String> acceptedContentTypes;
    private final List<String> acceptedFileExtensions;

    private String message;

    public ImageFileValidator(
            @Value("${images.accepted-content-types}") List<String> acceptedContentTypes,
            @Value("${images.accepted-file-extensions}") List<String> acceptedFileExtensions
    ) {
        this.acceptedContentTypes = acceptedContentTypes;
        this.acceptedFileExtensions = acceptedFileExtensions;
    }

    @Override
    public void initialize(ValidImageType constraintAnnotation) {
        // Extract any value you would need from the annotation
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String contentType = multipartFile.getContentType();
        boolean isFileExtensionCorrect = getFileExtension(multipartFile.getOriginalFilename())
                .map(acceptedFileExtensions::contains)
                .orElse(false);
        if (contentType == null
                || !isAcceptedContentType(contentType)
                || !isFileExtensionCorrect) {
            context.disableDefaultConstraintViolation();
            context.getDefaultConstraintMessageTemplate();
            context.buildConstraintViolationWithTemplate(
                            message == null || message.isEmpty()
                                    ? MESSAGE_TEMPLATE
                                    : message)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    protected Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(name -> name.lastIndexOf(".") != -1)
                .map(name -> name.substring(name.lastIndexOf(".") + 1));
    }

    protected boolean isAcceptedContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return acceptedContentTypes.contains(contentType.split(";")[0]);
    }
}
