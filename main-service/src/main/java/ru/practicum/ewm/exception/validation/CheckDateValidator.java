package ru.practicum.ewm.exception.validation;

import ru.practicum.ewm.event.dto.NewEventDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<CreatedValid, NewEventDto> {
    @Override
    public void initialize(CreatedValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(NewEventDto eventDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime dateTime = LocalDateTime.now().plusHours(2);
        LocalDateTime created = eventDto.getEventDate();
        if (created == null) {
            return false;
        }

        return created.isBefore(dateTime);
    }
}