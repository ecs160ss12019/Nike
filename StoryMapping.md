## User Stories:

-   As a player, I want to shoot with missiles.
-   As a player, I want to move my laserbase to avoid attack from invaders' missiles.
-   As a player, I want to hide behind the base shelters.
-   As an artistic player, I want to have a nice and beautiful UI that displays 
    all the objects and my score.


### Missile: (Zhiyuan Guo)
1. Moveable missiles   
2. missiles with different shape, sound effect, and animation effect
3. missiles with AOE, "debris" effect

### LaserBase: (Henry Yi)
1. Able to move my laserbase with 3 lives.
2. laserbase can shoot missiles, and will die if hit by invaders or resurrected if it still has lives.
3. laserbase can move faster under certain circumstances(combos or VIP), and has cool shapes(images).

### Invader: (Xuanchen Zhou, Henry Yi)
1. Invaders move from left to right(horizontally), and move down(vertically) once it reaches the wall.
2. Invaders can shoot missiles, and increases the frequency of shooting as their number reduces.
    Have some mystery invaders that move super fast that give user many points if user hits it.
3. Invaders have cool shapes(images).

### Base Shelter: (Roberto Lozano, Weili Yin)
1. Four base shelters.
2. Base shelters will shrink (lose pixels) if hit by invaders.
3. Base shelters have cool images.

### UI: (Zhiyuan Guo, Roberto Lozano)
1. UI has a black universe background.
2. UI has scores and all the objects.


### Sprint Chart

**User Stories**|**As a player I want to move my laserbase**|**to avoid attack from invaders’ missiles**|**while shooting with missiles**|**and hiding behind the base shelter**|**and have a nice and beautiful UI**
:-----:|:-----:|:-----:|:-----:|:-----:|:-----:
Sprint 1|Move my laserbase with 3 lives|Invaders move from left to right and move down once it reaches the wall|Moveable missiles|Four Base Shelters|A black universe blackground
Sprint 2|LaserBase can shoot missiles, and will die if hit by invaders or resurrected if it still has lives|Invaders can shoot missiles, and increase their speed as their number reduces. Have some mystery invaders that move super fast with bonus reward.|Missile with different shapes, sound effects,  and animation effects.|Base Shelters will shrink if hit by invaders.|UI displays scores and all the objects.
Sprint 3|LaserBase can move faster under certain circumstances, and has cool images|Invaders have cool images|Missiles with AOE and debris effect.|Baser shelter has cool images.| 


### Acceptance test/criteria

**User Stories Sprint 1**|**Able to move my laserbase with 3 lives**|**Invaders move from left to right and move down once it reaches the wall**|**Moveable Missiles**|**Four Base Shelters**|**UI has a black universe background**
:-----:|:-----:|:-----:|:-----:|:-----:|:-----:
Acceptance Criteria|User is able to see and move the laserbase left or right|User can see the movement of invaders on the screen|User can see that missile is moving up or down depending on who shoot it|User can see there are four base shelters located between invaders and laserbase|User is able to see it