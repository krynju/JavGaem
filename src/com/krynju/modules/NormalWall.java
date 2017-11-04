package com.krynju.modules;

import java.awt.*;

public class NormalWall extends Wall{
    public NormalWall(int x, int y) {
        super(x, y, 0,0);
        destroyable = false;
    }

    @Override
    public void destroy() {}

    @Override
    public void tick(double timeElapsedSeconds) {}

    @Override
    public void render(Graphics g) {

            g.setColor(Color.DARK_GRAY);
                    g.fillRect((int) x, (int) y, 40, 40);

    }
}
