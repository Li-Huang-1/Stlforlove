# StLforLove 个人博客系统

一个基于 Spring Boot + Vue 3 的现代化个人博客网站，采用前后端分离架构，支持文章管理、访客统计、音乐播放、图片上传等功能。

## 项目预览

> 🎨 **首页特色**：玻璃拟态设计 · 动态波浪背景 · 音乐播放器 · 实时时钟 · 响应式布局

![首页截图](./Frontend/src/assets/image.jpg)

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 后端框架 |
| MyBatis + PageHelper | 1.4.7 | 数据访问层 & 分页 |
| MySQL | 8.0+ | 数据库 |
| Lombok | - | 简化代码 |
| Log4j2 | - | 日志框架 |
| Spring WebFlux | - | 调用 GitHub API |
| dotenv-java | 3.0.0 | 环境变量加载 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.2+ | 前端框架 |
| Vue Router | 4.6+ | 路由管理 |
| Element Plus | 2.13+ | UI 组件库 |
| Axios | 1.16+ | HTTP 请求 |
| Quill | 2.0+ | 富文本编辑器 |
| highlight.js | 11.11+ | 代码高亮 |
| @fontsource/noto-sans-sc | 5.2+ | 中文字体 |

## 功能特性

- 📝 **文章管理** - 支持创建、编辑、发布、删除文章
- 🎨 **富文本编辑器** - 基于 Quill 的 Markdown 编辑器，支持图片上传
- 👤 **用户系统** - 管理员登录、Token 鉴权
- 📊 **数据统计** - 文章数量、访客统计
- 🎵 **音乐播放器** - 首页音乐播放，支持列表循环、拖拽进度条
- 🖼️ **图片图床** - 基于 GitHub 仓库的图片上传，自动生成 CDN 链接
- 🎨 **精美 UI** - 玻璃拟态设计、动态波浪背景、响应式布局
- 📱 **移动端适配** - 支持手机、平板等多设备访问

## 项目结构

```
Stlforlove-1.0/
├── Backend/                          # 后端项目
│   ├── src/main/java/org/example/myblog/
│   │   ├── common/                   # 通用类（BaseController、AjaxResult 等）
│   │   ├── config/                   # 配置类（跨域、GitHub、环境变量）
│   │   ├── controller/               # 控制器层
│   │   ├── domain/                   # 实体类、DTO、VO
│   │   ├── interceptor/              # 拦截器（Token 鉴权）
│   │   ├── mapper/                   # MyBatis Mapper
│   │   ├── service/                  # 业务逻辑层
│   │   └── utils/                    # 工具类
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis XML 映射
│   │   ├── application.yml           # 应用配置
│   │   └── log4j2.xml                # 日志配置
│   └── pom.xml
├── Frontend/                         # 前端项目
│   ├── src/
│   │   ├── assets/                   # 静态资源（图片、音频等）
│   │   ├── components/               # Vue 组件
│   │   ├── router/                   # 路由配置
│   │   ├── utils/                    # 工具函数（request 封装）
│   │   ├── App.vue
│   │   └── main.js
│   └── package.json
└── README.md
```

## 快速开始

### 环境要求

- **Java** 17+
- **Node.js** 16+
- **MySQL** 8.0+
- **Maven** 3.6+

### 1. 数据库准备

创建数据库并执行以下 SQL 建表语句：

```sql
CREATE DATABASE IF NOT EXISTS myblog 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE myblog;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for articles
-- ----------------------------
DROP TABLE IF EXISTS `articles`;
CREATE TABLE `articles`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0草稿 1已发布',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章类别',
  `cover_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章封面',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '验证密钥',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for visitor_count
-- ----------------------------
DROP TABLE IF EXISTS `visitor_count`;
CREATE TABLE `visitor_count`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int NOT NULL DEFAULT 0 COMMENT '访客总数',
  `last_update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客计数表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```

### 2. 后端配置

在 `Backend/` 目录下创建 `.env` 文件：

```env
# 数据库配置
DB_HOST=localhost
DB_PORT=3306
DB_NAME=myblog
DB_USERNAME=root
DB_PASSWORD=your_password

# GitHub 图床配置（用于图片上传）
GITHUB_TOKEN=your_github_personal_access_token
GITHUB_USERNAME=your_github_username
GITHUB_REPO=my-blog-images
GITHUB_BRANCH=main
```

