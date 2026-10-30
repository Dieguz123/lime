ALTER TABLE rooms ADD COLUMN name VARCHAR(50);

UPDATE rooms SET name = 'Room ' || invite_code;

ALTER TABLE rooms ALTER COLUMN name SET NOT NULL;
