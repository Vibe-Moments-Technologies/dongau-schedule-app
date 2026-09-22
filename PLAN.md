# План-проект: Dongau Schedule на базе krasava-app

Цель: взять `Vibe-Moments-Technologies/krasava-app` (main) как базу, вычистить всё
МИРЭА-специфичное, пересадить data-слой ДонГУ из `KiraSunshine/Dongau-Schedule`
(main + ветка `fix`), перенести визуальные фишки форка, опубликовать в
`Vibe-Moments-Technologies/dongau-schedule-app`.

Оба репо уже изучены, локальные копии: `%TEMP%\dsh-XtZCoM\dsh-study\{krasava,dongau-fork}`.

## Ключевые факты (результаты изучения)

- Общая кодовая база (KMP, `com.jetbrains.kmpapp`, Compose MP 1.12, Kotlin 2.4.10,
  ktor 3.5.1, Koin 4.2.2). Общих файлов commonMain — 51, разъехались — 33.
  Git-merge невозможен (у krasava переписанная история, 1 коммит), только ручной перенос.
- API ДонГУ в форке уже написан: `DongauScheduleApi` (http://edu.dongau.ru/api/raspGrouplist,
  /api/Rasp, JSON, только группы) + `DongauScheduleParser` (циклическое расписание,
  expandCyclic, defaultBells 1–7) + модели (`DongauApiResponse/DongauGroupItem/DongauScheduleResponse`,
  `LessonType.fromDisciplineName`) — живут в fork-файлах `api/DongauScheduleApi.kt`,
  `parser/DongauScheduleParser.kt`, `model/ScheduleModels.kt`.
- Ветка `fix` форка = playbook: −Free Rooms, анти-flash старт (MainActivity + night-темы),
  фикс нумерации недель (DateUtils: понедельники от недели 1 сентября), поиск только групп,
  credits. Патчем не переносится — krasava ушла вперёд; переносим руками по смыслу.
- Темы Sakura/Cyberpunk/Matrix (`SakuraTheme.kt`, `ThemeOverlay.kt`) идентичны в обоих — не трогаем.
- Site colors из форка = сплошная покраска LessonCard по типу (`getTypeBadgeColors` → containerColor).
  В krasava карта богаче (заметки, прогресс-анимация, 5-й тип ADDITIONAL) — merge руками.
- Модели разъехались: krasava `LessonType` имеет ADDITIONAL; `ScheduleTargetType` использует
  `pathName` (для iCal), форк — `apiParam` (idGroup/idTeacher/idAuditorium). Krasava
  `ScheduleStorage` переписан (+467 строк: week markers, notes, multi-schedule).
- Недели: krasava берёт `SemesterWeeks.weekNumberFor()` (маркеры из iCal) с фолбэком
  `computedWeekNumber()`. У ДонГУ маркеров нет → маркеры не сетуются, фолбэк из ветки `fix`
  становится основной логикой. Инфраструктуру SemesterWeeks оставляем (ноль изменений в UI).
- VpnDetector в krasava — чисто МИРЭА-история (их серверы гео-блокированы). Для ДонГУ удалить.
- Хвосты ребренда: applicationId `ru.vibemoments.krasava`, BUNDLE_ID iOS, APP_NAME «Красава!»,
  strings.xml, иконки (mipmap ic_launcher_new*, drawable appicon_*), `AppVersion`
  (GITHUB_REPO, UPDATE_FEED_URL, CHANGELOG), `RemoteConfigLoader` (CONFIG_URL на gh-pages krasava),
  ResourcesScreen (6 ссылок mirea.ru), TeamScreen/AboutScreen, config.json, workflows
  (имена артефактов Krasava.apk/.ipa, issue-triage текст), `.github/copilot-instructions.md`.
- Лицензия GPL-3.0 в обоих — сохраняем, добавляем атрибуцию MIREA-Schedule (credit уже есть в fix).

## Фазы

### Фаза 0. Bootstrap (в `E:\Projects Directory\Dongau-Schedule`)
- [ ] Скопировать krasava-app (main) в workspace поверх пустого git-репо
      (историю krasava не тащим — стартуем одним squash-коммитом «Initial: krasava-app base»).
- [ ] origin уже указывает на dongau-schedule-app. Добавить remote `fork` =
      KiraSunshine/Dongau-Schedule (для справки при переносе; в финале убрать).
- [ ] Проверить baseline-сборку: `gradlew :shared:compileKotlinMetadata` или
      `gradlew androidApp:assembleDebug` (нужен JDK 17+; проверить наличие локально).

### Фаза 1. Ампутация МИРЭА-подсистем (механическая, по списку)
Удалить файлы:
- [ ] `data/api/MireaScheduleApi.kt`, `data/parser/MireaICalParser.kt`
- [ ] `commonTest/.../MireaICalParserLessonsTest.kt`, `MireaICalParserWeeksTest.kt`
- [ ] `screens/map/*` (5 файлов), `androidMain/.../map/CampusMapView.android.kt`,
      `iosMain/.../map/CampusMapView.ios.kt`, `shared/.../files/maps/**`, корневой `maps/`
