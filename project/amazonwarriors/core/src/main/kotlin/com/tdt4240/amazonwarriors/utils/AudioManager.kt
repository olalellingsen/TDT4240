package com.tdt4240.amazonwarriors.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import ktx.assets.disposeSafely

object AudioManager {
    private val menuMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("Audio/MenuMusic.mp3"))
    private val gameMusic: Music = Gdx.audio.newMusic(Gdx.files.internal("Audio/GameMusic.mp3"))
    private val click: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/click.mp3"))
    private val click2: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/click2.mp3"))
    private val clickBack: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/clickBack.mp3"))
    private val monkeySound: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/monkey.mp3"))
    private val jaguarSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/jaguar.mp3"))
    private val truckSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/truck.mp3"))
    private val jeffSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/jeff.mp3"))
    private val throwSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Audio/throw.mp3"))

    private var inGame: Boolean = false

    fun playMenuMusic() {
        menuMusic.isLooping = true
        menuMusic.volume = 0.5f
        inGame = false
        if (SettingsManager.isBackgroundSoundOn()) {
            menuMusic.play()
            gameMusic.pause()
        } else {
            menuMusic.pause()
        }
    }

    fun stopBackgroundMusic() {
        menuMusic.pause()
        gameMusic.pause()
    }

    fun disposeMusic() {
        menuMusic.disposeSafely()
        gameMusic.disposeSafely()
    }

    fun playGameMusic(){
        inGame = true
        gameMusic.isLooping = true
        gameMusic.volume = 0.5f
        if (SettingsManager.isBackgroundSoundOn()) {
            menuMusic.pause()
            gameMusic.play()
        } else {
            gameMusic.pause()
        }
    }

    fun playClick() {
        if (SettingsManager.isSoundEffectOn()) {
           click.play(0.5f)
        }
    }

    fun playClick2() {
        if (SettingsManager.isSoundEffectOn()) {
            click2.play(0.5f)
        }
    }

    fun playClickBack(){
        if (SettingsManager.isSoundEffectOn()) {
            clickBack.play(0.5f)
        }
    }

    fun playMonkeySound(){
        if (SettingsManager.isSoundEffectOn()) {
            if (inGame) { monkeySound.play(0.5f) }
        }
    }

    fun playJaguarSound(){
        if (SettingsManager.isSoundEffectOn()) {
            if (inGame) { jaguarSound.play(0.5f) }
        }
    }

    fun playThrowSound(){
        if (SettingsManager.isSoundEffectOn()) {
            if (inGame) { throwSound.play(0.5f) }
        }
    }

    fun playTruckSound(){
        if (SettingsManager.isSoundEffectOn()) {
            if (inGame) { truckSound.play(0.3f) }
        }
    }

    fun playJeffSound(){
        if (SettingsManager.isSoundEffectOn()) {
            if (inGame) { jeffSound.play(0.3f) }
        }
    }

    fun settingsTurnBackOnBackgroundMusic() {
        if (SettingsManager.isBackgroundSoundOn()) {
            if (inGame) {
                gameMusic.play()
            } else {
                menuMusic.play()
            }
        }
    }

}

