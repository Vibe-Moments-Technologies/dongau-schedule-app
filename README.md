# Расписание ДонГУ (Dongau Schedule)

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20iOS-3DDC84.svg?logo=android&logoColor=white)](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/releases)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.4.10-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.12.0-4285F4.svg?logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License](https://img.shields.io/badge/License-GPL_v3-blue.svg)](LICENSE)

Кроссплатформенное мобильное приложение для студентов и преподавателей ДонГУ (Донской государственный аграрный университет): расписание занятий, задачи, конспекты и сравнение расписаний.

> [!NOTE]
> **Проект неофициальный.** Не аффилирован с ДонГУ. Название вуза и все данные (расписание) принадлежат их правообладателям; приложение только отображает их со ссылкой на официальные источники.

**Скачать:** [APK (Android, стабильная версия)](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/releases/latest/download/DongauSchedule.apk) · [IPA (iOS, без подписи)](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/releases/latest/download/DongauSchedule.ipa) · [Все релизы](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/releases)

**Сообщество:** [GitHub](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app)

---

## Возможности

**Расписание**
- Поиск и просмотр расписания групп.
- Нумерация недель семестра, чётные и нечётные недели.
- Цветные карточки пар по типу занятия (лекция / практика / лабораторная).
- Текущая пара и прогресс до её конца.
- Несколько сохранённых расписаний с быстрым переключением.
- Подсветка изменений при обновлении расписания.
- Локальный кэш: расписание доступно без интернета.
- Напоминания о занятиях: локальные уведомления с настраиваемым временем.

**Сравнение расписаний**
- Несколько групп рядом, подсветка различий, поиск по сохранённым расписаниям.

**Задачи**
- Задачи по предметам: категории (лабораторные, практики, домашние задания, курсовые и другие), приоритеты, статусы, чеклисты подзадач.

**Конспекты**
- Страницы заметок с цветными полями, поиском, переименованием и подтверждением удаления.
- Заметки к парам и предметам прямо из карточки занятия.
- Хранятся локально на устройстве и никуда не отправляются.

**Интерфейс**
- Темы: светлая, тёмная, системная; секретные оверлеи Sakura / Cyberpunk / Matrix.
- Настраиваемая нижняя панель страниц: порядок и видимость разделов.
- Раздел «Сервисы»: доступ к страницам, не добавленным на панель.
- Встроенная проверка обновлений.

---

## Установка

### Android
1. Скачайте [DongauSchedule.apk](https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/releases/latest/download/DongauSchedule.apk) из последнего стабильного релиза.
2. Установите приложение, разрешив установку из неизвестных источников.
3. Дальше приложение само проверяет обновления и предлагает установить новую версию.

### iOS
IPA собирается без подписи, поэтому для установки его нужно переподписать любым инструментом для sideload.

Источник приложений (AltStore-совместимый формат):

```
https://raw.githubusercontent.com/Vibe-Moments-Technologies/dongau-schedule-app/gh-pages/apps.json
```

---

## Сборка из исходников

Требования: JDK 21, Android SDK (compileSdk 37, minSdk 24, targetSdk 37), Xcode 16+ для iOS. Gradle 9.6.1 подключается через wrapper (`gradlew`).

```bash
git clone https://github.com/Vibe-Moments-Technologies/dongau-schedule-app.git
cd dongau-schedule-app

# Android (debug APK)
./gradlew assembleDebug
# результат: androidApp/build/outputs/apk/debug/androidApp-debug.apk

# iOS (unsigned, на macOS)
cd iosApp
xcodebuild -scheme iosApp -configuration Release -sdk iphoneos \
  CODE_SIGNING_ALLOWED=NO CODE_SIGNING_REQUIRED=NO build
```

Тесты общего кода: `./gradlew :shared:allTests`.

Стек: Kotlin Multiplatform, Compose Multiplatform, Ktor Client, kotlinx.serialization, Koin.

---

## Источники данных

- **Расписание:** публичный API расписания ДонГУ ([edu.dongau.ru](https://edu.dongau.ru)).
- Приложение не отправляет данные пользователей: сетевые запросы — только чтение (API расписания и GitHub: проверка обновлений, список контрибьюторов, ссылки соцсетей).

## Лицензия

Код проекта распространяется по [GNU GPL v3](LICENSE).

Приложение основано на [MIREA-Schedule](https://github.com/l1ratch/MIREA-Schedule) и его форке [Dongau-Schedule](https://github.com/KiraSunshine/Dongau-Schedule).

Права на использованные данные и материалы остаются за их правообладателями: расписание и сведения о занятиях — ДонГУ.
