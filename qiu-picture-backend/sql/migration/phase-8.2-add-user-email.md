# Phase 8.2: User 表新增 email 字段

## 迁移 SQL

```sql
-- 新增 email 字段（允许 NULL，兼容老用户）
ALTER TABLE user ADD COLUMN email VARCHAR(128) NULL AFTER userPassword;

-- 加唯一索引（MySQL 唯一索引允许多个 NULL）
ALTER TABLE user ADD UNIQUE INDEX idx_email (email);
```

## 回滚 SQL

```sql
ALTER TABLE user DROP INDEX idx_email;
ALTER TABLE user DROP COLUMN email;
```

## 说明

- 新用户注册时 email 必填
- 老用户 email 为 NULL，不受唯一索引影响（MySQL InnoDB 下 nullable unique index 允许多个 NULL）
- 暂时不发送邮件、不校验验证码
- 后续可基于此字段扩展找回密码、邮箱验证码等功能
