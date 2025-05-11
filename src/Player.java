import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;
import javafx.scene.image.Image;

public class Player extends GameObject {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 80;
    private static final double SPEED = 4;

    private boolean moveLeft;
    private boolean moveRight;
    private boolean moveForward;
    private boolean moveBackward;

    private int health;
    private boolean dead;

    private Image playerImage;

    public Player(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        this.health = 3; // mặc định 3 mạng
        this.dead = false;

        //tải ảnh lên
        this.playerImage = new Image("file:res/player.png");
    }

    @Override
    public double getWidth() {
        return WIDTH;
    }

    @Override
    public double getHeight() {
        return HEIGHT;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            this.dead = true;
        }
    }

    @Override
    public void update() {
        if (moveLeft) x -= SPEED;
        if (moveRight) x += SPEED;
        if (moveForward) y -= SPEED;
        if (moveBackward) y += SPEED;

        // Giới hạn trong màn hình (giả sử kích thước 800x600)
        x = Math.max(getWidth() / 2, Math.min(800 - getWidth() / 2, x));
        y = Math.max(getHeight() / 2, Math.min(600 - getHeight() / 2, y));
    }

    @Override
    public void render(GraphicsContext gc) {
        if (playerImage != null) {
            // Vẽ ảnh player
            gc.drawImage(playerImage, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        } else {
            // Nếu không tải được ảnh, vẽ hình chữ nhật thay thế
            gc.setFill(Color.CYAN);
            gc.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        }

        // Vẽ máu (chữ nhỏ phía trên)
        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + health, x - 15, y - HEIGHT / 2 - 5);
    }

    public void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setMoveForward(boolean moveForward) {
        this.moveForward = moveForward;
    }

    public void setMoveBackward(boolean moveBackward) {
        this.moveBackward = moveBackward;
    }

    public void shoot(List<GameObject> newObjects) {
        Bullet bullet = new Bullet(x, y - HEIGHT / 2);
        newObjects.add(bullet);
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}



