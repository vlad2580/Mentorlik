BEGIN;

INSERT INTO mentor_profiles (user_id, expertise, bio, experience_years, certifications, is_available, city, country, hourly_rate, languages, rating, review_count)
VALUES
    ((SELECT id FROM users WHERE email = 'mentor2@example.com'), 'Data Science', 'Data scientist with expertise in machine learning', 8, '[{"certification": "Google Data Scientist"}, {"certification": "Python ML Expert"}]', TRUE, 'Berlin', 'Germany', 70.00, '{"English", "German"}', 4.7, 200),
    ((SELECT id FROM users WHERE email = 'mentor3@example.com'), 'Cybersecurity', 'Specialist in network security and ethical hacking', 5, '[{"certification": "Certified Ethical Hacker"}]', FALSE, 'Warsaw', 'Poland', 60.00, '{"English", "Polish"}', 4.5, 150),
    ((SELECT id FROM users WHERE email = 'mentor4@example.com'), 'UI/UX Design', 'Senior designer with 15 years of experience', 15, '[{"certification": "Adobe Certified Expert"}]', TRUE, 'Vienna', 'Austria', 90.00, '{"English", "German"}', 4.9, 300);

COMMIT;