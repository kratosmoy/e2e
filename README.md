# Gradle + Cucumber JVM + Playwright Java E2E Framework

这是一个从零搭建的多模块端到端自动化测试框架：

- `core`：封装 Playwright 浏览器初始化、统一配置、Scenario 上下文与 Cucumber hooks。
- `test-suite`：承载具体业务测试，每个 app 在模块内有自己的根目录，集中放置 runner、steps 和 feature 资源。

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
    │   └── demoapp
    │       ├── runner
    │       └── steps
    └── src/test/resources/apps
        └── demoapp
            └── features
```

## 模块说明

### `core`

核心模块提供：

- `FrameworkConfig`：通过系统参数统一管理 `base.url`、`browser`、`headless`、本地浏览器模式等配置。
- `PlaywrightFactory` / `PlaywrightManager`：负责浏览器、上下文、页面生命周期管理。
- `PlaywrightStepsSupport`：给 step definitions 提供 `page()` 和 `scenarioContext()` 访问器，避免在每个 step 里重复写 `PlaywrightManager.page()`。
- `ScenarioContext`：支持在 step definitions 间共享场景级数据。
- `CucumberHooks`：在场景前后自动创建 session、失败截图并输出 trace，同时把失败产物挂到 Allure。

### `test-suite`

测试模块提供：

- `DemoAppRunCucumberTest`：按 app 拆分的显式执行入口。
- `RunCucumberTest`：默认的全量执行入口类，用于 Gradle `test` 和直接 JUnit 运行。
- `tests/demoapp/runner`：demoapp 自己维护的 runner。
- `tests/demoapp/steps`：demoapp 自己维护导航、断言等步骤定义。
- `apps/demoapp/features`：demoapp 自己维护的 feature 文件。

## 运行方式

### 1. 生成 Gradle Wrapper

仓库中**不提交** `gradle-wrapper.jar`，以避免 PR 工具因二进制文件报错；首次克隆后请在本地执行：

```bash
gradle wrapper
```

如果你在内网环境里无法直连 Maven Central，可以在执行 Gradle 时显式指定公司 Maven 代理 / 镜像：

```bash
./gradlew test -Dmaven.repo.url=https://your-internal-maven.example.com/repository/maven-public/
```

也可以通过环境变量：

```bash
export MAVEN_REPO_URL=https://your-internal-maven.example.com/repository/maven-public/
```

### 2. Playwright 浏览器模式

默认情况下：

- 所有环境都会默认启用“本地浏览器模式”，并设置 `PLAYWRIGHT_SKIP_BROWSER_DOWNLOAD=1`
- 在 `browser=chromium` 且未显式指定路径时，默认走本机 `msedge`
- Linux 环境默认直接跳过这些 E2E task，避免在 CI 或开发机上误跑本地浏览器模式
- macOS 如需覆盖默认行为，仍可通过系统参数显式指定 channel 或 executable path

Windows 下常用启动方式：

```bash
./gradlew :test-suite:testDemoApp
```

如果要显式指定本地浏览器：

```bash
./gradlew :test-suite:testDemoApp \
  -Dplaywright.use.local.browser=true \
  -Dbrowser=chromium \
  -Dbrowser.channel=chrome
```

或者：

```bash
./gradlew :test-suite:testDemoApp \
  -Dplaywright.use.local.browser=true \
  -Dbrowser.executable.path="C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
```

### 3. 运行所有 E2E 用例

```bash
./gradlew clean test
```

> `test` 是标准 Gradle `Test` 任务，会直接执行全量 runner：`RunCucumberTest`。

### 4. 传入运行参数

```bash
./gradlew clean test \
  -Dbase.url=https://playwright.dev \
  -Dtest.env=qa \
  -Dhomepage.demoapp.qa=https://qa.playwright.dev \
  -Dbrowser=chromium \
  -Dheadless=true \
  -Dslowmo=0
