import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

public class Enemy extends GameObject {

    protected static final int WIDTH = 40;
    protected static final int HEIGHT = 40;

    public static double SPEED = 1;

    private boolean dead;
    private Image enemyImage;

    public Enemy(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        this.dead = false;

        //lấy ảnh lên
        this.enemyImage = new Image("file:res/enemy.png");
    }

    @Override
    public void update() {
        y += SPEED;

        // Nếu ra khỏi màn hình (giả sử cao 600), thì đánh dấu là chết
        if (y - HEIGHT / 2 > 600) {
            this.dead = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (enemyImage != null) {
            // Vẽ ảnh enemy
            gc.drawImage(enemyImage, x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        } else {
            // Nếu không tải được ảnh, vẽ hình chữ nhật thay thế
            gc.setFill(Color.RED);
            gc.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        }
    }

    @Override
    public double getWidth() {
        return WIDTH;
    }

    @Override
    public double getHeight() {
        return HEIGHT;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public boolean isDead() {
        return dead;
    }
}
