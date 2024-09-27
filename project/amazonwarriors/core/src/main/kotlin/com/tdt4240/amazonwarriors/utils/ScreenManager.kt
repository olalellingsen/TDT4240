package com.tdt4240.amazonwarriors.utils

import com.tdt4240.amazonwarriors.AmazonWarriors
import com.tdt4240.amazonwarriors.screens.views.GameEndView
import com.tdt4240.amazonwarriors.screens.views.GameView
import com.tdt4240.amazonwarriors.screens.views.LeaderboardView
import com.tdt4240.amazonwarriors.screens.views.MainMenuView
import com.tdt4240.amazonwarriors.screens.views.SettingsView
import com.tdt4240.amazonwarriors.screens.views.TutorialView
import ktx.app.KtxScreen
import java.util.EmptyStackException
import java.util.Stack

object ScreenManager {
    private var states: Stack<KtxScreen> = Stack<KtxScreen>()
    private lateinit var game: AmazonWarriors
    private var lastScreen: KtxScreen? = null

    fun addAllScreens() {

        // Add all screen
        game.addScreen(MainMenuView())
        game.addScreen(TutorialView())
        game.addScreen(SettingsView())
        game.addScreen(LeaderboardView())

    }

    fun setGame(game: AmazonWarriors) {
        this.game = game
    }

    fun peek(): KtxScreen? {
        return lastScreen
    }

    fun push(screen: KtxScreen) {
        lastScreen = try {
            states.peek()
        } catch (e: EmptyStackException) {
            null
        }
        states.push(screen)
        game.setScreen(screen::class.java)
    }

    fun pop() {
        lastScreen = states.peek()
        states.pop().dispose()
        game.setScreen(states.peek()::class.java)
    }

    fun set(screen: KtxScreen) {
        while (!states.empty()) {
            states.pop().dispose()
        }
        push(screen)
    }

    fun setEndGame(score: Int) {
        game.addScreen(GameEndView(score))
        game.removeScreen<GameView>()
        set(GameEndView(score))
    }

    fun leaveGame() {
        game.removeScreen<GameView>()
        set(MainMenuView())
        println("Leaving game states: $states")
    }

    fun leaveEndGame() {
        game.removeScreen<GameEndView>()
        set(MainMenuView())
    }

    fun setNewGame() {
        game.addScreen(GameView())
        set(GameView())
    }

}
