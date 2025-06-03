-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

-- Таблица вещей
CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    is_available BOOLEAN NOT NULL,
    owner_id INTEGER REFERENCES users(id),
    request_id INTEGER
);

-- Таблица запросов (если используется)
CREATE TABLE IF NOT EXISTS requests (
    id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    requestor_id INTEGER NOT NULL REFERENCES users(id)
);

-- Таблица бронирований
CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id INTEGER NOT NULL REFERENCES items(id),
    booker_id INTEGER NOT NULL REFERENCES users(id),
    status VARCHAR(20) NOT NULL
);

-- Таблица комментариев
CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    item_id INTEGER NOT NULL REFERENCES items(id),
    author_id INTEGER NOT NULL REFERENCES users(id),
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
