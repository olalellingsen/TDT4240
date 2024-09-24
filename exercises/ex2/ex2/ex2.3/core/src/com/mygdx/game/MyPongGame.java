package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Controller.GameController;
import com.mygdx.game.Model.Ball;
import com.mygdx.game.Model.Paddle;
import com.mygdx.game.Model.PongManager;
import com.mygdx.game.View.Button;
import com.mygdx.game.View.Renderer;

public class MyPongGame extends ApplicationAdapter {
	private Renderer renderer;
	private GameController gameController;
	private Paddle leftPaddle, rightPaddle;
	private Ball ball;
	private int leftPlayerScore = 0;
	private int rightPlayerScore = 0;
	private static Button easyButton;
	private static Button mediumButton;
	private static Button hardButton;
	private static PongManager pongManager;

	@Override
	public void create() {
		renderer = new Renderer(); // view
		pongManager = PongManager.getInstance(); // singleton class, model

		// set initial game level to easy
		pongManager.setCurrentLevel(PongManager.GameLevel.EASY);

		// initialize paddles (model)
		int paddleHeight = pongManager.getPaddleHeight();
		leftPaddle = new Paddle(10, Gdx.graphics.getHeight() / 2 - 50, 20, paddleHeight);
		rightPaddle = new Paddle(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() / 2 - 50, 20, paddleHeight);

		// initialize ball (model) with random position
		float randomX = MathUtils.random(100, Gdx.graphics.getWidth() - 100);
		float randomY = MathUtils.random(100, Gdx.graphics.getHeight() - 100);
		ball = new Ball(randomX, randomY, 30);
		ball.setSpeed(pongManager.getBallSpeed());
		ball.setLeftPaddle(leftPaddle);
		ball.setRightPaddle(rightPaddle);

		// initialize buttons
		easyButton = new Button("Easy", 300, 20, 350, 100);
		easyButton.setIsClicked(true);
		mediumButton = new Button("Medium", 900, 20, 450, 100);
		mediumButton.setIsClicked(false);
		hardButton = new Button("Hard", 1600, 20, 350, 100);
		hardButton.setIsClicked(false);

		// initialize controller
		gameController = new GameController(this, leftPaddle, rightPaddle, ball, pongManager);

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				gameController.handleInput();
				return true;
			}
		});
	}


	@Override
	public void render() {
		gameController.handleInput();
		renderer.render(leftPaddle, rightPaddle, ball, leftPlayerScore, rightPlayerScore, easyButton, mediumButton, hardButton);
	}

	// methods used by controller for updating the score
	public void increaseLeftScore() {
		leftPlayerScore++;
	}
	public void increaseRightScore() {
		rightPlayerScore++;
	}

	// getters for buttons, ball and paddles
	public Button getEasyButton() {
		return easyButton;
	}
	public Button getMediumButton() {
		return mediumButton;
	}
	public Button getHardButton() {
		return hardButton;
	}
	public Ball getBall() {
		return ball;
	}
	public Paddle getRightPaddle() {
		return rightPaddle;
	}
	public Paddle getLeftPaddle() {
		return leftPaddle;
	}
}