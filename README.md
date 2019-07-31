# Nike

## Space Invaders

An android game: Space Invaders

By wikipedia, Space Invaders is a 1978 arcade game created by Tomohiro Nishikado.
The objective of the game is to shoot the invaders with  your laser base while
avoiding their shots and their invasion. The main goal is getting a score as
highest as possible by shooting down invaders.
For more info, see this [quick guide](https://www.classicgaming.cc/classics/space-invaders/play-guide).

A link to [Repo](https://github.com/ecs160ss12019/Nike)

A link to [Story Mapping(Acceptance test included)](https://github.com/ecs160ss12019/Nike/blob/master/StoryMapping.md)

A link to [CRC](https://github.com/ecs160ss12019/Nike/blob/master/CRC.md)

A link to our [design track (including updates)](https://github.com/ecs160ss12019/Nike/blob/master/DesignTrack.md)


## Team members

*Format: Name, Github Username*

Zhiyuan Guo,    danielmonster

Henry Yi,    Henry-YiW

Roberto Lozano,    robertolozano

Xuanchen Zhou,    PathaChow

Weili Yin,    CharlesYWL


## CRC


| SpaceGame | |
|---|---|
|  - UI of this game  <br> - Shows the graphical background of the game, which is the universe <br> - Contains a list of objects in the game |  BaseShelter <br> Invader <br> LaserBase <br> Missile |



| AnimatedObject | |
|---|---|
|  - Base class of every animated object created in the game that holds a view of object on the game screen and the SpaceGame object  <br> - Stores and manages view, animator, and handlers <br> - Handle notification issued by other objects <br> -Configure the animator listener | LaserBase <br> Invader <br> InvaderGroup <br> BaseShelter <br> BaseShelterGroup <br> HUD <br> Missle <br> SpaceGame |



| LaserBase | |
|---|---|
|  - Able to move right or left when user touches the left/right part of the screen <br> - Has 3 lives <br> - Able to shoot missle when user touch the middle screen on the screen |  Missle <br> SpaceGame |



| Invader | |
|---|---|
|  - Generates and Manages the movement and size of invaders <br> - Detect the collision with missiles <br> - Spawns at the top of interface |  Missle <br> SpaceGame |


| InvaderGroup | |
|---|---|
|  - Manage a group of invaders|  Invader |


| BaseShelterGroup | |
|---|---|
|  - Manage a group of baseShelters  <br> - Get the action signal from the outside world and then distribute it to every baseShelter  |  BaseShelter <br> SpaceGame |



| BaseShelter | |
|---|---|
|  - Has an array of ShelterBlocks that make up a baseShelter  <br> - Detect the collision with missiles <br> - Spawns at between invader and laserbase |  Missle <br> SpaceGame |


| ShelterBlock | |
|---|---|
|  - Make up each baseShelter  <br> - Each block should have height and width <br> - Detect whether a block is dead or not | BaseShelter |




| MissilePool | |
|---|---|
|  - Providing a pool of missiles available to use  <br> - Handle the case where one missile collides with another missile |  Missile <br> SpaceGame |



| Missile | |
|---|---|
|  - Controls the movement and size of missile  <br> - Emitted by invaders and laserbase with specified directions |  BaseShelter <br> Invader <br> LaserBase <br> SpaceGame |



| HUD | |
|---|---|
|  - Records information of status like Lifes, score, and level  <br> - Update them when variables change |  GameObject <br> SpaceGame <br> LaserBase |

| Lives | |
|---|---|
|  - A member in the HUD class that keeps track of the remaining number of lives  <br> - Manage the animation of live icons appearing on the top right corner of the screen |  HUD <br> SpaceGame |


## Screenshot

![A](https://github.com/ecs160ss12019/Nike/blob/master/screenshot/sprint2A.png)

![B](https://github.com/ecs160ss12019/Nike/blob/master/screenshot/sprint2B.png)

![C](https://github.com/ecs160ss12019/Nike/blob/master/screenshot/sprint2C.png)


## Reference:
@misc{Charles2013,
  author = {wasabeef},
  title = {Blurry},
  year = {2015-2019},
  publisher = {GitHub},
  journal = {GitHub repository},
  howpublished = {\url{https://github.com/wasabeef/Blurry}},
}
