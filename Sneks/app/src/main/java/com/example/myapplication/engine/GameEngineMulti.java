package com.example.myapplication.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.myapplication.classes.Coordinate;
import com.example.myapplication.enums.Direction;
import com.example.myapplication.enums.GameState;
import com.example.myapplication.enums.TileType;

public class GameEngineMulti {
    public static final int GameWidth = 40; //Width of the game window
    public static final int GameHeight = 80;//Height of the game window

    private List<Coordinate> walls = new ArrayList<>(); // Creates wall objects
    private List<Coordinate> snake = new ArrayList<>(); // Creates the snake object
    private List<Coordinate> foods = new ArrayList<>(); // Creates the food object
    private List<Coordinate> snake2 = new ArrayList<>(); // Creates the second snake object

    private Random random = new Random(); // Creates a random variable
    private boolean increaseTail = false; // Declares boolean variable to grow tail for the first snake
    private boolean increaseTail2 = false; // Declares boolean variable to grow tail for the second snake
    private int increaseWall = 0; // Determines if a new wall should be added
    public int score = 0; //Keeps the score count
    public int score2 = 0; // Keeps the score count for the second snake
    private int gameIndex;
    private Direction currentDirection = Direction.Right; // Snake moves east initially
    private  Direction currentDirection2 = Direction.Right; // 2nd snakes moves east initially

    private GameState currentGameState = GameState.Running; // Game starts

    private Coordinate getSnakeHead(){ // Finds the location of the head
        return snake.get(0); // Gets the location of the head of the snake
    }
    private Coordinate getSnakeHead2(){ // Finds the location of the second head
        return snake2.get(0); // Gets the location of the head of the second snake
    }

    public GameEngineMulti(){ // Creates the engine of the game to accessible to other activities

    }
    public void initGame(){
        currentGameState = GameState.Running;
        AddSnakes(); // Creates the two snakes objects
        AddWalls(); // Creates the walls around the phone
        AddFoods(); // Adds one food
        AddFoods(); // Adds another food
    }

