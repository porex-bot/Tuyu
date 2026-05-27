# Qiu Picture Backend

图片管理后端项目

## 环境配置

### 1. 数据库配置

在 IDEA 运行配置中设置环境变量：
```
DB_USERNAME=root
DB_PASSWORD=your_password
```

或者直接修改 `application.yml` 中的数据库配置。

### 2. 对象存储配置

复制 `src/main/resources/application-local.yml.example` 为 `application-local.yml`，并填入真实的腾讯云 COS 配置：

```yaml
cos:
  client:
    host: https://your-bucket.cos.ap-region.myqcloud.com
    secretId: your_secret_id
    secretKey: your_secret_key
    region: ap-beijing
    bucket: your-bucket-name
```

**注意：`application-local.yml` 已在 `.gitignore` 中，不会被提交到仓库。**

## 运行项目

```bash
mvn spring-boot:run
```

## 技术栈

- Spring Boot
- MyBatis Plus
- MySQL
- Redis
- 腾讯云 COS
