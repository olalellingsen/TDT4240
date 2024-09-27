# AmazonWarriors

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/tommyettinger/gdx-liftoff).

This project was generated with a Kotlin project template that includes Kotlin application launchers and [KTX](https://libktx.github.io/) utilities.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3. Currently this platform is not supported.
- `android`: Android mobile platform. Needs Android SDK.

## How to compile and run the project

This project uses [Gradle](http://gradle.org/) to manage dependencies and build tasks. The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.

Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/lib`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any). 
For example, `core:clean` removes `build` folder only from the `core` project.

### Android

The game is specifically made to be run on Android. To run the game on Android, you need to have Android Studio installed and an Android device connected to your computer. You can also use an emulator.
Alternatively, you can use the `android:installDebug` task to install the game on a connected device or emulator.

## System
The 'android' module contains the Android launcher for the game and the Android Firestore Service implementation. The core module contains all the game logic.
In the 'core' module AmazonWarriors is the main class that extends KtxGame and is responsible for managing the game screens.
The 'screens' package contains the MVVM structure of the game. Each screen has a Model, a ViewModel and a View. The Model stores the data relevant for the View, the ViewModel is responsible for the game logic, and the View is responsible for rendering the screens.
The 'ecs' package contains the Entity Component System structure of the game. The 'systems' package contains the systems that update the entities, and the 'components' package contains the components that are attached to the entities. Most entities are created in the GameViewModel class. 
