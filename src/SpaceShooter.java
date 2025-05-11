import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpaceShooter extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static int numLives = 3;

    private int score;
    private boolean bossExists;
    private boolean gameRunning;

    private List<GameObject> gameObjects;
    private Player player;
    private Pane root;
    private Scene scene;
    private GraphicsContext gc;

    private Label scoreLabel;
    private Image heartImage;
    private Image backgroundImage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        scoreLabel = new Label("Score: 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);

        root.getChildren().add(scoreLabel);

        scene = new Scene(root, WIDTH, HEIGHT, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Space Shooter");
        primaryStage.show();

        // Tải ảnh nền
        try {
            backgroundImage = new Image("file:res/background.png");
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh nền: " + e.getMessage());
            backgroundImage = null; // Fallback nếu không tải được ảnh
        }
        // Tải ảnh trái tim
        try {
            heartImage = new Image("file:res/heart.png");
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh heart: " + e.getMessage());
            heartImage = null; // Fallback nếu không tải được ảnh
        }      

        initGame();
        initEventHandlers();

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        gameLoop.start();
    }

    private void initGame() {
        gameObjects = new ArrayList<>();
        player = new Player(WIDTH / 2, HEIGHT - 50);
        gameObjects.add(player);
        score = 0;
        bossExists = false;
        gameRunning = true;
    }

    private void initEventHandlers() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT -> player.setMoveLeft(true);
                case RIGHT -> player.setMoveRight(true);
                case UP -> player.setMoveForward(true);
                case DOWN -> player.setMoveBackward(true);
                case SPACE -> player.shoot(gameObjects);
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case LEFT -> player.setMoveLeft(false);
                case RIGHT -> player.setMoveRight(false);
                case UP -> player.setMoveForward(false);
                case DOWN -> player.setMoveBackward(false);
            }
        });
    }

private void update() {
    if (!gameRunning) return;

    // Cập nhật tất cả các đối tượng
    for (GameObject obj : gameObjects) {
        obj.update();

        // Kiểm tra nếu Enemy vượt quá đáy khung hình
        if (obj instanceof Enemy && obj.getY() > HEIGHT) {
            obj.setDead(true); // Đánh dấu Enemy là "dead"
            numLives--; // Giảm số mạng
        }
    }

    // Kiểm tra va chạm
    checkCollisions();

    // Loại bỏ các đối tượng "dead"
    gameObjects.removeIf(GameObject::isDead);

    // Sinh thêm kẻ địch hoặc boss
    spawnEnemy();
    if (score >= 100 && !bossExists) {
        spawnBossEnemy();
    }

    // Cập nhật UI
    scoreLabel.setText("Score: " + score);

    // Kiểm tra nếu hết mạng
    if (numLives <= 0) {
        resetGame();
    }
}

    private void render() {
        // Vẽ ảnh nền
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT); // Vẽ ảnh nền toàn màn hình
        } else {
            gc.setFill(Color.BLACK); // Nếu không tải được ảnh, vẽ nền màu đen
            gc.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // Vẽ các đối tượng trong trò chơi
        for (GameObject obj : gameObjects) {
            obj.render(gc);
        }

        // Vẽ số mạng bằng hình ảnh trái tim
        drawLives();
    }

    private void drawLives() {
        // Vẽ chữ "Lives:" ở góc trên bên phải
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(20)); // Đặt font chữ
        gc.fillText("Lives:", WIDTH - 200, 30); // Dịch chữ "Lives:" sang trái một chút

        // Vẽ hình ảnh trái tim bên phải chữ "Lives:"
        if (heartImage != null) {
            for (int i = 0; i < numLives; i++) {
                gc.drawImage(heartImage, WIDTH - 140 + i * 40, 10, 20, 20); // Dịch trái tim sang trái một chút
            }
        } else {
            // Nếu không tải được ảnh, hiển thị số mạng bằng chữ
            gc.fillText(String.valueOf(numLives), WIDTH - 140, 30);
        }
    }

private void spawnEnemy() {
    if (Math.random() < 0.005) { // Giảm xác suất sinh kẻ địch
        double x = Math.random() * (WIDTH - Enemy.WIDTH) + Enemy.WIDTH / 2;
        gameObjects.add(new Enemy(x, -Enemy.HEIGHT / 2));
    }
}

    private void spawnBossEnemy() {
        bossExists = true;
        gameObjects.add(new BossEnemy(WIDTH / 2, -BossEnemy.HEIGHT / 2));
    }

    private void checkCollisions() {
        for (GameObject obj1 : gameObjects) {
            for (GameObject obj2 : gameObjects) {
                if (obj1 != obj2 && obj1.getBounds().intersects(obj2.getBounds())) {
                    if (obj1 instanceof Bullet && obj2 instanceof Enemy) {
                        obj1.setDead(true);
                        obj2.setDead(true);
                        score += 10;
                    } else if (obj1 instanceof Player && obj2 instanceof Enemy) {
                        obj2.setDead(true);
                        numLives--;
                    } else if (obj1 instanceof Bullet && obj2 instanceof BossEnemy) {
                        Bullet bullet = (Bullet) obj1;
                        BossEnemy boss = (BossEnemy) obj2;

                        // Gây sát thương cho boss
                        boss.takeDamage(bullet.getDamage());
                        bullet.setDead(true);

                        // Nếu boss chết, tăng điểm và đánh dấu boss không còn tồn tại
                        if (boss.isDead()) {
                            score += 50;
                            bossExists = false;
                        }
                    }
                }
            }
        }
    }

    private void resetGame() {
        gameRunning = false;
        System.out.println("Game Over! Final Score: " + score);
    }
}