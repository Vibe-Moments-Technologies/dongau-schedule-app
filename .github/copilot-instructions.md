# GitHub Copilot Instructions for dongau-schedule-app

## О проекте
**dongau-schedule-app** («Расписание ДонГУ») — кроссплатформенное мобильное приложение расписания для студентов и преподавателей ДонГУ (Донской государственный аграрный университет) на базе **Kotlin Multiplatform (KMP)** и **Compose Multiplatform** для Android и iOS.

## Архитектура и технологии
- **Язык**: Kotlin 2.x
- **UI Framework**: Compose Multiplatform (Material 3)
- **Dependency Injection**: Koin 4.x (`koinViewModel()`, `koinInject()`, `singleOf`, `factoryOf`)
- **Сетевой стек**: Ktor 3.x Client (OkHttp engine на Android, Darwin engine на iOS)
- **Сериализация**: `kotlinx.serialization` (JSON с `ignoreUnknownKeys = true`)
- **Работа с датами**: `kotlinx.datetime` и `kotlin.time.Clock`
- **Асинхронность**: Kotlin Coroutines & StateFlow / SharedFlow
- **Хранилище**: `PlatformStorage` (`SharedPreferences` на Android, `NSUserDefaults` на iOS)

## Структура кодовой базы
- `shared/src/commonMain/kotlin/com/jetbrains/kmpapp/`:
  - `data/`: Репозитории (`ScheduleRepository`, `TaskRepository`), API (`DongauScheduleApi` — edu.dongau.ru, JSON, HTTP), парсер (`DongauScheduleParser` — циклическое расписание), модели данных (`Lesson`, `StudyTask`, `Subject`, `ScheduleTarget`), система кэширования (`UnifiedSyncManager`).
  - `screens/`:
    - `schedule/`: Главный экран расписания, цветные карточки занятий (`LessonCard`), детальный просмотр (`LessonDetailScreen`), миникалендарь (`WeekCalendarStrip`).
    - `tasks/`: Трекер учебных задач, конструктор предметов, дедлайны.
    - `notes/`: Электронные конспекты и заметки к парам.
    - `compare/`: Сравнение расписаний нескольких групп.
    - `services/`: Концентратор сервисов, не добавленных на панель.
    - `other/`: Настройки дока, выбор тем оформления, о программе, статистика памяти.
    - `components/`: Плавающий док (`FloatingDock`), свайп назад (`PlatformBackHandler`).
- `androidApp/`: Точка входа Android (`ScheduleApp.kt`, `MainActivity.kt`), конфигурация сборки и подписи.
- `iosApp/`: Xcode-проект и запуск iOS-приложения.

## Особенности проекта
- API расписания ДонГУ работает по **HTTP** (не HTTPS): в манифесте обязателен `android:usesCleartextTraffic="true"`.
- Поиск расписания — **только по группам** (`searchGroups`); преподаватели и аудитории не поддерживаются API.
- Аналитики и трекинга в приложении **нет** — не добавлять.
- Нумерация недель считается по датам семестра (`DateUtils.getWeekInfo`), внешних маркеров недель нет.

## Протокол AI-агента

Перед заметной правкой агент должен:

1. Прочитать `AGENTS.md` и релевантные `.agents/*.md`.
2. Проверить доступные плагины и навыки среды; для разработки использовать **Ponytail** (YAGNI, минимальный diff, переиспользование).
3. Использовать **Repowise MCP** до массового чтения: `get_overview`, затем `get_context`/`get_answer`/`search_codebase`; для риска — `get_risk`/`get_change_risk`.
4. Сначала изучить и спланировать изменение, затем менять код; после — выполнить подходящую проверку и сообщить результат.
5. Не добавлять секреты, временные файлы, артефакты Repowise/агентов и незапланированные изменения.

Если инструмент или навык недоступен, агент должен явно отметить это и сверять Repowise с живым исходником при stale-индексе.

## Правила разработки и решения Issue
1. **Безопасность потоков и старта**:
   - Все сетевые запросы и парсинг файлов ДОЛЖНЫ выполняться строго на `Dispatchers.IO` или `Dispatchers.Default`.
   - В конструкторах и блоках `init` ViewModels ЗАПРЕЩЕНО выполнять блокирующие сетевые запросы.
   - Любые операции с хранилищем и парсингом должны быть защищены `try-catch (_: Throwable)`.
2. **UI и дизайн**:
   - Поддержка темного и светлого оформления Material 3 + секретные темы Sakura / Cyberpunk / Matrix.
   - Корректные отступы под системные панели (WindowInsets: `navigationBarsPadding()`, `statusBarsPadding()`).
3. **Локализация**:
   - Весь интерфейс и текстовые сообщения пользователю составляются на русском языке.
