# sigm_app

Мобильный клиент мессенджера sigm на Kotlin Multiplatform + Compose Multiplatform.

## Стек

- **Kotlin Multiplatform** — общая бизнес-логика для Android и iOS
- **Compose Multiplatform** — общий UI для Android и iOS
- **Ktor Client** — HTTP и WebSocket
- **kotlinx.serialization** — сериализация JSON
- **kotlinx.datetime** — работа с датами
- **multiplatform-settings** — хранение токена
- **ViewModel + StateFlow** — управление состоянием

## Быстрый старт

### Требования
- Android Studio Hedgehog или новее
- Xcode 15+ (для iOS)
- JDK 21

### Настройка

Укажи адрес бэкенда в `Config.kt`:
```kotlin
object Config {
    const val BASE_URL = "192.168.x.x:8080"
    const val HTTP_URL = "http://$BASE_URL"
    const val WS_URL = "ws://$BASE_URL"
}
```

### Android
Открой проект в Android Studio и запусти `composeApp`.

### iOS
```bash
cd iosApp
open iosApp.xcodeproj
```
Запусти через Xcode на симуляторе или устройстве.

## Структура проекта

```
composeApp/src/
├── commonMain/kotlin/ru/hey_savvy/sigm_app/
│   ├── model/                  — модели данных
│   │   ├── User.kt
│   │   ├── Room.kt
│   │   ├── Message.kt
│   │   ├── UserProfile.kt
│   │   ├── AuthResponse.kt
│   │   ├── ProfileUpdate.kt
│   │   └── ChangeRequest.kt
│   ├── repository/             — работа с сетью
│   │   ├── ApiClient.kt        — общий HTTP клиент и токен
│   │   ├── AuthRepository.kt   — логин, регистрация, логаут
│   │   ├── RoomRepository.kt   — комнаты
│   │   ├── MessageRepository.kt — сообщения и WebSocket
│   │   └── UserRepository.kt   — профиль пользователя
│   ├── screen/                 — экраны
│   │   ├── LoginScreen.kt
│   │   ├── RoomsScreen.kt
│   │   ├── ChatScreen.kt
│   │   └── ProfileScreen.kt
│   ├── extension/
│   │   └── ModifierExtensions.kt — clearFocusOnTap
│   ├── Config.kt               — адрес бэкенда
│   ├── Theme.kt                — тема приложения
│   └── TokenStorage.kt         — хранение токена
├── androidMain/                — Android точка входа
│   └── MainActivity.kt
└── iosMain/                    — iOS точка входа
    └── MainViewController.kt
```

## Навигация

```
LoginScreen → RoomsScreen → ChatScreen
                  ↓
            ProfileScreen
```

## Архитектура

Проект следует паттерну **MVVM**:

- **Model** — data классы в `model/`
- **Repository** — сетевые запросы в `repository/`
- **ViewModel** — состояние экранов в `view/`
- **View** — Compose экраны в `screen/`

Общий код живёт в `commonMain` и компилируется для обеих платформ. Платформо-специфичный код минимален — только точки входа (`MainActivity` и `MainViewController`).

## Типы комнат

| Тип | Отображение |
|-----|-------------|
| `GROUP` | группа |
| `CHANNEL` | канал |
| `CHAT` | чат |

## Статусы пользователя

| Статус | Отображение |
|--------|-------------|
| `AVAILABLE` | 🟢 Доступен |
| `BUSY` | 🔴 Занят |
| `DO_NOT_DISTURB` | ⛔ Не беспокоить |
| `AWAY` | 🟡 Отошёл |
| `INVISIBLE` | ⚫ Невидимый |