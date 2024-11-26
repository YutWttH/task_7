
import javax.swing.*;
import java.awt.*;
import java.awt.image.MemoryImageSource;

public class SectorFiller {
    private int imageSize;
    private Point center;
    private int startAngle;
    private int endAngle;
    private Color startColor;
    private Color endColor;

    public SectorFiller(int imageSize, Point center, int startAngle, int endAngle, Color startColor, Color endColor) {
        this.imageSize = imageSize;
        this.center = center;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public Image fillSector() {
        int[] pixels = new int[imageSize * imageSize];
        int radius = Math.min(center.x, center.y);

        for (int y = 0; y < imageSize; y++) {
            for (int x = 0; x < imageSize; x++) {
                int dx = x - center.x;
                int dy = y - center.y;
                double distance = Math.hypot(dx, dy);

                if (distance <= radius) {
                    double angle = Math.toDegrees(Math.atan2(dy, dx));
                    if (angle < 0) angle += 360;

                    if (angle >= startAngle && angle <= endAngle) {
                        float ratio = (float) (distance / radius);
                        Color interpolatedColor = interpolateColor(startColor, endColor, ratio);
                        pixels[y * imageSize + x] = interpolatedColor.getRGB();
                    } else {
                        pixels[y * imageSize + x] = Color.WHITE.getRGB();
                    }
                } else {
                    pixels[y * imageSize + x] = Color.WHITE.getRGB();
                }
            }
        }

        return Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(imageSize, imageSize, pixels, 0, imageSize));
    }

    private Color interpolateColor(Color startColor, Color endColor, float ratio) {
        int r = (int) (startColor.getRed() + ratio * (endColor.getRed() - startColor.getRed()));
        int g = (int) (startColor.getGreen() + ratio * (endColor.getGreen() - startColor.getGreen()));
        int b = (int) (startColor.getBlue() + ratio * (endColor.getBlue() - startColor.getBlue()));
        return new Color(r, g, b);
    }

    public static void main(String[] args) {
        int imageSize = 400;
        Point center = new Point(200, 200);
        int startAngle = 0;
        int endAngle = 90;
        Color startColor = Color.RED;
        Color endColor = Color.BLUE;

        SectorFiller sectorFiller = new SectorFiller(imageSize, center, startAngle, endAngle, startColor, endColor);
        Image image = sectorFiller.fillSector();

        // Display the image in a JFrame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(imageSize, imageSize);
        frame.add(new JLabel(new ImageIcon(image)));
        frame.setVisible(true);
    }
}