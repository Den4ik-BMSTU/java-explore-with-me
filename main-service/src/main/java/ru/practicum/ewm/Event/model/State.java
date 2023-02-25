package ru.practicum.ewm.Event.model;

public enum State {
    PENDING,  // рассматриваемый
    PUBLISHED,  // опубликованный
    CANCELED,  // отмененный
    PUBLISH_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW,
    REJECT_EVENT,
    CONFIRMED,  // подтвержденный
    REJECTED  // отклоненный
}
