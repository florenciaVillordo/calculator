DELETE
FROM tx_history;

INSERT INTO public.tx_history (id, create_date, result, tx_type)
VALUES (random_uuid(), '2022-02-06 21:07:36.645246', 0, 'LOGIN');
INSERT INTO public.tx_history (id, create_date, result, tx_type)
VALUES (random_uuid(), '2021-12-06 21:08:18.140772',1, 'SIGN_UP');
INSERT INTO public.tx_history (id, create_date, result, tx_type)
VALUES (random_uuid(), '2022-01-04 21:08:46.667390', 0, 'TRANSACTIONS');
INSERT INTO public.tx_history (id, create_date, result, tx_type)
VALUES (random_uuid(), '2022-02-06 21:07:36.645246', 1, 'LOGIN');