INSERT INTO colours (colour_name, colour_code)
VALUES
    ('Red', E'\\xFF0000'),
    ('Green', E'\\x00FF00'),
    ('Blue', E'\\x0000FF');


INSERT INTO users (username, password, email_address, first_name, last_name)
VALUES
    ('user1', E'\\x736563726574' /* 'secret' */, 'user1@example.com', 'John', 'Doe'),
    ('user2', E'\\x70617373776F7264' /* 'password' */, 'user2@example.com', 'Jane', 'Smith');


INSERT INTO categories (category_name, category_description, fk_category_colour_id, fk_user_id)
VALUES
    (E'\\x486F6D65', E'\\x546573742063617465676F7279' /* 'Home', 'Test category' */, 1, 1),
    (E'\\x576F726B', E'\\x546573742063617465676F7279' /* 'Work', 'Test category' */, 2, 1);


INSERT INTO subcategories (fk_category_id, subcategory_name, subcategory_description, fk_subcategory_colour_id, fk_user_id)
VALUES
    (1, E'\\x4C6976696E67436F6F6C', E'\\x546573742073756263617465676F7279' /* 'LivingCool', 'Test subcategory' */, 1, 1),
    (2, E'\\x436F6D7075746572436F6F6C', E'\\x546573742073756263617465676F7279' /* 'ComputerCool', 'Test subcategory' */, 2, 1);


INSERT INTO labels (label_name, label_description, fk_label_colour_id, fk_user_id)
VALUES
    (E'\\x4C6162656C' /* 'Label' */, E'\\x54657374206C6162656C' /* 'Test label' */, 1, 1),
    (E'\\x4E6F746573' /* 'Notes' */, E'\\x54657374206C6162656C' /* 'Test label' */, 2, 1);


INSERT INTO entries (entry_name, entry_description, entry_amount, entry_creation_time, entry_time_of_expense, entry_attachment, fk_subcategory_id, fk_user_id)
VALUES
    (E'\\x456E74727931' /* 'Entry1' */, E'\\x54657374206465736372697074696F6E' /* 'Test description' */, E'\\x313030' /* '100' */, E'\\x323031323031323331303030' /* '2021-07-11 10:00:00' */, E'\\x323031323031323331303030' /* '2021-07-11 10:00:00' */, NULL, 1, 1),
    (E'\\x456E74727932' /* 'Entry2' */, E'\\x54657374206465736372697074696F6E' /* 'Test description' */, E'\\x323030' /* '200' */, E'\\x323031323031323331303030' /* '2021-07-11 10:00:00' */, E'\\x323031323031323331303030' /* '2021-07-11 10:00:00' */, NULL, 2, 1);


INSERT INTO entry_labels (fk_entry_id, fk_label_id, fk_user_id)
VALUES
    (1, 1, 1),
    (2, 2, 1);