> **获取 GitHub Token**：访问 [GitHub Settings → Developer settings → Personal access tokens](https://github.com/settings/tokens) 生成一个具有 `repo` 权限的 Token。

**启动后端服务：**

```bash
cd Backend
./mvnw spring-boot:run
```

服务将在 `http://localhost:8081` 启动。

### 3. 前端配置

在 `Frontend/` 目录下创建 `.env.local` 文件：

```env
VUE_APP_BASE_API=http://localhost:8081
```

**启动前端开发服务器：**

```bash
cd Frontend
npm install
npm run serve
```

应用将在 `http://localhost:8080` 启动。

## API 接口

### 公开接口（无需认证）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/public/user/login` | 用户登录 |
| GET | `/public/user/info` | 获取用户信息 |
| POST | `/public/article/list` | 文章列表（简略，无内容） |
| POST | `/public/article/listdetail` | 文章列表（详情，含内容） |
| GET | `/public/article/{id}` | 获取文章详情 |
| GET | `/public/article/count` | 获取文章总数 |
| GET | `/public/visitor/increment` | 访客计数 +1 |
| GET | `/public/visitor/count` | 获取访客总数 |

### 管理接口（需要 Token 认证）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/admin/article/new` | 创建文章 |
| POST | `/admin/article/update` | 更新文章 |
| DELETE | `/admin/article/delete/{id}` | 删除文章 |
| POST | `/admin/upload` | 上传图片到 GitHub 图床 |

## 项目截图

### 首页
- 玻璃拟态风格卡片布局
- 个人信息展示
- 音乐播放器（支持封面旋转动画）
- 实时时钟
- 文章/随笔/杂项导航入口
- 社交媒体链接

### 管理后台
- 数据统计仪表盘
- 文章 CRUD 管理
- 富文本编辑器
- 系统设置

## 开发说明

### 目录结构

- **`/public`** - 无需认证的公开接口
- **`/admin`** - 需要 Token 认证的管理接口
- 后端通过 `TokenInterceptor` 拦截器实现鉴权
- 前端通过路由守卫实现页面访问控制

### 跨域配置

后端 `WebConfig.java` 已配置允许所有来源的跨域请求，开发环境无需额外配置。

### 图片上传流程

1. 前端通过 `POST /admin/upload` 接口上传图片
2. 后端调用 GitHub API 将图片存储到指定仓库
3. 返回 jsDelivr CDN 链接（`https://cdn.jsdmirror.com/gh/...`）
4. 前端将链接插入到文章中

## 部署指南

### 后端打包

```bash
cd Backend
mvnw.cmd spring-boot:run
java -jar target/Library-0.0.1-SNAPSHOT.jar
```

### 前端打包

```bash
cd Frontend
npm run build
```

打包后的静态文件位于 `Frontend/dist/` 目录，可以部署到 Nginx、Vercel 或其他静态托管服务。

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;

    # 前端静态文件
    location / {
        root /path/to/Frontend/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # 后端 API 反向代理
    location /api/ {
        proxy_pass http://localhost:8081/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 常见问题

### Q: 如何修改登录密码？

直接修改数据库中的 `user` 表：
```sql
UPDATE `user` SET `password` = 'new_password' WHERE `username` = 'admin';
```

### Q: 图片上传失败怎么办？

1. 检查 `.env` 文件中的 GitHub Token 是否正确
2. 确认 GitHub 仓库存在且 Token 有写入权限
3. 确认仓库大小限制（建议使用专用图片仓库）

### Q: 如何更换端口号？

修改 `Backend/src/main/resources/application.yml`：
```yaml
server:
  port: 8081  # 修改为所需端口
```

同时更新前端 `.env.local` 中的 `VUE_APP_BASE_API` 地址。

## 更新日志

### v1.0.0
- 🎉 初始版本发布
- ✅ 文章 CRUD 功能
- ✅ 用户登录认证
- ✅ GitHub 图床图片上传
- ✅ 访客统计功能
- ✅ 音乐播放器
- ✅ 响应式首页设计

## 许可证

本项目采用 [MIT License](./LICENSE) 开源协议。

## 作者

**Li_Huang1**
- GitHub: [@Li-Huang-1](https://github.com/Li-Huang-1)
- Bilibili: [空间链接](https://space.bilibili.com/410739371)

## 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Quill](https://quilljs.com/)
- [jsDelivr](https://www.jsdelivr.com/) (CDN 服务)