- [ ] `data/FreeRoomsRepository.kt`, `data/model/FreeRoomsModels.kt`, `screens/rooms/*`,
      `tools/free_rooms_builder/`, `.github/workflows/sync-free-rooms.yml`, `FREE_ROOMS_API.md`
- [ ] Аналитика целиком: `data/analytics/*` (common+android+ios), `AppMetricaEngine.swift`,
      appmetrica из `libs.versions.toml` и `shared/build.gradle.kts`, вызовы из
      iOSApp.swift/ContentView.swift, упоминание в LicensesScreen
- [ ] `data/network/VpnDetector.kt` + android/ios actual'ы + UI-плашка VPN (найти потребителей)
Вычистить ссылки:
- [ ] `AppTab`: убрать FREE_ROOMS и MAP (FloatingDock.kt), `App.kt` (ветки навигации ~181,187,235,239),
      `ScheduleStorage` DEFAULT_DOCK_TABS + sanitize (~856), `OtherViewModel` (~94,96),
      `ServicesScreen` (~76,78,85,111,147,155)
- [ ] `Koin.kt`: убрать singleOf(MireaScheduleApi/FreeRoomsRepository/RemoteConfigLoader?) и VM
- [ ] `ScheduleRepository`: точки входа — строки 3,14,293,295,337,338,341 (см. фазу 2)
- [ ] `DebugConfig.isMapCoordinatePlaneEnabled`, `DebugSettingsScreen` карта-секция (как в fix)
- [ ] Убрать из workflows всё про free_rooms sync

### Фаза 2. Data-слой ДонГУ (ядро пересадки)
- [ ] Скопировать из форка: `DongauScheduleApi.kt`, `DongauScheduleParser.kt`,
      Dongau-модели (`DongauApiResponse`, `DongauGroupItem`, `DongauScheduleResponse`,
      `DongauScheduleItem`, `LessonType.fromDisciplineName`) — влить в krasava `ScheduleModels.kt`,
      СОХРАНИВ krasava-поля (`id: String`, `groups`, ADDITIONAL) и добавив fork-поле `isReplacement`
      только если krasava-код его не ломает (иначе выбросить).
- [ ] `ScheduleTargetType`: добавить `apiParam` из форка (idGroup/...), `pathName` удалить вместе с iCal.
- [ ] `ScheduleRepository`: заменить 5 МИРЭА-точек —
      `api.search(query)` → `api.searchGroups(query)`;
      `getIcal+MireaICalParser.parse` → `getSchedule+DongauScheduleParser.parse`;
      `saveWeekMarkers(...)` — убрать вызов (SemesterWeeks остаётся пустым → фолбэк).
- [ ] `DateUtils.getWeekInfo/computedWeekNumber`: перенести фикс из `fix`-ветки
      (startOfWeek, недели от понедельника недели 1 сентября). Обновить `DateUtilsTest`
      (в krasava есть тест — прогнать, добавить кейс «1 сентября в середине недели»).
- [ ] AndroidManifest: `android:usesCleartextTraffic="true"` (HTTP edu.dongau.ru) — в krasava
      его НЕТ, обязательно добавить.
- [ ] Koin: `singleOf(::DongauScheduleApi)`.
- [ ] Проверить стабильность `Lesson.id` из fork-парсера (ноты/диффы krasava ключуются по id).

### Фаза 3. Ребренд
- [ ] `applicationId` → `ru.vibemoments.dongauschedule` (или согласовать с юзером),
      namespace оставляем `com.jetbrains.kmpapp` (переименование пакета — отдельная большая
      механическая задача, на релиз не влияет; ponytail: сделать позже или никогда)
- [ ] iOS: `Config.xcconfig` BUNDLE_ID/APP_NAME, Info.plist display name
- [ ] `strings.xml` app_name → «Расписание ДонГУ», иконки — из форка (mipmap ic_launcher*
      + drawable ic_launcher_*) или новые от юзера; manifest icon/roundIcon поправить
      (krasava ссылается на ic_launcher_new)
- [ ] `AppVersion`: RELEASE_VERSION 26.1.0 (build 1 — как в fix), GITHUB_REPO/URL/ISSUES →
      dongau-schedule-app, UPDATE_FEED_URL → gh-pages dongau-schedule-app, CHANGELOG новый,
      DEVELOPER_NAME — по решению юзера
- [ ] `RemoteConfigLoader.CONFIG_URL` → gh-pages dongau-schedule-app/config.json;
      `config.json` в репо: telegram/ссыллки ДонГУ (или удалить загрузчик и захардкодить —
      решение на месте; проще перенацелить)
- [ ] ResourcesScreen: 6 ссылок mirea.ru → ссылки из форка (edu.dongau.ru ×2, dongau.ru),
      OtherScreen subtitle → «Портал, Расписание и Официальный сайт» (как в fix)
- [ ] AboutScreen/TeamScreen/TeamMembers: credits из fix (KiraSunshine + «Материнское
      приложение Mirea-Schedule» + про роль l1ratch/prosto-max решить с юзером);
      drawable team_*.jpg — заменить/удалить
