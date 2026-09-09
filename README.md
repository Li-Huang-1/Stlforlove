# StLforLove 个人博客系统

一个基于 Spring Boot + Vue 3 的现代化个人博客网站，采用前后端分离架构，支持文章管理与分类、留言板、访客统计、音乐播放、GitHub 图床上传、图片懒加载等功能。

## 项目预览

> 🎨 **首页特色**：玻璃拟态设计 · 动态波浪背景 · 音乐播放器 · 实时时钟 · 响应式布局

![首页截图](./Frontend/src/assets/image.jpg)

## 技术栈

### 后端
| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 后端框架 |
| MyBatis-Plus | 3.5.14 | ORM 框架（分页插件、字段自动填充、LambdaQueryWrapper） |
| MySQL | 8.0+ | 数据库 |
| Hibernate Validator | 6.2.3 | JSR-303 声明式参数校验 |
| spring-security-crypto | - | BCrypt 密码加密（仅引入加密模块，不含安全过滤链） |
| Lombok | - | 简化代码 |
| Log4j2 | - | 日志框架 |
| Spring WebFlux | - | WebClient 调用 GitHub API |
| dotenv-java | 3.0.0 | 环境变量加载 |

### 前端
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.2+ | 前端框架 |
| Vue Router | 4.6+ | 路由管理（含路由守卫） |
| Element Plus | 2.13+ | UI 组件库 |
| Axios | 1.16+ | HTTP 请求封装 |
| Vue Quill | 1.5+（Quill 2.0 内核） | 富文本编辑器 |
| highlight.js | 11.11+ | 代码高亮 |
| Fontsource | 5.2+ | Noto Serif SC / Roboto 字体 |
| IntersectionObserver | 浏览器原生 | 图片懒加载（v-lazy 自定义指令） |

## 功能特性

- 📝 **文章管理** - 创建、编辑、发布、删除文章，支持草稿状态与文章封面
- 🗂️ **文章分类** - 技术 / 项目 / 日志三个栏目，按类别浏览
- 💬 **留言板** - 访客发布留言，毛玻璃悬浮发布框，瀑布流卡片展示
- 🖼️ **图片懒加载** - 自定义 `v-lazy` 指令，封面图与富文本正文图滚动到视口才加载，占位图过渡
- 🛡️ **工程化后端** - 全局异常处理器（GlobalExceptionHandler）、JSR-303 参数校验、DTO/VO 分层
- � **安全认证** - 管理员 Token 鉴权、BCrypt 密码加密
- 🎨 **富文本编辑器** - 基于 Quill，支持图片上传与代码高亮
- 🎵 **音乐播放器** - 首页音乐播放，支持列表循环、拖拽进度条、封面旋转动画
- 🖼️ **GitHub 图床** - 图片上传至 GitHub 仓库，自动生成 jsDelivr CDN 链接
- ✨ **页面过渡动画** - 路由切换淡入淡出（含登录/后台重定向）
- 🎨 **精美 UI** - 玻璃拟态设计、动态波浪背景、响应式布局
- 📱 **移动端适配** - 支持手机、平板等多设备访问

## 项目结构

```
Stlforlove-1.5/
├── Backend/                          # 后端项目（Spring Boot）
│   ├── src/main/java/org/example/myblog/
│   │   ├── common/                   # 通用类（AjaxResult、TableDataInfo、PageQuery 等）
│   │   ├── config/                   # 配置类（跨域、GitHub、MyBatis-Plus、密码加密、dotenv）
│   │   ├── controller/               # 控制器层
│   │   ├── domain/
│   │   │   ├── dto/                  # 请求参数对象（JSR-303 校验注解）
│   │   │   ├── entity/               # 数据库实体类
│   │   │   └── vo/                   # 响应视图对象
│   │   ├── handle/                   # GlobalExceptionHandler 全局异常处理、FieldHandler 字段填充
│   │   ├── interceptor/              # TokenInterceptor 鉴权拦截器
│   │   ├── mapper/                   # MyBatis-Plus Mapper
│   │   ├── service/                  # 业务层（I*Service 接口 + impl 实现类）
│   │   └── utils/                    # 工具类（UserContext 等）
│   ├── src/main/resources/
│   │   ├── mapper/                   # MyBatis XML 映射
│   │   ├── application.yml           # 应用配置
│   │   └── log4j2.xml                # 日志配置
│   └── pom.xml
├── Frontend/                         # 前端项目（Vue 3 + Vue CLI）
│   ├── src/
│   │   ├── assets/                   # 静态资源（图片、音频、全局样式等）
│   │   ├── components/               # Vue 页面与组件
│   │   ├── directives/               # 自定义指令（lazy.js —— v-lazy 图片懒加载）
│   │   ├── router/                   # 路由配置与全局守卫
│   │   ├── utils/                    # 工具函数（request 封装）
│   │   ├── App.vue                   # 根组件（含路由过渡动画）
│   │   └── main.js                   # 应用入口
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
-- Table structure for articles（文章表）
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
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'tech' COMMENT '文章类型: tech-技术 project-项目 log-日志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for messages（留言表）
-- ----------------------------
DROP TABLE IF EXISTS `messages`;
CREATE TABLE `messages`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '昵称（前端 maxlength=20，留余量）',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容（前端 maxlength=200，留余量）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '留言列表按创建时间倒序查询索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '留言墙' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user（用户表）
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码（BCrypt 加密存储）',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '验证密钥',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '用户头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for visitor_count（访客计数表）
-- ----------------------------
DROP TABLE IF EXISTS `visitor_count`;
CREATE TABLE `visitor_count`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `count` int NOT NULL DEFAULT 0 COMMENT '访客总数',
  `last_update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '访客计数表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
