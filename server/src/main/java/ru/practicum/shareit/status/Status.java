package ru.practicum.shareit.status;

public enum Status {
    WAITING,    // новое бронирование, ожидает одобрения
    APPROVED,   // подтверждено владельцем
    REJECTED,   // отклонено владельцем
    CANCELED
}