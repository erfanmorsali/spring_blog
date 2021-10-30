package com.example.blog.modules.posts.validators;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImageFormatValidator implements ConstraintValidator<ImageFormatConstraint, MultipartFile> {

    @Override
    public void initialize(ImageFormatConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        List<String> validFormats = new ArrayList<>(2);
        validFormats.add("jpg");
        validFormats.add("jpeg");
        return validFormats.contains(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1]);
    }
}
