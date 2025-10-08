-- Update admin user password to correct bcrypt hash
UPDATE users
SET password_hash = '$2a$10$N9qo8uLOickgx2ZMRZoMye5RJwmdN8v/3L6UtYEMGfalTDxoz./Mi'
WHERE username = 'admin';
