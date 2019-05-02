package com.example.myapplication.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.myapplication.classes.Coordinate;
import com.example.myapplication.enums.Direction;
import com.example.myapplication.enums.GameState;
import com.example.myapplication.enums.TileType;

public class GameEngine {
    public static final int GameWidth = 30; //Width of the game window
    public static final int GameHeight = 60;//Height of the game window
    public static int updateDelay = 150; //Determines the speed of snake

    private List<Coordinate> walls = new ArrayList<>(); // Creates wall objects
    private List<Coordinate> snake = new ArrayList<>(); // Creates the snake object
    private List<Coordinate> foods = new ArrayList<>(); // Creates the food object
    private List<Coordinate> snacks = new ArrayList<>(); // Creates the snack object

    private Random random = new Random(); // Creates a random variable
    private boolean increaseTail = false;
    private int increaseWall = 0; // Determines if a new wall should be added
    public int score = 0; //Keeps the score count
    public int stopGrowing = 0; // Stop the snake from growing after a point
    public int stopWall = 0; // Stops spawning new walls
    public boolean increaseSpeed = false;

    private Direction currentDirection = Direction.Right; // Snake moves east initially
    private GameState currentGameState = GameState.Running; // Game starts



    private Coordinate getSnakeHead(){ // Finds the location of the head
        return snake.get(0); // Gets the location of the head of the snake
    }

    public GameEngine(){ // Creates the engine of the game
    }

    public void initGame(){
        currentGameState = GameState.Running;
        AddSnake(); // Creates the snake object
        AddWalls(); // Creates the walls around the phone
        AddFoods(); // Adds foods
        AddSnacks();// Adds snacks
    }

    public void Update(){ // This function allows the snake to move around
        switch (currentDirection){
            case Up:
                UpdateSnake(0,-1); // North will be -1 in the y direction
                break;
            case Right:
                UpdateSnake(1,0); // East will be 1 in the x direction
                break;
            case Down:
                UpdateSnake(0,1); // South will be 1 in the y direction
                break;
            case Left:
                UpdateSnake(-1,0); // West will be -1 in the x direction
                break;
        }
        // Check wall collisions
        for(Coordinate w: walls){
            if(snake.get(0).equals(w)){
                currentGameState = GameState.Lost;
            }
        }

        for(int i = 1; i < snake.size(); i++){ // Check for self collision. Starts with 1 since 0 is the head
            if(getSnakeHead().equals(snake.get(i))){ // Snake collision to itself
                currentGameState = GameState.Lost; // Game lost
                return;
            }
        }

        //Check foods
        Coordinate foodToRemove = null;
        for(Coordinate food : foods){
            if(getSnakeHead().equals(food)){
                foodToRemove = food;
                increaseTail = true;
                score += 1; // Each food is worth 1 point
                if(increaseSpeed) {
                    updateDelay = 150;
                    increaseSpeed = false;
                }
            }
        }
        if(foodToRemove != null){
            foods.remove(foodToRemove); // Removes the eaten food
            AddFoods();  // Adds a new food
            increaseWall++;
        }

        Coordinate snackToRemove = null;
        for(Coordinate snack : snacks){
            if(getSnakeHead().equals(snack)){
                snackToRemove = snack;
                updateDelay = 100; // snack changes the speed of snake
                increaseSpeed = true;
            }
        }
        if(snackToRemove != null){
            snacks.remove(snackToRemove); // Removes the eaten snack
            AddSnacks();  // Adds a new snack
        }
    }

    private void UpdateSnake(int x, int y){ // Updates the game after they delay
        int newX = snake.get(snake.size() - 1).getX(); // Determines the x position of the added body
        int newY = snake.get(snake.size() - 1).getY(); // Determines the y position of the new body

        for(int i = snake.size() -1; i > 0; i--) // Moves the snake
        {
            snake.get(i).setX(snake.get(i-1).getX()); // Changes the x position of the body
            snake.get(i).setY(snake.get(i-1).getY()); // Changes the y position of the body
        }

        if(stopGrowing < 10) { // Stops the snake from growing after eating 8 foods
            if (increaseTail) { // Increases the tail after moving the snake body
                snake.add(new Coordinate(newX, newY)); // Adds the new snake body at the newx, new y position
                increaseTail = false; // Restart the growing
                stopGrowing++;
            }
        }

        if(stopWall < 5) { // Stops the amount of walls to keep going forever
            if (increaseWall == 2) //When the snake eats n number of foods, the number of walls increase
            {
                increaseWall = 0; // Resets the parameter
                stopWall++;
                for (int i = 0; i < 4; i++) // Adds i amount of walls
                    randomWall(); // Adds the new random wall into the game
            }
        }


        snake.get(0).setX(snake.get(0).getX() + x); // Declares the head coordinate
        snake.get(0).setY(snake.get(0).getY() + y); // Declares the head coordinate
    }

    private void AddSnake(){ // Makes a snake with 5 elements
        snake.clear();
        snake.add(new Coordinate(17,30)); //Location of the head of snake
        snake.add(new Coordinate(16,30));
        snake.add(new Coordinate(15,30));
        snake.add(new Coordinate(14,30));
        snake.add(new Coordinate(13,30));
    }

