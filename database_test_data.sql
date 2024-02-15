INSERT INTO colours (colour_name, colour_code)
VALUES
	('red', E'\\xFF0000'),
	('orange', E'\\xFF7F00'),
	('yellow', E'\\xFFFF00'),
	('green-yellow', E'\\x7FFF00'),
	('green', E'\\x00FF00'),
	('mint green', E'\\x00FF7F'),
	('turquoise', E'\\x00FFFF'),
	('light blue', E'\\x007FFF'),
	('blue', E'\\x0000FF'),
	('violet', E'\\x7F00FF'),
	('pink', E'\\xFF00FF'),
	('magenta', E'\\xFF007F');


INSERT INTO users (username, password, email_address, first_name, last_name)
VALUES
    ('Fischer', '$2a$10$gZCcERvdFyulJTLm2WKTsemD139mdSshwO807DgAWJGQBrOyr6tny', 'Jessica.Fischer.105@gmail.com', 'Jessica', 'Fischer'),
    ('Frontend', '$2a$10$lcApWghul6EYoPqU0Pn.Mup5aHcJEw7nBrr6vDlNEeaQ/0oAvIti.', NULL, NULL, 'Fischer'),
    ('Backend', '$2a$10$Z1wVjY64StnOZqRnd3ukm.NS31yuqZMzdzrt6UAy.9eTVJmtYbXlK', NULL, NULL, 'Fischer');


INSERT INTO categories (category_name, category_description, fk_category_colour_id, fk_user_id)
VALUES
    ('\xc30d040703021fb8f72a1ea658ff65d2380147ced7d10fc4e97f98255834713717a3fdc478fe4399e3a06dc83ce770e7248243ffef2f188d8fe1303648a698649061e91a380937944a', NULL, 1, 1),
    ('\xc30d0407030259272b80349e109f6ed23a01a9f812ae8886431bd2a2503c98734deec2b7dc9b3e8099165c444d1ddee868cccb9f2e7efd47694ab3a0e821ca63ec104010d65e148b11fbd7', NULL, 9, 1),
    ('\xc30d040703023b0ae213081ed6587bd23801fe8838551c012d0d9a6b5d33837218e5661276629b1a904fee319edbe8863c94033a68f2cf7faece779e69f6be11e0ac074f3ebbd08cf5', NULL, 10, 1),
    ('\xc30d04070302e5c7492b90baa6167ed2390166f4c8481f2cd42314be3c93740e0d7ecdaf8a0b83576043039ddfc8d4fdaed146f844a4eb487d266f182af9ccf8de054c6b62ab3c754618', NULL, 6, 1);


INSERT INTO subcategories (fk_category_id, subcategory_name, subcategory_description, fk_subcategory_colour_id, fk_user_id)
VALUES
    (1, '\xc30d040703027ec0e6949056197e75d23d0125e311252423aa1304e07199a96a3e06a4970650e5beb3d26887809aae90ba1e6f5398358b9166e7de472eee9d2cc32cdd01cd675c91cb8661ea4ff7', NULL, 8, 1),
    (2, '\xc30d0407030251e69011d4c8e6f373d23a0196cf46b585b298bf64ecec96fb916a48ad9e17c6831e34154716b9dd27128cb7ca3272e78e79da3a84be23f278a19cb8b515e9826957640dc6', NULL, 7, 1),
    (2, '\xc30d04070302ec2d9e156a055de97ad23a012c04afa3c628a95af26f78a8124c743fbb6a3ac3112fa378b588a01efa184db9b15fc4954329d00987918dfd4f49c13610fa723bf6dd850322', NULL, 9, 1),
    (3, '\xc30d04070302c6d5791a636766206ad23f018155fb8ad9e2d7731e73cb26ef0fd71da286323fc908430a8a434c1bf36e233d5f87289016900844f09c2afa5a57a5c437bb5d6537959b76e8d7c3bdc7f1', NULL, 11, 1),
    (3, '\xc30d040703021e35055c51f41ef86fd23b018258fa1f2095f6a71c6a717f64d1508d505e95ccf78024c96aa7fb19d52ed7a53ccb00addffdaf8c020c256cac38657fcfaa88b1defea67698f4', NULL, 12, 1),
    (4, '\xc30d04070302112efa74520e75ae7dd23f01b102527070baf573858dfa6e1cfe83652ddcc35e78022d963f247915be14dac3e92cddd5d45cf4c7f779c4b547001270b4ce1335db5b123b48e15f21e409', NULL, 1, 1);


INSERT INTO labels (label_name, label_description, fk_label_colour_id, fk_user_id)
VALUES
    ('\xc30d040703020b825c472c2d7c7f72d23e0190def4d3e741a9a753ce96eab0e08f16b997ae5303cf74c14e914718e36dd75a6cfa84bdc98620fe14c310d2c6d61b885e1792a493c59fa28f536c9486', NULL, 10, 1),
    ('\xc30d04070302f0a4ff9ef40a01a673d24d01c1cd4bd027d24d2cfde7b323586afa28302c3df7cbde1ee578f084b9274db8dc285dd58daf750c298ff3cfef4803da79f060f68aad7f76e2c0f66ddeb92a958a05849b0fd637d918b9a587e8', NULL, 1, 1);