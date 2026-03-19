# Gradle + Cucumber JVM + Playwright Java E2E Framework

这是一个从零搭建的多模块端到端自动化测试框架：

- `core`：封装 Playwright 浏览器初始化、统一配置、Scenario 上下文与 Cucumber hooks。
- `test-suite`：承载具体业务测试，按 app 维度拆分 `steps` 与 `features` 目录。

## 目录结构

```text
.
├── build.gradle
├── core
│   └── src/main/java/com/example/e2e/core
│       ├── config
│       ├── context
│       ├── hooks
│       └── playwright
├── settings.gradle
└── test-suite
    ├── src/test/java/com/example/e2e/tests
    │   ├── runner
    │   └── steps
    │       ├── common
    │       └── demoapp
    └── src/test/resources/features
        ├── common
        └── demoapp
```

## 模块说明

### `core`

核心模块提供：

- `FrameworkConfig`：通过系统参数统一管理 `base.url`、`browser`、`headless` 等配置。
- `PlaywrightFactory` / `PlaywrightManager`：负责浏览器、上下文、页面生命周期管理。
- `ScenarioContext`：支持在 step definitions 间共享场景级数据。
- `CucumberHooks`：在场景前后自动创建 session、失败截图并输出 trace。

### `test-suite`

测试模块提供：

- `RunCucumberTest`：JUnit Platform + Cucumber 统一入口。
- `steps/common`：跨应用可复用的通用步骤。
- `steps/demoapp`：按业务 app 拆分的步骤定义。
- `features/common`、`features/demoapp`：按应用拆分 feature 文件。

## 运行方式

### 1. 生成 Gradle Wrapper

仓库中**不提交** `gradle-wrapper.jar`，以避免 PR 工具因二进制文件报错；首次克隆后请在本地执行：

```bash
gradle wrapper
```

### 2. 安装 Playwright 浏览器

```bash
./gradlew :test-suite:playwright --help
```

> 实际首次执行测试时，Playwright Java 会自动下载所需浏览器；如果 CI 需要预装浏览器，也可以在镜像阶段准备缓存。

### 3. 运行所有 E2E 用例

```bash
./gradlew clean test
```

### 4. 传入运行参数

```bash
./gradlew clean test \
  -Dbase.url=https://playwright.dev \
  -Dbrowser=chromium \
  -Dheadless=true \
  -Dslowmo=0
```

### 5. 只运行某个 app 的 features

```bash
./gradlew :test-suite:test -Dcucumber.features=src/test/resources/features/demoapp
```

## 扩展建议

新增 app 时，建议同步创建：

- `test-suite/src/test/java/com/example/e2e/tests/steps/<app-name>/`
- `test-suite/src/test/resources/features/<app-name>/`

这样可以让 step definitions 与 features 保持一致的业务边界。
