## For Sprint 1

| LaserBase | |
|---|---|
|  - Able to move right or left when user touches the left/right part of the screen <br> - Has 3 lives <br> - Able to shoot missle when user touch the middle screen on the screen |  Missle <br> SpaceGame |



| Invader | |
|---|---|
|  - Generates and Manages the movement and size of invaders <br> - Detect the collision with missiles <br> - Spawns at the top of interface |  Missle <br> SpaceGame |



| BaseShelter | |
|---|---|
|  - Has the size of the baseShelter as well as how many pixels it has  <br> - Detect the collision with missiles <br> - Spawns at between invader and laserbase |  Missle <br> SpaceGame |


| Missile | |
|---|---|
|  - Controls the movement and size of missile  <br> - Emitted by invaders and laserbase with specified directions |  BaseShelter <br> Invader <br> LaserBase <br> SpaceGame |


| SpaceGame | |
|---|---|
|  - UI of this game  <br> - Shows the graphical background of the game, which is the universe <br> - Contains a list of objects in the game |  BaseShelter <br> Invader <br> LaserBase <br> Missile |



## Updates for Sprint 2

### Modified:

| BaseShelter | |
|---|---|
|  - Has an array of ShelterBlocks that make up a baseShelter  <br> - Detect the collision with missiles <br> - Spawns at between invader and laserbase |  Missle <br> SpaceGame |



### Added:

| InvaderGroup | |
|---|---|
|  - Manage a group of invaders|  Invader |



| ShelterBlock | |
|---|---|
|  - Make up each baseShelter  <br> - Each block should have height and width <br> - Detect whether a block is dead or not | BaseShelter |



| AnimatedObject | |
|---|---|
|  - Base class of every animated object created in the game that holds a view of object on the game screen and the SpaceGame object  <br> - Stores and manages view, animator, and handlers <br> - Handle notification issued by other objects <br> -Configure the animator listener | LaserBase <br> Invader <br> InvaderGroup <br> BaseShelter <br> BaseShelterGroup <br> HUD <br> Missle <br> SpaceGame |


| HUD | |
|---|---|
|  - Records information of status like Lifes, score, and level  <br> - Update them when variables change |  GameObject <br> SpaceGame <br> LaserBase |