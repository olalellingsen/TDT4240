package com.mygdx.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyPongGame extends ApplicationAdapter {
	private ShapeRenderer shapeRenderer;
	private Paddle leftPaddle, rightPaddle;
	private Ball ball;
	private int leftPlayerScore = 0;
	private int rightPlayerScore = 0;
	private BitmapFont font;
	private PongManager pongManager;

	@Override
	public void create() {
		pongManager = PongManager.getInstance();

		// set game level
		pongManager.setCurrentLevel(PongManager.GameLevel.MEDIUM);

		int paddleHeight = pongManager.getPaddleHeight();

		shapeRenderer = new ShapeRenderer();
		leftPaddle = new Paddle(10, Gdx.graphics.getHeight() / 2 - 50, 20, paddleHeight);
		rightPaddle = new Paddle(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() / 2 - 50, 20, paddleHeight);

		// initialize ball with random position
		float randomX = MathUtils.random(100, Gdx.graphics.getWidth() - 100);
		float randomY = MathUtils.random(100, Gdx.graphics.getHeight() - 100);
		ball = new Ball(randomX, randomY, 30);
		ball.setSpeed(pongManager.getBallSpeed());
		ball.setLeftPaddle(leftPaddle);
		ball.setRightPaddle(rightPaddle);

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				handleInput();
				return true;
			}
		});

		font = new BitmapFont();
		font.setColor(Color.BLACK);
	}


	private void handleInput() {
		if (Gdx.input.isTouched()) {
			float screenX = Gdx.input.getX();
			float screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

			float oldLeftHeight = leftPaddle.getY();
			float oldRightHeight = rightPaddle.getY();

			float newY = MathUtils.clamp(screenY - leftPaddle.getHeight() / 2, 0, Gdx.graphics.getHeight() - leftPaddle.getHeight());

			if (screenX < Gdx.graphics.getWidth() / 2) {
				leftPaddle.setY(newY);
				rightPaddle.setY(oldRightHeight);
			}

			if (screenX > Gdx.graphics.getWidth() / 2) {
				rightPaddle.setY(newY);
				leftPaddle.setY(oldLeftHeight);
			}
		}
	}


	@Override
	public void render() {
		handleInput();

		// clear screen
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float deltaTime = Gdx.graphics.getDeltaTime();
		ball.update(deltaTime);

		checkForScore();
		// start shape rendering
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		// draw paddles
		leftPaddle.draw(shapeRenderer);
		rightPaddle.draw(shapeRenderer);
		ball.draw(shapeRenderer);

		drawScores();
		// end shape rendering
		shapeRenderer.end();
	}

	private void checkForScore() {
		if (ball.getX() - ball.getRadius() < 0) {
			// Right player gets point
			rightPlayerScore++;
			resetGame();
		}

		if (ball.getX() + ball.getRadius() > Gdx.graphics.getWidth()) {
			// Left player gets point
			leftPlayerScore++;
			resetGame();
		}
	}
	private void resetGame() {
		ball.setX(Gdx.graphics.getWidth() / 2);
		ball.setY(Gdx.graphics.getHeight() / 2);
		leftPaddle.setY(Gdx.graphics.getHeight() / 2 - leftPaddle.getHeight() / 2);
		rightPaddle.setY(Gdx.graphics.getHeight() / 2 - rightPaddle.getHeight() / 2);
	}
	private void drawScores() {
		Batch batch = new SpriteBatch();
		batch.begin();

		font.setColor(Color.BLUE);
		font.getData().setScale(10);
		font.draw(batch, String.valueOf(leftPlayerScore), Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() - 50);
		font.draw(batch, String.valueOf(rightPlayerScore), 3 * Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() - 50);

		batch.end();
		batch.dispose();
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}
}