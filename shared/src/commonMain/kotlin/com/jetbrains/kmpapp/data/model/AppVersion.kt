package com.jetbrains.kmpapp.data.model

object AppVersion {
    /**
     * Базовая версия текущей линии разработки в формате YY.X.Z.
     * CI подставляет полный VERSION_NAME по каналу:
     *  - тег v26.1.0 / v26.1.1      → stable
     *  - тег v26.1.0-beta.3 / -rc.1  → beta / rc (prerelease)
     *  - push в main                 → 26.X-dev.N (rolling preview)
     *  - contributor build           → 26.X-contrib.N
     */
    const val RELEASE_VERSION = "26.1.0"
    const val VERSION_NAME = RELEASE_VERSION

    /** stable | beta | rc | dev | contrib — подставляет CI через tools/versioning.py */
    const val BUILD_CHANNEL = "stable"

    /**
     * Числовой код сборки. CI вычисляет ОДИН раз на запуск в resolve-джобе
     * (epoch-секунды) и раздаёт всем джобам через --build-id: монотонно во
     * всех каналах, влезает в Int32 / Android versionCode (max 2147483647).
     * github.run_id и github.run_started_at НЕ подходят.
     */
    const val BUILD_NUMBER = 1
    const val COMMIT_SHA = "local"

    /** Стабильный канал обновлений (обновляется только стабильными релизами). */
    const val UPDATE_FEED_URL = "https://raw.githubusercontent.com/Vibe-Moments-Technologies/dongau-schedule-app/gh-pages/version.json"

    const val IS_CRITICAL = false
    const val MIN_SUPPORTED_BUILD = 1
    const val CHANGELOG = "Первая версия «Расписание ДонГУ» на новой базе: расписание занятий ДонГУ (группы), задачи, сравнение расписаний, напоминания о парах, темы оформления, цветные карточки пар."

    const val DISPLAY_VERSION = "Версия $VERSION_NAME (сборка $BUILD_NUMBER)"
    const val GITHUB_REPO = "Vibe-Moments-Technologies/dongau-schedule-app"
    const val GITHUB_REPO_URL = "https://github.com/Vibe-Moments-Technologies/dongau-schedule-app"
    const val GITHUB_ISSUES_URL = "https://github.com/Vibe-Moments-Technologies/dongau-schedule-app/issues"
    const val DEVELOPER_NAME = "Vibe Moments Technologies"

}
