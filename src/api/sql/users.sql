-- name: get-users
-- Gets a list of all users with limit/offset
select *
from users
offset :offset
limit :limit

-- name: insert-user<!
-- Creates a user
insert into users (
  email,
  password
) values (
  :email,
  :password
)

-- name: delete-user<!
-- Deletes a user
delete from users
where id = :id

-- name: update-user-email<!
-- Updates a user's email
update users
set email = :email
where id = :id

-- name: update-user-password<!
-- Updates a user's password
update users
set password = coalesce(:password, password)
where id = :id

-- name: get-user
-- Gets a user by an id
select *
from users
where id = :id
offset 0
limit 1
