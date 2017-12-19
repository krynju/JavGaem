package com.krynju.ai;

/**
 * Node class used in AI algorithms. It represents a single step in the path finding algorithm
 *
 * @see AI
 */
public class Node {
    /**
     * X cord of the tile the movement goes to
     */
    int x;
    /**
     * Y cord of the tile the movement goes to
     */
    int y;
    /**
     * Node's step number
     */
    int step;
    /**
     * Counter used to represent how many walls get destroyed if a bomb is placed on this tile
     * Used in the AI findBreakableWallsAlgorithm
     *
     * @see AI#findBreakableWallsAlgorithm()
     */
    int destroyableWallsCount = 0;

    /**
     * Reference to the node from which this node got found
     *
     * @see AI#runAwayFromBombAlgorithm()
     */
    Node previousNode;


    /**
     * Constructor used in all algorithms except the runAwayFromBombAlgorithm
     *
     * @param x    cord of the node
     * @param y    cord of the node
     * @param step represents how many steps down the path the algorithm is
     */
    Node(int x, int y, int step) {
        this.x = x;
        this.y = y;
        this.step = step;
    }

    /**
     * Constructor used in the runAwayFromBombAlgorithm
     *
     * @param x    cord of the node
     * @param y    cord of the node
     * @param step represents how many steps down the path the algorithm is
     * @param prev node from the previous step
     * @see AI#runAwayFromBombAlgorithm()
     */
    Node(int x, int y, int step, Node prev) {
        this.x = x;
        this.y = y;
        this.step = step;
        this.previousNode = prev;
    }
}
