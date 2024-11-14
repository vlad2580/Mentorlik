-- Создание основной таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (char_length(role) <= 50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS 'Main user table containing general user details';
COMMENT ON COLUMN users.email IS 'Unique email address for user login';
COMMENT ON COLUMN users.role IS 'User role, e.g., admin, mentor, student';

-- Таблица профилей администраторов
CREATE TABLE IF NOT EXISTS admin_profiles (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    access_level VARCHAR(50) NOT NULL CHECK (char_length(access_level) <= 50),
    role_description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE admin_profiles IS 'Admin-specific profile information';

-- Таблица профилей наставников
CREATE TABLE IF NOT EXISTS mentor_profiles (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    expertise VARCHAR(255) CHECK (char_length(expertise) <= 255),
    bio TEXT,
    experience_years INT CHECK (experience_years >= 0),
    certifications JSONB,
    is_available BOOLEAN DEFAULT TRUE,
    city VARCHAR(100),
    country VARCHAR(100),
    hourly_rate DECIMAL(10, 2),
    languages TEXT[],
    rating DECIMAL(3, 2) CHECK (rating BETWEEN 0 AND 5),
    review_count INT DEFAULT 0 CHECK (review_count >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE mentor_profiles IS 'Table containing mentor-specific profile information';
COMMENT ON COLUMN mentor_profiles.expertise IS 'Mentor expertise area, e.g., Software Development';

-- Индексы для оптимизации поиска
CREATE INDEX idx_mentor_expertise ON mentor_profiles(expertise);
CREATE INDEX idx_mentor_hourly_rate ON mentor_profiles(hourly_rate);
CREATE INDEX idx_mentor_rating ON mentor_profiles(rating);
CREATE INDEX idx_mentor_city ON mentor_profiles(city);
CREATE INDEX idx_mentor_country ON mentor_profiles(country);

-- Лог изменений для mentor_profiles
CREATE TABLE IF NOT EXISTS mentor_profiles_log (
    id SERIAL PRIMARY KEY,
    mentor_id INT REFERENCES mentor_profiles(id) ON DELETE CASCADE,
    action VARCHAR(10) NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data JSONB
);

COMMENT ON TABLE mentor_profiles_log IS 'Log table to track changes in mentor profiles';

-- Триггер и функция для логирования изменений
CREATE OR REPLACE FUNCTION log_mentor_changes() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO mentor_profiles_log (mentor_id, action, data)
        VALUES (NEW.id, 'INSERT', row_to_json(NEW));
        RETURN NEW;
    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO mentor_profiles_log (mentor_id, action, data)
        VALUES (NEW.id, 'UPDATE', row_to_json(NEW));
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO mentor_profiles_log (mentor_id, action, data)
        VALUES (OLD.id, 'DELETE', row_to_json(OLD));
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER mentor_profiles_changes
AFTER INSERT OR UPDATE OR DELETE ON mentor_profiles
FOR EACH ROW EXECUTE FUNCTION log_mentor_changes();

-- Таблица профилей студентов
CREATE TABLE IF NOT EXISTS student_profiles (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    field_of_study VARCHAR(255) CHECK (char_length(field_of_study) <= 255),
    education_level VARCHAR(50) CHECK (char_length(education_level) <= 50),
    learning_goals TEXT,
    skills TEXT[],
    is_available_for_mentorship BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE student_profiles IS 'Table containing student-specific profile information';
COMMENT ON COLUMN student_profiles.field_of_study IS 'Field of study of the student';

-- Таблица для хранения информации Google-пользователей
CREATE TABLE IF NOT EXISTS google_user_info (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    google_id VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255),
    profile_picture_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE google_user_info IS 'Stores Google-specific user information';

-- Таблица для хранения информации LinkedIn-пользователей
CREATE TABLE IF NOT EXISTS linkedin_user_info (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    linkedin_id VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    profile_picture_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE linkedin_user_info IS 'Stores LinkedIn-specific user information';