package com.krynju;

import com.krynju.enums.Direction;
import com.krynju.modules.Field;
import com.krynju.modules.Tile;

import java.util.LinkedList;

class element {
    element(int x, int y, int c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    int x, y;
    int c;
}

public class AI {
    private Model model;
    private boolean flag = false;


    public AI(Model model) {
        this.model = model;
    }

    public void test() {
        if(model.player.getTileCordX() == model.enemy.getTileCordX()
                && model.player.getTileCordY() == model.enemy.getTileCordY())
            return;

        boolean flaggy = false;
        LinkedList<element> list = new LinkedList<element>();
        LinkedList<element> templist = new LinkedList<element>();
        LinkedList<element> removelist = new LinkedList<element>();

        list.add(new element(model.player.getTileCordX(), model.player.getTileCordY(), 0));

        int i = 0;
        while (true) {
            try {
                templist.add(new element(list.get(i).x, list.get(i).y + 1, list.get(i).c + 1));
                templist.add(new element(list.get(i).x - 1, list.get(i).y, list.get(i).c + 1));
                templist.add(new element(list.get(i).x + 1, list.get(i).y, list.get(i).c + 1));
                templist.add(new element(list.get(i).x, list.get(i).y - 1, list.get(i).c + 1));
            }catch(Exception eg){
                i--;
                break;
            }

            for (element obj : templist) {
                Tile ref;
                if (obj.x < 0 || obj.y < 0 || obj.x >= Field.tileCountX || obj.y >= Field.tileCountY) {
                    removelist.add(obj);
                    continue;
                } else
                    ref = Field.getTileRef(obj.x, obj.y);

                if (ref.isWallOnTile() || ref.isBombed()) {
                    removelist.add(obj);
                    continue;
                }
                if (ref.isBombed()) {
                    removelist.add(obj);
                    continue;
                }

                for (element obj2 : list) {
                    if (obj2.x == obj.x && obj2.y == obj.y && obj2.c <= obj.c) {
                        removelist.add(obj);
                        break;
                    }
                }
            }


            templist.removeAll(removelist);


            list.addAll(templist);

            for(element obj:templist){
                if(obj.x == model.enemy.getTileCordX() && obj.y == model.enemy.getTileCordY())
                    flaggy = true;
            }

            if(flaggy)
                break;

            removelist.clear();
                templist.clear();

            i++;
        }


        element g = list.get(i);
        try {
            if (g.x > model.enemy.getTileCordX())
                model.enemy.move(Direction.right);
            else if (g.x < model.enemy.getTileCordX())
                model.enemy.move(Direction.left);
            else if (g.y > model.enemy.getTileCordY())
                model.enemy.move(Direction.down);
            else if (g.y < model.enemy.getTileCordY())
                model.enemy.move(Direction.up);
        }
        catch(Exception e){}


        for(element e :list){
            System.out.print(e.x);
            System.out.print(e.y);
            System.out.print(e.c);
            System.out.println();
        }

        flag = true;
    }
}
