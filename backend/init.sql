-- Drop and recreate database/user
DROP DATABASE IF EXISTS tnpsc_app;
DROP USER IF EXISTS tnpsc_user;

-- Create user
CREATE USER tnpsc_user WITH ENCRYPTED PASSWORD 'tnpsc_password';

-- Create database
CREATE DATABASE tnpsc_app OWNER tnpsc_user;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE tnpsc_app TO tnpsc_user;

-- Connect to the database and grant schema privileges
\c tnpsc_app
ALTER SCHEMA public OWNER TO tnpsc_user;
GRANT ALL PRIVILEGES ON SCHEMA public TO tnpsc_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO tnpsc_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO tnpsc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO tnpsc_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO tnpsc_user;