```

首页地址解析优先级为：

1. `-Dhomepage.<app>.<test.env>`（例如 `homepage.demoapp.qa`）
2. `-Dhomepage.<app>`（例如 `homepage.demoapp`）
3. `-Dbase.url`

### 5. 只运行某个 app

```bash
./gradlew :test-suite:testDemoApp
```

这些 task 会执行自己的 app runner，并输出独立的报告与产物目录。

### 6. 显式聚合执行所有 app

```bash
./gradlew :test-suite:testAllApps
```

### 7. 生成 Allure 报告

```bash
./gradlew :test-suite:allureDownload
./gradlew :test-suite:allureReport
./gradlew :test-suite:allureServe
```

`allureDownload` 现在只检查本机是否已经有可用的 Allure CLI，不会再尝试联网下载 `allure-generator` / `allure-commandline`。

这意味着：

- 运行测试本身**不需要** `allure-generator` / `allure-commandline`
- 只有在执行 `allureReport` / `allureServe` 时，才需要本机提前提供 Allure CLI
- 可以通过 `-Dallure.commandline=/path/to/allure`、`ALLURE_COMMANDLINE` 或 `ALLURE_HOME` 指定 CLI 路径
- 如果依赖仓库也走内网镜像，可以同时使用 `-Dmaven.repo.url=...` 或 `MAVEN_REPO_URL`

`allureReport` / `allureServe` 会先执行 `testAllApps`，然后在检测到本地 Allure CLI 时基于 Allure results 生成或打开报告。

### 8. 在 IntelliJ + Cucumber+ 中使用

如果你在 IntelliJ IDEA 里安装了 `Cucumber+` plugin，推荐按下面的方式使用：

1. 先用 Gradle 导入项目，确认 `test-suite/src/test/resources/apps/` 下的 `.feature` 文件和 `test-suite/src/test/java/com/example/e2e/tests/` 下各 app 目录中的 step definitions 都已被 IDE 正确索引。
2. 打开任意 `.feature` 文件后，可以直接使用 `Cucumber+` 提供的跳转、查找 step definition、scenario 导航等能力。
3. 如果你只想快速跑当前 scenario 或当前 feature，可以直接在 `.feature` 文件里用 gutter run icon 启动。
4. 如果你想稳定地按 app 跑，优先在 IntelliJ 里直接运行对应的 runner class。

```text
test-suite/src/test/java/com/example/e2e/tests/demoapp/runner/DemoAppRunCucumberTest.java
test-suite/src/test/java/com/example/e2e/tests/runner/RunCucumberTest.java
```

5. `DemoAppRunCucumberTest` 用于 demoapp，`RunCucumberTest` 用于全量执行。
6. 每个 app runner 使用自己的 glue 范围，因此不同 app 可以定义同名 step 方法，不会在全量执行时互相冲突。
7. 如果需要传环境参数，在 IntelliJ 的 Run Configuration 里加 VM options，常用示例：

```text
-Dbase.url=https://playwright.dev -Dbrowser=chromium -Dheadless=false -Dslowmo=200
```

8. Windows 下如果希望强制走本地浏览器模式，可以在 VM options 里加：

```text
-Dplaywright.use.local.browser=true -Dbrowser=chromium -Dbrowser.channel=chrome
```

或者：

```text
-Dplaywright.use.local.browser=true -Dbrowser.executable.path=C:\Program Files\Google\Chrome\Application\chrome.exe
```

9. 如果是直接从 `.feature` 文件发起运行，项目会使用 `junit-platform.properties` 里的默认 Cucumber 配置；如果你需要更明确的执行边界，优先运行对应的 runner class。
10. IntelliJ 里的运行结束后，Allure 原始结果仍然会落到 `test-suite/build/allure-results/`。如果要看完整报告，继续在终端执行：

```bash
./gradlew :test-suite:allureServe
```

## 产物输出

- `test-suite/build/artifacts/demoapp/`
- `test-suite/build/allure-results/`

这样可以避免不同 app 在分任务执行时互相覆盖浏览器产物，同时统一由 Allure 汇总测试结果。

## 扩展建议

新增 app 时，建议同步创建：

- `test-suite/src/test/java/com/example/e2e/tests/<app-name>/runner/`
- `test-suite/src/test/java/com/example/e2e/tests/<app-name>/steps/`
- `test-suite/src/test/resources/apps/<app-name>/features/`
- `test-suite/build.gradle` 中的 app-specific `Test` task

详细步骤见 [docs/new-app-onboarding.md](/home/kratos/projects/e2e/docs/new-app-onboarding.md)。