- [ ] Workflows: имена артефактов Krasava.* → DongauSchedule.*, текст issue-triage,
      copilot-instructions; README.md — из форка (ДонГУ-описание) с апдейтом фич krasava
- [ ] gh-pages нового репо: version.json (+beta.json если нужен), config.json — отдельный шаг публикации

### Фаза 4. Визуальные фишки из форка
- [ ] **Site colors**: в krasava `LessonCard` покрасить контейнер карты цветом типа
      (fork-подход: containerColor = typeBg, текст белый, полупрозрачные белые подложки),
      сохранив krasava-фичи карты (бейдж заметки, прогресс, ADDITIONAL-цвет из fix-палитры
      не нужен — берём krasava-палитру TYPE_BADGE_COLORS как источник цвета, но красим всю карту).
      Проверить в обеих темах + всех 3 оверлеях.
- [ ] **Анти-flash старт**: перенести из fix `MainActivity.readPersistedTheme/resolveWindowBackground`
      (window background под тему/оверлей до setContent), КЛЮЧИ PREFS — krasava-собственные
      (узнать фактические KEY_APP_THEME/KEY_THEME_OVERLAY в krasava ScheduleStorage, не dongau_*).
      Night-темы res/values[-night] в krasava уже есть — сверить цвета window_background с оверлеями.
- [ ] **Поиск только групп**: AddScheduleBottomSheet — placeholder «Группа», убрать filter chips
      (если в krasava есть), hint «Введите название группы» (fix-ветка, но по krasava-коду)
- [ ] ManageSchedulesScreen: убрать filter chips типов (fix)
- [ ] Пустой день: krasava-вариант оставить (новее); сверить с fork-видом, если юзер хочет борзую —
      в форке её нет (только 🏉), так что ничего не переносим

### Фаза 5. Сборка, тесты, верификация
- [ ] `gradlew clean :shared:allTests` (уцелевшие: DateUtilsTest, VersionComparatorTest)
- [ ] `gradlew androidApp:assembleDebug` — APK ставится/запускается (эмулятор или устройство юзера)
- [ ] Ручной прогон: добавление группы (поиск edu.dongau.ru), расписание на неделю
      (чётность/нумерация!), site colors в светлой/тёмной/Sakura, заметки к паре, задачи,
      сравнение расписаний, уведомления, dock без аудиторий/карты, экраны «Другое»
- [ ] iOS: только компиляция shared (`linkPodDebugFrameworkIosArm64`) — полная сборка требует macOS,
      помечаем в README как «iOS не верифицирован локально»

### Фаза 6. Публикация
- [ ] Финальный коммит, push в main dongau-schedule-app (потребует эскалацию sandbox —
      credential helper блокируется; одна approval-подсветка на push)
- [ ] gh-pages: version.json/config.json (можно первым релизом позже)
- [ ] Тег v26.1.0 + релиз (по желанию юзера)

## Риски и решения

| Риск | Митигция |
|---|---|
| ScheduleStorage krasava завязан на week markers / iCal-метаданные | Маркеры не сетуем — фолбэк DateUtils; места saveWeekMarkers вычищаем в фазе 2 |
| `Lesson.id` форка может collide после expandCyclic | Проверить генерацию id; при коллизиях — суффикс даты/недели (ноты и диффы зависят) |
| edu.dongau.ru отдаёт HTTP и может быть медленным/нестабильным | cleartextTraffic + таймауты ktor уже есть; офлайн-кэш расписания в storage остаётся |
| krasava ServicesScreen/dock ожидают вкладки MAP/FREE_ROOMS в нескольких местах | Полный grep `AppTab\.` после удаления enum-значений — компилятор поймает остальное |
| Удаление AppMetrica заденет PDP_POLICY/PRIVACY и «экран приветствия» (согласие на аналитику) | Прочесать на упоминания аналитики; consent-экран выпилить или оставить как «приветствие» без трекинга |
| iOS-сборка не проверяется на Windows | Компилируем только shared framework; iosApp правки — аккуратный diff без сборки |
| Пакет `com.jetbrains.kmpapp` чужой | На релиз не влияет; переименование — опционально отдельным PR (механический rename) |

## Оценка

Фаза 1+2 (ядро) — основная работа, ~1 день плотных сессий. Фазы 3–4 — ~1 день.
Фаза 5 — итеративно с юзером (нужен девайс/эмулятор для ручного прогона).
Итого реально довести до собирающегося APK с расписанием ДонГУ за 2–3 рабочих дня.

## Открытые вопросы к юзеру (не блокируют фазы 0–2)

1. applicationId и название приложения в сторах: «Расписание ДонГУ» (как в форке)?
2. DEVELOPER_NAME/команда в About: кто в credits нового приложения?
3. Нужен ли beta-канал обновлений (в fix его UI убрали — предлагаю не возвращать)?
4. Telegram/соцссылки для config.json нового репо?
5. Иконки: берём из форка или будут новые?
