INSERT INTO public.role(id, `type`) VALUES (random_uuid(), 0);

INSERT INTO public."user"(id, create_date, update_date, username, password) VALUES (random_uuid(), now(), now(), 'name', '$2a$10$doeH46uAyzDlWFgEq7H4ve0DoyeUlpBUuHCXznOW15ZdBefJuERHK');