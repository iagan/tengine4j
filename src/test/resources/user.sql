SELECT 
  a.NAME,
  (a.age + 1) AS plusAge,
  a.addr addr2,
  (a.emial) email,
  (SELECT 
    a.role_name 
  FROM
    t_role 
  WHERE user_id = a.id) AS roleName 
FROM
  t_user AS u,
  LEFT JOIN 
    (SELECT 
      r.role_name,
      r.user_id 
    FROM
      t_role AS r 
    WHERE r.deleted = '0') AS role 
    ON u.uid = role.uid 
    AND u.name = role.name 
WHERE a.id = role.user_id 
  AND a.id IN '$ids' 
  -- ?name /**?name**/
  AND (
    a.namn LIKE '%?{name}' 
    AND A.GAE > 20 
    OR (
      a.age < 18 
      AND a.create_time = '?createTime'
    )
  ) 
  AND EXISTS 
  (SELECT 
    1 
  FROM
    t_role 
  WHERE user_id = a.id) 
  AND 1 != 2 
  OR id = 1 