    public void Update(){ // This function updates the game
        switch (currentDirection){ // Moves the first snake
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

        switch (currentDirection2){ // Moves the second snake with the update
            case Up:
                UpdateSnake2(0,-1);
                break;
            case Right:
                UpdateSnake2(1,0);
                break;
            case Down:
                UpdateSnake2(0,1);
                break;
            case Left:
                UpdateSnake2(-1,0); // West will be -1 in the x direction
                break;
        }

        // Check wall collisions
        for(Coordinate w: walls){
            if(snake.get(0).equals(w)){ // If the third snake lose
                gameIndex=1+ gameIndex;
            }
            if(snake2.get(0).equals(w)){ // If the second snake lose
                gameIndex=2+ gameIndex;
            }
            if(gameIndex==1) {
                currentGameState = GameState.Lost;
            }
            if(gameIndex==2){
                currentGameState = GameState.Lost2;
            }
            if(gameIndex==3){
                currentGameState = GameState.Draw;
            }
        }

        for(int i = 1; i < snake.size(); i++){ // Check for self collision. Starts with 1 since 0 is the head
            if(getSnakeHead().equals(snake.get(i))){ // Snake collision to itself
                currentGameState = GameState.Lost; // Game lost
                return;
            }
        }

        for(int i = 1; i < snake2.size(); i++){ // Same as above, but for the second snake
            if(getSnakeHead2().equals(snake2.get(i))){ // Snake collision to itself
                currentGameState = GameState.Lost2; // Game lost
                return;
            }
        }

        for(int i = 1; i < snake2.size(); i++){ // Checks if the first snake hits the body of the other snake
            if(getSnakeHead().equals(snake2.get(i))){
                currentGameState = GameState.Lost; // Snake one loses
                return;
            }
        }

        for(int i = 1; i < snake.size(); i++){ // Checks if the 2nd snake hits the body of the other snake
            if(getSnakeHead2().equals(snake.get(i))){
                currentGameState = GameState.Lost; // Snake two loses
                return;
            }
        }

        if(getSnakeHead2().equals(getSnakeHead())) // It's a draw
        {
            currentGameState = GameState.Draw;
            return;
        }

        //Check foods
        Coordinate foodToRemove = null;
        for(Coordinate food : foods){ // Checks if the first snake eats an food
            if(getSnakeHead().equals(food)){ // If the head of the snake and food share a tile
                foodToRemove = food; // Remove the food
                increaseTail = true; // Increase the tail
                score += 1; // Increase the score of the first snake
            }
        }
        for(Coordinate food : foods){ // Checks if the second snake eats an food
            if(getSnakeHead2().equals(food)){ // If the head of the snake and food share a tile
                foodToRemove = food; // Remove the food
                increaseTail2 = true; // Increase the tail of the second snake
                score2 += 1; // Increase the score of the second snake
            }
        }

        if(foodToRemove != null){
            foods.remove(foodToRemove); // Removes the eaten food
            AddFoods();  // Adds a new food
            increaseWall++; // Increases the counter to add another wall set
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

        if(increaseTail){ // Increases the tail after moving the snake body
            snake.add(new Coordinate(newX, newY)); // Adds the new snake body at the newx, new y position
            increaseTail = false; // Restart the growing

        }

        snake.get(0).setX(snake.get(0).getX() + x); // Declares the head  coordinate
        snake.get(0).setY(snake.get(0).getY() + y); // Declares the head coordinate
    }

    private void UpdateSnake2(int x, int y){ // Repeat of the code above, but for second snake
        int newX = snake2.get(snake2.size() - 1).getX(); // Determines the x position of the added body
        int newY = snake2.get(snake2.size() - 1).getY(); // Determines the y position of the new body

        for(int i = snake2.size() -1; i > 0; i--) // Moves the snake
        {
            snake2.get(i).setX(snake2.get(i-1).getX()); // Changes the x position of the body
            snake2.get(i).setY(snake2.get(i-1).getY()); // Changes the y position of the body
        }

        if(increaseTail2){ // Increases the tail after moving the snake body
            snake2.add(new Coordinate(newX, newY)); // Adds the new snake body at the newx, new y position
            increaseTail2 = false; // Restart the growing

        }
        snake2.get(0).setX(snake2.get(0).getX() + x); // Declares the head  coordinate
        snake2.get(0).setY(snake2.get(0).getY() + y); // Declares the head coordinate
    }

    private void AddSnakes(){ // Makes a snake with 5 elements
        snake.clear(); // Clears of previous snakes
        snake2.clear(); // Repeats above for the second snake

        snake.add(new Coordinate(7,28)); //Location of the snake
        snake.add(new Coordinate(6,28));
        snake.add(new Coordinate(5,28));
        snake.add(new Coordinate(4,28));
        snake.add(new Coordinate(3,28));

        snake2.add(new Coordinate(7, GameHeight-27)); // Location of the second snake
        snake2.add(new Coordinate(6, GameHeight-27));
        snake2.add(new Coordinate(5, GameHeight-27));
        snake2.add(new Coordinate(4, GameHeight-27));
        snake2.add(new Coordinate(3, GameHeight-27));
    }

    private void AddWalls(){
        //Top and Bottom Walls
        for(int x = 1; x < GameWidth; x++)
        {
            walls.add(new Coordinate(x,21)); // Creates a new wall object
            walls.add(new Coordinate(x,GameHeight -20)); // Creates a new wall object
        }
        //Left and Right Walls
        for(int y = 21; y < (GameHeight-20); y++)
        {
            walls.add(new Coordinate(1,y)); // Creates a new wall object
            walls.add(new Coordinate(GameWidth - 1, y));

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
            map[s.getX()][s.getY()] = TileType.SnakeTail_P1;
        }
        map[snake.get(0).getX()][snake.get(0).getY()] =  TileType.SnakeHead_P1; // The first to be head

        for(Coordinate s2 : snake2){ // Declares the body of the second snake as a snake tail
            map[s2.getX()][s2.getY()] = TileType.SnakeTail_P2;
        }
        map[snake2.get(0).getX()][snake2.get(0).getY()] =  TileType.SnakeHead_P2; // The first to be head for the second snake

        for(Coordinate wall: walls)
        {
            map[wall.getX()][wall.getY()] = TileType.Wall;
        }

        for(Coordinate a: foods){
            map[a.getX()][a.getY()] = TileType.Food; // Declares the food object
        }
        return map;
    }

    public void UpdateDirection(Direction newDirection){ // Updates the direction of the snake
        if(currentDirection != newDirection){
            currentDirection = newDirection; // The current direction is the new direction
        }
    }

    public void UpdateDirection2(Direction newDirection){ // Updates the direction of the snake
        if(currentDirection2 != newDirection){
            currentDirection2 = newDirection; // The current direction is the new direction
        }
    }

    private void AddFoods(){ // Adds food to the game
        Coordinate coordinate = null;

        boolean added = false;

        while(!added) { // Prevents an food from spawning inside the snake or wall
            int x = 2 + random.nextInt(GameWidth - 2);
            int y;
            y = 21 + random.nextInt(GameHeight-42);

            coordinate = new Coordinate(x,y);
            boolean collision = false; // Declares boolean variable

            for(Coordinate s:snake){
                if(s.equals(coordinate)){ // If the snake body and new food are in the same tile
                    collision = true; // There is a collision
                    break; // Get out of the for loop and try again
                }
            }

            for(Coordinate s2:snake2){ // Repeat but with second snake
                if(s2.equals(coordinate)){ // If the snake body and new food are in the same tile
                    collision = true; // There is a collision
                    break; // Get out of the for loop and try again
                }
            }

            for(Coordinate a:foods){ // Do not spawn an food inside another food
                if(a.equals(coordinate)){
                    collision = true;
                    break;
                }
            }
            for(Coordinate w:walls){ // Checks if there is a wall
                if(w.equals(coordinate)){ // If there is a wall, do not spawn a food
                    collision = true;
                    break;
                }
            }
            added = !collision;
        }
        foods.add(coordinate); // Adds an food to the map
    }

    public GameState getCurrentGameState(){ // Gets the current game state
        return currentGameState;
    }
    public void setCurrentGameState(GameState current){ // Gets the current game state
        currentGameState = current;
    }
}

