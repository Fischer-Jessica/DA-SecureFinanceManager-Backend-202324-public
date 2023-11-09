CREATE DATABASE secure_finance_manager;

\c secure_finance_manager;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE colours (
	pk_colour_id SERIAL PRIMARY KEY,
	colour_name TEXT NOT NULL,
	colour_code bytea NOT NULL
);

CREATE TABLE users (
	pk_user_id SERIAL PRIMARY KEY,
	username TEXT UNIQUE NOT NULL,
	password bytea NOT NULL,
	email_address TEXT UNIQUE,
	first_name TEXT,
	last_name TEXT
);

CREATE TABLE categories (
	pk_category_id SERIAL PRIMARY KEY,
	category_name bytea NOT NULL,
	category_description bytea,
	fk_category_colour_id INT NOT NULL,
	fk_user_id INT NOT NULL,
	FOREIGN KEY (fk_user_id) REFERENCES users(pk_user_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_category_colour_id) REFERENCES colours(pk_colour_id)
);

CREATE TABLE subcategories (
	pk_subcategory_id SERIAL PRIMARY KEY,
	fk_category_id INT NOT NULL,
	subcategory_name bytea NOT NULL,
	subcategory_description bytea,
	fk_subcategory_colour_id INT NOT NULL,
	fk_user_id INT NOT NULL,
	FOREIGN KEY (fk_user_id) REFERENCES users(pk_user_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_category_id) REFERENCES categories(pk_category_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_subcategory_colour_id) REFERENCES colours(pk_colour_id)
);

CREATE TABLE labels (
	pk_label_id SERIAL PRIMARY KEY,
	label_name bytea NOT NULL,
	label_description bytea,
	fk_label_colour_id INT NOT NULL,
	fk_user_id INT NOT NULL,
	FOREIGN KEY (fk_user_id) REFERENCES users(pk_user_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_label_colour_id) REFERENCES colours(pk_colour_id)
);

CREATE TABLE entries (
	pk_entry_id SERIAL PRIMARY KEY,
	entry_name bytea,
	entry_description bytea,
	entry_amount bytea NOT NULL,
	entry_creation_time bytea NOT NULL,
	entry_time_of_transaction bytea NOT NULL,
	entry_attachment bytea,
	fk_subcategory_id INT NOT NULL,
	fk_user_id INT NOT NULL,
	FOREIGN KEY (fk_user_id) REFERENCES users(pk_user_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_subcategory_id) REFERENCES subcategories(pk_subcategory_id) ON DELETE CASCADE
);

CREATE TABLE entry_labels (
	pk_entry_label_id SERIAL PRIMARY KEY,
	fk_entry_id INT NOT NULL,
	fk_label_id INT NOT NULL,
	fk_user_id INT NOT NULL,
	FOREIGN KEY (fk_user_id) REFERENCES users(pk_user_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_entry_id) REFERENCES entries(pk_entry_id) ON DELETE CASCADE,
	FOREIGN KEY (fk_label_id) REFERENCES labels(pk_label_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX entry_labels_unique_idx ON entry_labels (fk_entry_id, fk_label_id, fk_user_id);