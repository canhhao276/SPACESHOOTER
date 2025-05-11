import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EnemyBullet extends GameObject {

    public static final int WIDTH = 4;
    public static final int HEIGHT = 20;

    private static final double SPEED = 3;

    private boolean dead;

    public EnemyBullet(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        this.dead = false;
    }

    @Override
    public void update() {
        y += SPEED;

        // Nếu ra khỏi màn hình dưới thì đánh dấu bị loại
        if (y - HEIGHT / 2 > 600) { // giả sử chiều cao màn hình là 600
            this.dead = true;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    @Override
    public double getWidth() {
        return WIDTH;
    }

    @Override
    public double getHeight() {
        return HEIGHT;
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
