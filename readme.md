## 使用说明
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