    private void AddWalls(){
        //Top and Bottom Walls
        for(int x = 1; x < GameWidth; x++)
        {
            walls.add(new Coordinate(x,1)); // Creates a new wall object
            walls.add(new Coordinate(x,GameHeight - 1)); // Creates a new wall object
        }
        //Left and Right Walls
        for(int y = 1; y < GameHeight; y++)
        {
            walls.add(new Coordinate(1,y)); // Creates a new wall object
            walls.add(new Coordinate(GameWidth -1, y));

        }

    }

    public TileType[][] getMap(){ // Creates the map of the game
        TileType[][] map = new TileType[GameWidth][GameHeight];

        for(int x = 0; x < GameWidth; x++)
        {
            for(int y = 0; y < GameHeight; y++)
            {
                map[x][y] = TileType.Nothing; // Empty space added
            }
        }
        for(Coordinate s : snake){ // Declares the body of the snake as a snake tail
            map[s.getX()][s.getY()] = TileType.SnakeTail;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] =  TileType.SnakeHead; // The first to be head

        for(Coordinate wall: walls)
        {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for(Coordinate a: foods){
            map[a.getX()][a.getY()] = TileType.Food; // Declares the food object
        }

        for(Coordinate b: snacks){
            map[b.getX()][b.getY()] = TileType.Snack; // Declares the snack object
        }
        return map;
    }

    public void UpdateDirection(Direction newDirection){ // Updates the direction of the snake
        if(Math.abs(newDirection.ordinal()-currentDirection.ordinal()) % 2 == 1){
            currentDirection = newDirection; // The current direction is the new direction
        }
    }

    private void randomWall(){ //Add random walls to the game after the parameter is met
        Coordinate coordinate = null;
        boolean added = false; // Decides when to add the food

        while(!added) { // Prevents an wall from spawning inside the snake or another wall
            int x = 1 + random.nextInt(GameWidth - 2); // Away from the outer walls
            int y = 1 + random.nextInt(GameHeight - 2); // Away from the outer walls

            coordinate = new Coordinate(x,y);
            boolean collision = false; // Declares boolean variable

            for(Coordinate s:snake){ // Checks if the new wall is inside the snake
                if(s.equals(coordinate)){
                    collision = true;
                    break;
                }
            }

            for(Coordinate a:foods){ // Checks if the new wall is inside an food
                if(a.equals(coordinate)){
                    collision = true;
                    break;
                }
                for(Coordinate w:walls){ // Checks if there is a wall
                    if(w.equals(coordinate)){
                        collision = true;
                        break;
                    }
                }
            }

            for(Coordinate b:snacks){ // Checks if the new wall is inside an snack
                if(b.equals(coordinate)){
                    collision = true;
                    break;
                }
                for(Coordinate w:walls){ // Checks if there is a wall
                    if(w.equals(coordinate)){
                        collision = true;
                        break;
                    }
                }
            }
            added = !collision; // Keep going until an empty or already declared wall is found
        }
        walls.add(coordinate); // Adds the random wall to the map

    }
    private void AddFoods(){ // Adds food to the game
        Coordinate coordinate = null;

        boolean added = false;

        while(!added) { // Prevents an food from spawning inside the snake or wall
            int x = 2 + random.nextInt(GameWidth - 2);
            int y = 2 + random.nextInt(GameHeight - 2);

            coordinate = new Coordinate(x,y);
            boolean collision = false; // Declares boolean variable

            for(Coordinate s:snake){
                if(s.equals(coordinate)){
                    collision = true; // Snake eats the food
                    break;
                }
            }

            for(Coordinate a:foods){
                if(a.equals(coordinate)){
                    collision = true;
                    break;
                }
            }
            for(Coordinate w:walls){ // Checks if there is a wall
                if(w.equals(coordinate)){
                    collision = true;
                    break;
                }
            }
            added = !collision;
        }
        foods.add(coordinate); // Adds an food to the map
    }

    private void AddSnacks(){ // Adds snack to the game
        Coordinate coordinate = null;

        boolean added = false;

        while(!added) { // Prevents an snack from spawning inside the snake or wall
            int x = 2 + random.nextInt(GameWidth - 2);
            int y = 2 + random.nextInt(GameHeight - 17);

            coordinate = new Coordinate(x,y);
            boolean collision = false; // Declares boolean variable

            for(Coordinate s:snake){
                if(s.equals(coordinate)){
                    collision = true; // Snake eats the snack
                    break;
                }
            }

            for(Coordinate b:snacks){
                if(b.equals(coordinate)){
                    collision = true;
                    break;
                }
            }
            for(Coordinate w:walls){ // Checks if there is a wall
                if(w.equals(coordinate)){
                    collision = true;
                    break;
                }
            }
            added = !collision;
        }
        snacks.add(coordinate); // Adds an snack to the map
    }

    public GameState getCurrentGameState(){ // Gets the current game state
        return currentGameState;
    }

}