```

### 2. 后端配置

在 `Backend/` 目录下创建 `.env` 文件（已在 `.gitignore` 中，不会提交到仓库）：

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
./mvnw spring-boot:run      # Windows 下使用 mvnw.cmd spring-boot:run
```

服务将在 `http://localhost:8081` 启动。

### 3. 前端配置

在 `Frontend/` 目录下创建 `.env.local` 文件（已在 `.gitignore` 中）：

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
| GET | `/public/message/list` | 获取留言列表 |
| POST | `/public/message` | 发布留言 |
| GET | `/public/visitor/increment` | 访客计数 +1 |
| GET | `/public/visitor/count` | 获取访客总数 |

### 管理接口（需要 Token 认证）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/admin/article/new` | 创建文章 |
| POST | `/admin/article/update` | 更新文章 |
| DELETE | `/admin/article/delete/{id}` | 删除文章 |
| POST | `/admin/upload` | 上传图片到 GitHub 图床 |

## 开发说明

### 分层与约定

- **`/public`** - 无需认证的公开接口；**`/admin`** - 需要 Token 认证的管理接口
- 后端通过 `TokenInterceptor` 拦截器实现鉴权，前端通过路由守卫实现页面访问控制
- 参数校验：DTO 上使用 JSR-303 注解（`@NotBlank`、`@Size` 等），Controller 使用 `@Valid` 触发
- 异常处理：Controller 不写 try-catch，统一由 `GlobalExceptionHandler` 捕获并返回标准响应
- Service 接口以 `I` 前缀命名（如 `IMessageService`），实现类位于 `service/impl`
- `create_time` / `update_time` 由 MyBatis-Plus 的 `FieldHandler` 在插入/更新时自动填充

### 图片懒加载

- 模板中的图片使用自定义指令：`<img v-lazy="图片地址" />`
- `v-html` 渲染的富文本正文图，在 DOM 更新后调用 `setupLazyImages(容器元素)` 批量接管
- 基于 IntersectionObserver 实现，图片进入视口前不发起网络请求，进入后预加载并替换占位图

### 跨域配置

后端 `WebConfig.java` 已配置允许所有来源的跨域请求，开发环境无需额外配置。

### 图片上传流程

1. 前端通过 `POST /admin/upload` 接口上传图片
2. 后端调用 GitHub API 将图片存储到指定仓库
3. 返回 jsDelivr CDN 链接（`https://cdn.jsdmirror.com/gh/...`）
4. 前端将链接插入到富文本编辑器中

## 部署指南

### 后端打包

```bash
cd Backend
./mvnw clean package
java -jar target/Stlforlove-0.0.1-SNAPSHOT.jar
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

密码使用 BCrypt 加密存储，不可直接写明文。可在数据库中更新为 BCrypt 哈希值，或通过注册/初始化逻辑生成：
```sql
-- password 字段需存放 BCrypt 加密后的字符串，而非明文
UPDATE `user` SET `password` = 'BCrypt加密后的密码' WHERE `username` = 'admin';
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

### v1.5.0
- 💬 新增留言板功能（留言发布、列表展示、毛玻璃悬浮发布框）
- 🗂️ 新增文章分类栏目：技术 / 项目 / 日志
- 🖼️ 新增图片懒加载：`v-lazy` 自定义指令，封面图与富文本正文图进入视口才加载
- ✨ 新增路由切换淡入淡出过渡动画（含登录/后台重定向场景）
- 🛡️ 新增 GlobalExceptionHandler 全局异常处理与 JSR-303 声明式参数校验
- 🔐 密码改为 BCrypt 加密存储
- 🔄 数据访问层迁移至 MyBatis-Plus（分页插件、字段自动填充、LambdaQueryWrapper）
- 🖼️ 文章封面字段支持

### v1.0.0
- 🎉 初始版本发布
- ✅ 文章 CRUD 功能
- ✅ 用户登录认证
- ✅ GitHub 图床图片上传
- ✅ 访客统计功能
- ✅ 音乐播放器
- ✅ 响应式首页设计

## 许可证

本项目采用 [MIT License](./LICENSE.md) 开源协议。

## 作者

**Li_Huang1**
- GitHub: [@Li-Huang-1](https://github.com/Li-Huang-1)
- Bilibili: [空间链接](https://space.bilibili.com/410739371)

## 致谢

感谢以下开源项目：
- [Spring Boot](https://spring.io/projects/spring-boot)
- [MyBatis-Plus](https://baomidou.com/)
- [Vue.js](https://vuejs.org/)
- [Vue Quill](https://github.com/vueup/vue-quill)
- [Element Plus](https://element-plus.org/)
- [jsDelivr](https://www.jsdelivr.com/) (CDN 服务)
