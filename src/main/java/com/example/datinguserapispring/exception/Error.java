package com.example.datinguserapispring.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

/**
 * Error contains: CODE a unique code which value's first 3 numbers represent the response status
 * code. HTTP STATUS the response status code MESSAGE the error message
 */
@Getter
@RequiredArgsConstructor
public enum Error {
    FIREBASE_ERROR(4003, BAD_REQUEST, "FireBase Error"),
    IMAGE_IS_REQUIRED(4004, BAD_REQUEST, "No Image Was Passed With Request"),
    INVALID_ROLE(4007, BAD_REQUEST, "Can't Parse String Value To Role Enum"),
    INVALID_STATUS(4008, BAD_REQUEST, "Can't Parse String Value To Status Enum"),
    INVALID_FIELD(4009, BAD_REQUEST, "Invalid Field In Fetch Request"),
    TOKEN_EXPIRED(40012, BAD_REQUEST, "Token Expired"),
    REFRESH_TOKEN_INVALID(40013, BAD_REQUEST, "Invalid Refresh Token"),

    BAD_CREDENTIALS(4011, UNAUTHORIZED, "Bad Credentials"),
    APPLE_AUTH_FAILED(4012, UNAUTHORIZED, "Apple authentication failed"),

    ACCESS_DENIED(4031, FORBIDDEN, "Permission Denied To Requested Resource"),

    BLOCKED_USER(4032, FORBIDDEN, "You are blocked"),


    CONFIRM_TOKEN_NOT_FOUND(4041, NOT_FOUND, "Confirmation Token Not Found"),

    USER_NOT_FOUND(4042, NOT_FOUND, "There Is No User With Such Id"),

    IMAGE_NOT_FOUND(4045, NOT_FOUND, "There Is No Image For The Given User"),

    BOT2_NOT_FOUND(4046, NOT_FOUND, "There Is No Bot2 With Such Id"),

    ENTITY_NOT_FOUND(4047, NOT_FOUND, "There Is No Entity With Such Id"),


    ADMIN_ALREADY_EXIST(4092, CONFLICT, "There Is a User Registered"),

    INVALID_IMAGE_FORMAT(4121, PRECONDITION_FAILED, "Cannot Upload The Image"),

    INVALID_IMAGE_SIZE(
            4122, PRECONDITION_FAILED, "Image Has Smaller Size Than It's Required (600x600)"),

    INVALID_FILE_SIZE(4123, PRECONDITION_FAILED, "File Size Should Be Between 70KB and 30MB"),

    INVALID_IMAGE_EXTENSION(
            4124, PRECONDITION_FAILED, "The Extension Of The Image Should Be Either 'jpg/jpeg' or 'png'"),

    FAILED_MULTIPART_CONVERTING(
            5002, INTERNAL_SERVER_ERROR, "Error Occurred While Converting Multipart File To File"),

    FAILED_IMAGE_RESIZING(5003, INTERNAL_SERVER_ERROR, "Error Occurred While Resizing The Image"),

    FAILED_IMAGE_CONVERTING(
            5005, INTERNAL_SERVER_ERROR, "Error Occurred While Processing Downloaded Image"),

    USER_LIKE_LIMIT(4091, CONFLICT, "User has reached likes limit"),

    FAILED_JSON_CONVERTING(5007, INTERNAL_SERVER_ERROR, "Error Occurred While Parsing To Json"),

    INTERRUPTED_FILE_READ(5007, INTERNAL_SERVER_ERROR, "Error Occurred While reading the file"),

    FAILED_READ_FROM_JSON(50010, INTERNAL_SERVER_ERROR, "Error Occurred While Reading From Json"),

    FAILED_KEY_READ(5008, INTERNAL_SERVER_ERROR, "Error Occurred While Retrieving Security Keys"),

    FAILED_FILE_DELETION(5009, INTERNAL_SERVER_ERROR, "Error Occurred While Deleting The File"),

    FAILED_UPLOAD_PHOTO(50011, INTERNAL_SERVER_ERROR, "Error Occurred While upload photo");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;
}