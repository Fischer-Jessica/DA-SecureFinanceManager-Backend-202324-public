-- Erstellen Sie den Benutzer "backend" mit einem sicheren Passwort
CREATE ROLE backend WITH PASSWORD 'SECURE_PASSWORD' LOGIN;

-- Verbindung zur Datenbank gewähren
GRANT CONNECT ON DATABASE secure_finance_manager TO backend;

-- Berechtigungen für Tabellen erteilen
GRANT SELECT ON public.colours TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE ON public.users TO backend;

GRANT USAGE ON public.users_pk_user_id_seq TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON public.categories TO backend;

GRANT USAGE ON public.categories_pk_category_id_seq TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON public.subcategories TO backend;

GRANT USAGE ON public.subcategories_pk_subcategory_id_seq TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON public.entries TO backend;

GRANT USAGE ON public.entries_pk_entry_id_seq TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON public.labels TO backend;

GRANT USAGE ON public.labels_pk_label_id_seq TO backend;

GRANT SELECT, INSERT, UPDATE, DELETE, REFERENCES ON public.entry_labels TO backend;

GRANT USAGE ON public.entry_labels_pk_entry_label_id_seq TO backend;