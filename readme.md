## 项目介绍
#### 项目名称
ai-prompts-hub
#### 项目描述
一个轻量级、可私有部署的「AI 提示词模板仓库」。
基于 Spring Boot，自带 Web 管理后台 + OpenAPI，同时内置 MCP 服务器，让 Cursor、Cline、Trae 等对话式 IDE 一键拉取团队最新编码规范，生成代码即符合规范，无需人工复制粘贴。
#### 关键词
AI 团队规范管理

团队AI编码规范管理工具

## 技术栈说明

本项目采用 spring boot + mysql + jdk17

## 使用流程说明
### 1. 第一步先初始化表结构
### 2. 启动项目
### 3. 访问项目地址：http://localhost:8081
### 4. 创建项目
   ![img.png](doc/img.png)
### 5. 新增分类
   ![img_4.png](doc/img_4.png)
### 6. 新增提示词块
   ![img_1.png](doc/img_1.png)
   ![img_3.png](doc/img_3.png)
### 7. 配置提示词骨架
   ![img_5.png](doc/img_5.png)
   ![img_6.png](doc/img_6.png)
### 8. 预览
   ![img_7.png](doc/img_7.png)
   ![img_8.png](doc/img_8.png)
### 9. 保存提示词
   ![img_9.png](doc/img_9.png)
   ![img_10.png](doc/img_10.png)
### 10. 预览原始内容（非markdown格式）
    ![img_11.png](doc/img_11.png)
### 11. ID 说明
    ![img_12.png](doc/img_12.png)
### 12. 编辑骨架
    ![img_13.png](doc/img_13.png)

mcp使用视频介绍：
[使用示例.mp4](%E4%BD%BF%E7%94%A8%E7%A4%BA%E4%BE%8B.mp4)
#### MCP配置
```json
{
  "mcpServers": {
    "team-ai-updater": {
      "command": "D:/golande/ai-prompts-mcp/team-prompt.exe",
      "args": [],
      "env": {
        "API_BASE_URL": "http://localhost:8080",
        "TARGET_DIR": ".trae\\rules",
        "TARGET_FILENAME": "project_rules.md"
      }
    }
  }
}
```

### 说明
"API_BASE_URL": "http://localhost:8080", ai-prompts-hub的远程地址
"TARGET_DIR": ".trae\\rules", // AI规范放到什么目录，会读取项目的根目录，然后放到这个目录下， 如：D:\IdeaProjects\.trae\rules
"TARGET_FILENAME": "project_rules.md", // AI规范放到什么文件



