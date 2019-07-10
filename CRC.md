| LaserBase | |
|---|---|
|  - Able to move right or left when user touches the left/right part of the screen <br> - Has 3 lives <br> - Able to shoot missle when user moves up fingers on the screen |  Missle <br> SpaceGame |



| Invader | |
|---|---|
|  - Controls the movement and size of invaders <br> - Detect the collision with missiles <br> - Spawns at the top of interface |  Missle <br> SpaceGame |



| BaseShelter | |
|---|---|
|  - Has the size of the baseShelter as well as how many pixels it has  <br> - Detect the collision with missiles <br> - Spawns at between invader and laserbase |  Missle <br> SpaceGame |


| Missile | |
|---|---|
|  - Controls the movement and size of missile  <br> - Emitted by invaders and laserbase with specified directions |  BaseShelter <br> Invader <br> LaserBase <br> SpaceGame |


| SpaceGame | |
|---|---|
|  - UI of this game  <br> - Shows the graphical background of the game, which is the universe <br> - Contains a list of objects in the game |  BaseShelter <br> Invader <br> LaserBase <br> Missile |

