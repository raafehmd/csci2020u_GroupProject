![image](https://user-images.githubusercontent.com/90470871/163510395-e5744223-d4c2-4bc3-90fe-b203b81a4231.png)


# Overview
Tic Tac & Chat is an application where you can play Tic Tac Toe against another player and chat with your friends! The application features a drop-down menu in which you can make use of those problem-solving skills, then boast about your game win afterwards.

# Defining Moves...
![image](https://user-images.githubusercontent.com/90470871/163513042-0ff8cca7-9a75-42b8-bccb-946b8d21d97a.png)



# How to Run
As shown below, to run the project you must first open the 'Run/Debug configurations' dialog for GameApp and change the ipv4 address in the program arguments to the ipv4 address of your device. Then you need to run the Server application once and then run two instances of the GameApp application.

https://user-images.githubusercontent.com/90229554/163505643-29487a26-4efc-4810-a20e-098403e9a122.mp4

The following gif showcases the menubar and it's functions for both the game and the chat. 

![2020U-p2-g](https://user-images.githubusercontent.com/90229554/163505957-e18e3d8d-2d5d-4d90-b574-ce616d0c116d.gif)

Implementation:
1. Press alt + shift + f9 to go to 'Edit Configuations'
2. Select the 'GameApp' class
3. Under 'Build and run', enter your IPv4 address as a String input in the box 'CLI arguments to your application' (NOTE: an IPv4 address uniquely identifies a network interface on a machine and is required to run the program).
4. Ensure that the selected class has multiple instances enabled (Click 'Modify options' then select 'Allow multiple instances')
5. Run Server.java
6. Run two instances of GameApp.java 
7. Switch between Tic Tac & Chat game and a Chat window!

