package com.krynju;

import com.krynju.enums.ObjectID;
import com.krynju.modules.Bomb;
import com.krynju.modules.DestroyableWall;
import com.krynju.modules.Field;
import com.krynju.modules.GameObject;
import com.krynju.modules.NormalWall;
import com.krynju.modules.Player;

import java.util.LinkedList;

/**
 * Class managing all the gameObjects. It contains useful object references and a list of all objects.
 * Also supports reloading the whole game at any time
 */
public class Model {
    /**
     * player object reference
     *
     * @see Player
     */
    public Player player;
    /**
     * enemy object reference
     *
     * @see Player
     */
    public Player enemy;

    /**
     * List of all objects, useful for updating position and rendering
     *
     * @see GameObject
     */
    public LinkedList<GameObject> objectList = new LinkedList<>();
    /**
     * Controller reference
     *
     * @see Controller
     */
    private Controller controller;

    /**
     * Constructor assigns the controller and sends a reference to itself to the controller.
     * Then it loads all the GameObjects necessary
     *
     * @see Controller
     */
    Model(Controller controller) {
        this.controller = controller;
        controller.addModel(this);
        load();
    }

    /**
     * Loads all the necessary objects
     */
    private void load() {
        /*initiate tiles*/
        new Field();
        /*player*/
        Bomb bomb = new Bomb(controller);
        player = new Player(ObjectID.player, 0, 0, Game.playerColor, bomb);
        /*AI*/
        Bomb bomb2 = new Bomb(controller);
        enemy = new Player(ObjectID.enemy, Field.tileCountX - 1, Field.tileCountY - 1, Game.enemyColor, bomb2);
        /*adding objects to the list*/
        objectList.add(player);
        objectList.add(enemy);
        objectList.add(bomb);
        objectList.add(bomb2);

        /*walls*/
        /*non destroyable walls*/
        for (int i = 1; i < Field.tileCountX; i += 2) {
            for (int j = 1; j < Field.tileCountY; j += 2) {
                objectList.add(new NormalWall(i, j));
            }
        }

        /*destroyable walls*/
        for (int i = 2; i < Field.tileCountX; i += 2) {
            for (int j = 0; j < Field.tileCountY; j += 2) {
                objectList.add(new DestroyableWall(i - 1, j));
            }
        }

        for (int i = 2; i < Field.tileCountX - 1; i += 2) {
            for (int j = 1; j < Field.tileCountY; j += 2) {
                objectList.add(new DestroyableWall(i, j));
            }
        }
    }

    /**
     * Function that clears the list of the objects and then loads fresh new objects again
     */
    public void reload() {
        objectList.clear();
        load();
    }
}
