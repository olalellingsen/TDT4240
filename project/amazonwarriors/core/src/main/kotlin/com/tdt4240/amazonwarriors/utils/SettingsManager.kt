package com.tdt4240.amazonwarriors.utils

object SettingsManager {
    private var soundEffect: Boolean = true
    private var backgroundSound: Boolean = true

    fun isSoundEffectOn(): Boolean {
        return soundEffect
    }

    fun isBackgroundSoundOn(): Boolean {
        return backgroundSound
    }

    fun setSoundEffect(bool: Boolean) {
        soundEffect = bool
    }

    fun setBackgroundSound(bool: Boolean) {
        backgroundSound = bool
    }

}
